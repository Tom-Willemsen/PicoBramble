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
			startNewThread(jobSetupData);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void startNewThread(JobSetupData jobSetupData){
		this.jobID = jobSetupData.getJobID();
		this.initializationData = jobSetupData.getInitializationData();

		new Thread((Runnable) this.clone()).start();
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
		return new SlaveNode<T>(this.jobRunner);
	}
	
}
