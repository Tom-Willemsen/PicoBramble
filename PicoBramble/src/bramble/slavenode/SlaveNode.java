package bramble.slavenode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;
import bramble.genericnode.GenericNode;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;
import bramble.networking.ListenerServer;

public abstract class SlaveNode extends GenericNode implements Runnable, Cloneable {
	
	private final ListenerServer listenerServer;
	private volatile int jobID;
	private volatile ArrayList<Serializable> initializationData;
	
	protected SlaveNode() {
		ListenerServer listenerServer = null;
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
		new Thread((Runnable) this).start();
	}
	
	/**
	 * Sends the data from a completed job back to the master node.
	 * 
	 * @param jobIdentifier - the unique job ID
	 * @param message - Status message.
	 * @param data - The data to send back to the master node in ArrayList form.
	 */
	public static synchronized void sendData(int jobIdentifier, String message, ArrayList<? extends Object> data){
		try{
			(new JobResponseData(jobIdentifier, message, data)).send();
		} catch (IOException e) {
			System.out.println("Couldn't connect to master node. Aborting.");
			e.printStackTrace();
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
		System.out.println("DEBUG 1");
		runJob(this.jobID, this.initializationData);
	}
	
	/**
	 * Clients should override this method to call code relevant to their project.
	 * 
	 * @param jobID - the job identifier. This 
	 * 					should be saved and sent back to the master 
	 * 					node along with any relevant computation 
	 * 					data once the job is finished.
	 * 
	 * @param initializationData - An array of data which is used to 
	 * 							set the initial state, configuration, 
	 * 							or limits of a computation.
	 */
	protected abstract void runJob(int jobID, ArrayList<Serializable> initializationData);

}
