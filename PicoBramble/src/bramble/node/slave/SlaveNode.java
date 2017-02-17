package bramble.node.slave;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;
import bramble.networking.ListenerServer;

public class SlaveNode implements Runnable {
	
	private final ISlaveNodeRunner jobRunner;
	private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private final String ipAddress;

	/**
	 * Constructor
	 * @param ipAddress - the IP address of this slave node.
	 * @param jobRunner - a jobRunner implementation to use when a job is received.
	 */
	public SlaveNode(String ipAddress, ISlaveNodeRunner jobRunner) {
		this.jobRunner = jobRunner;
		this.ipAddress = ipAddress;		
	}

	/**
	 * Will listen forever for input data.
	 */
	@Override
	public final void run() {
		
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
			try {
				Thread.sleep(BrambleConfiguration.LISTENER_DELAY_MS);
			} catch (InterruptedException e) {
				return;
			}
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
			if(jobSetupData != null){
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
	public synchronized void sendData(int jobIdentifier, String message, Collection<Serializable> data) throws IOException{
		(new JobResponseData(ipAddress, jobIdentifier, message, data)).send();
	}
	
}
