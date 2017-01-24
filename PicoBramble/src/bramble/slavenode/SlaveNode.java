package bramble.slavenode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;
import bramble.genericnode.GenericNode;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;
import bramble.networking.ListenerServer;

public class SlaveNode<T extends IJobRunner> extends GenericNode implements Runnable {
	
	private final ListenerServer listenerServer;
	private volatile int jobID;
	private volatile ArrayList<Serializable> initializationData;
	
	private T jobRunner;
	
	public SlaveNode(T jobRunner) {
		ListenerServer listenerServer = null;
		this.jobRunner = jobRunner;
		
		try {
			listenerServer = new ListenerServer(BrambleConfiguration.SLAVE_PORT);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		this.listenerServer = listenerServer;
		
	}

	/**
	 * Will call listen() forever, meaning 
	 * that more than one job can be scheduled.
	 */
	public void listenForever() {
		while(true){
			listen();
		}
	}
	
	/**
	 * Sets up a listener server to listen for incoming jobs.
	 * 
	 * When a job is received, it sets jobID and 
	 * initializationData fields in this class, then calls run().
	 * 
	 */
	public void listen(){
		try {
			JobSetupData jobSetupData = (JobSetupData) listenerServer.listen();		
			startNewThread(jobSetupData);
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void startNewThread(JobSetupData jobSetupData){
		this.jobID = jobSetupData.getJobID();
		this.initializationData = jobSetupData.getInitializationData();

		new Thread(this.clone()).start();
	}
	
	/**
	 * Sends the data from a completed job back to the master node.
	 * 
	 * @param jobIdentifier - the unique job ID
	 * @param message - Status message.
	 * @param data - The data to send back to the master node in ArrayList form.
	 */
	public static synchronized void sendData(String senderIP, int jobIdentifier, String message, ArrayList<? extends Object> data){
		try{
			(new JobResponseData(senderIP, jobIdentifier, message, data)).send();
		} catch (IOException e) {
			System.out.println("Couldn't connect to master node. Aborting.");
			//e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Called when a job starts. By default, will 
	 * call runJob with the jobID and initialization 
	 * data as parameters.
	 * 
	 * Clients should override runJob() rather than run()
	 */
	public synchronized final void run(){
		jobRunner.runJob(this.jobID, this.initializationData);
	}
	
	
	public SlaveNode<T> clone(){
		return this.clone();	
	}
	
}
