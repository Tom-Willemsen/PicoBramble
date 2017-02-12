package bramble.node.slave;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;
import bramble.networking.ListenerServer;

public class SlaveNode<T extends ISlaveNodeRunner> implements Runnable {
	
	private final T jobRunner;
	private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private static String ipAddress;

	/**
	 * Constructor
	 * @param ipAddress - the IP address of this slave node.
	 * @param jobRunner - a jobRunner implementation to use when a job is received.
	 */
	public SlaveNode(String ipAddress, T jobRunner) {
		this(jobRunner);
		setIpAddress(ipAddress);		
	}
	
	/**
	 * Copy constructor, doesn't (re)set IP address.
	 * @param jobRunner
	 */
	private SlaveNode(T jobRunner){
		this.jobRunner = jobRunner;
	}

	/**
	 * Will call listen() forever, meaning 
	 * that more than one job can be scheduled.
	 */
	@Override
	public void run() {
		
		executor.execute(new KeepAliveRunner(ipAddress));
		
		ListenerServer listenerServer;
		try {
			listenerServer = new ListenerServer(BrambleConfiguration.SLAVE_PORT);
		} catch (IOException e) {
			System.out.println("Can't set up more than one listener on the same port.");
			return;
		}
		
		while(true){
			listen(listenerServer);
		}
	}
	
	/**
	 * Sets up a listener server to listen for incoming jobs.
	 * 
	 * When a job is received, it sets jobID and 
	 * initializationData fields in this class, then calls run().
	 * 
	 */
	private void listen(ListenerServer listenerServer){
		try {
			JobSetupData jobSetupData = (JobSetupData) listenerServer.listen();	
			if(jobSetupData != null && jobSetupData instanceof JobSetupData){
				scheduleJob(jobSetupData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts a new thread in which to run a job.
	 * @param jobSetupData - the job setup data to run the job with.
	 */
	private void scheduleJob(JobSetupData jobSetupData){	
		executor.execute(new Runnable(){
			public void run(){
				jobRunner.runJob(jobSetupData.getJobID(), jobSetupData.getInitializationData());
			}
		});	
	}
	
	/**
	 * Sends the data from a completed job back to the master node.
	 * 
	 * @param jobIdentifier - the unique job ID
	 * @param message - Status message.
	 * @param data - The data to send back to the master node in ArrayList form.
	 */
	public static void sendData(int jobIdentifier, String message, ArrayList<? extends Serializable> data){
		try{
			(new JobResponseData(ipAddress, jobIdentifier, message, data)).send();
		} catch (IOException e) {
			System.out.println("Couldn't connect to master node. Data was not sent.");
		}
	}

	/**
	 * Sets the IP address of this slave node.
	 * @param ipAddress - the IP address of this slave node
	 */
	private static void setIpAddress(String ipAddress) {
		SlaveNode.ipAddress = ipAddress;
	}
	
}
