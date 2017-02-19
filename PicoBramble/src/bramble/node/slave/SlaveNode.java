package bramble.node.slave;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import bramble.configuration.BrambleConfiguration;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;
import bramble.networking.ListenerServer;

public class SlaveNode implements Runnable {
	
	private final ISlaveNodeRunner jobRunner;
	private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private final String ipAddress;
	private final KeepAliveRunner keepAliveRunner;
	private ListenerServer listenerServer;

	/**
	 * Constructor
	 * @param ipAddress - the IP address of this slave node.
	 * @param jobRunner - a jobRunner implementation to use when a job is received.
	 * @throws IOException 
	 */
	public SlaveNode(String ipAddress, ISlaveNodeRunner jobRunner) throws IOException {
		this(ipAddress, jobRunner, new KeepAliveRunner(ipAddress), new ListenerServer(BrambleConfiguration.SLAVE_PORT));
	}
	
	/**
	 * 
	 */
	public SlaveNode(String ipAddress, ISlaveNodeRunner jobRunner, KeepAliveRunner keepAliveRunner, ListenerServer listenerServer){
		this.jobRunner = jobRunner;
		this.ipAddress = ipAddress;	
		this.keepAliveRunner = keepAliveRunner;
		this.listenerServer = listenerServer;
	}

	/**
	 * Will listen forever for input data.
	 */
	@Override
	public void run() {
		executor.execute(keepAliveRunner);
		executor.execute(new Runnable(){
			@Override
			public void run(){
				while(true){
					listen(listenerServer);
					try {
						Thread.sleep(BrambleConfiguration.LISTENER_DELAY_MS);
					} catch (InterruptedException e) {
						LogManager.getLogger().error("Interrupted while sleeping.", e);
						return;
					}
				}
			}
		});
	}
	
	/**
	 * Sets up a listener server to listen for incoming jobs.
	 * 
	 * When a job is received, it sets jobID and 
	 * initializationData fields in this class, then calls run().
	 * 
	 */
	private void listen(ListenerServer listenerServer){
		JobSetupData jobSetupData = null;
		
		try {
			jobSetupData = (JobSetupData) listenerServer.listen();
		} catch (IOException e) {
			LogManager.getLogger().error("Interrupted while sleeping.", e);
			return;
		} catch (ClassCastException e){
			LogManager.getLogger().error("Couldn't cast Message to JobSetupData.", e);
			return;
		}
		
		if(jobSetupData != null){
			scheduleJob(jobSetupData);
		}
	}
	
	/**
	 * Starts a new thread in which to run a job.
	 * @param jobSetupData - the job setup data to run the job with.
	 */
	private void scheduleJob(JobSetupData jobSetupData){	
		executor.execute(new Runnable(){
			public void run(){
				jobRunner.runJob(jobSetupData.getJobIdentifier(), jobSetupData.getInitializationData());
			}
		});	
	}
	
	/**
	 * Sends the data from a completed job back to the master node.
	 * Constructs a new JobResponseData from it's parameters
	 * 
	 * @param jobIdentifier - the unique job ID
	 * @param message - Status message
	 * @param data - The data to send back to the master node
	 * @throws IOException - if sending the data failed
	 */
	public void sendData(int jobIdentifier, String message, Collection<Serializable> data) throws IOException{
		sendData(new JobResponseData(ipAddress, jobIdentifier, message, data));
	}
	
	/**
	 * Sends the data from a completed job back to the master node.
	 * 
	 * @param jobResponseData - the job response data
	 * @throws IOException - if sending the data failed
	 */
	public synchronized void sendData(JobResponseData jobResponseData) throws IOException{
		jobResponseData.send();
	}
	
}
