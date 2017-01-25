package bramble.slavenode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;
import bramble.genericnode.GenericNode;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;
import bramble.networking.ListenerServer;

public class SlaveNode<T extends ISlaveNodeRunner> extends GenericNode implements Runnable, Cloneable {
	
	private volatile int jobID;
	private volatile ArrayList<Serializable> initializationData;
	
	private T jobRunner;
	
	/**
	 * Constructor
	 * @param jobRunner - a jobRunner implementation to use when a job is received.
	 */
	public SlaveNode(T jobRunner) {
		this.jobRunner = jobRunner;
	}

	/**
	 * Will call listen() forever, meaning 
	 * that more than one job can be scheduled.
	 */
	public void listenForever() {
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
				startNewThread(jobSetupData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts a new thread in which to run a job.
	 * @param jobSetupData - the jobSetupData to run the job with.
	 */
	private synchronized void startNewThread(JobSetupData jobSetupData){
		
		SlaveNode<T> newThreadSlaveNode = this.clone();
		
		newThreadSlaveNode.jobID = jobSetupData.getJobID();
		newThreadSlaveNode.initializationData = jobSetupData.getInitializationData();

		new Thread(newThreadSlaveNode).start();
	}
	
	/**
	 * Sends the data from a completed job back to the master node.
	 * 
	 * @param jobIdentifier - the unique job ID
	 * @param message - Status message.
	 * @param data - The data to send back to the master node in ArrayList form.
	 */
	public static void sendData(String senderIP, int jobIdentifier, String message, ArrayList<? extends Object> data){
		try{
			(new JobResponseData(senderIP, jobIdentifier, message, data)).send();
		} catch (IOException e) {
			System.out.println("Couldn't connect to master node. Aborting.");
			//e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * This tells the jobRunner to run a job.
	 * This method is called from Thread.new().
	 */
	public synchronized final void run(){
		jobRunner.runJob(this.jobID, this.initializationData);
	}
	
	/**
	 * A clone implementation.
	 */
	public final SlaveNode<T> clone(){
		return new SlaveNode<T>(this.jobRunner);
	}
	
}
