package bramble.node.slave;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;

import bramble.concurrency.BrambleThreadPool;
import bramble.configuration.BrambleConfiguration;
import bramble.networking.ListenerServer;
import bramble.networking.data.JobMetadata;
import bramble.networking.data.JobResponseData;
import bramble.networking.data.JobSetupData;

public class SlaveNode implements Runnable {

    private final ISlaveNodeRunner jobRunner;
    private final BrambleThreadPool threadPool;
    private final String ipAddress;
    private final KeepAliveRunner keepAliveRunner;
    private ListenerServer listenerServer;

    /**
     * Constructor.
     * 
     * @param ipAddress - the IP address of this slave node
     * @param jobRunner - a jobRunner implementation to use when a job is received
     * @throws IOException - if creating a listener server automatically failed
     */
    public SlaveNode(String ipAddress, ISlaveNodeRunner jobRunner) throws IOException {
	this(ipAddress, jobRunner, new KeepAliveRunner(ipAddress), 
		new ListenerServer(BrambleConfiguration.SLAVE_PORT));
    }

    /**
     * Constructor, specifying a keep-alive runner and listener server.
     * 
     * @param ipAddress - the IP address of this slave node
     * @param jobRunner - a jobRunner implementation to use when a job is received
     * @param keepAliveRunner - a user-specified keep-alive runner
     * @param listenerServer - a user-specified listener server
     */
    public SlaveNode(String ipAddress, ISlaveNodeRunner jobRunner, 
	    KeepAliveRunner keepAliveRunner, ListenerServer listenerServer){

	this.threadPool = new BrambleThreadPool();
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
	threadPool.run(keepAliveRunner);
	threadPool.run(new Runnable(){
	    @Override
	    public void run(){
		while(true){
		    listen();
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
     */
    private void listen(){
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
    private void scheduleJob(final JobSetupData jobSetupData){	
	threadPool.run(new Runnable(){
	    public void run(){
		Collection<Serializable> result;
		result = jobRunner.runJob(jobSetupData.getInitializationData());
		try {
		    sendData(jobSetupData.getJobMetadata(), result);
		} catch (IOException e) {
		    jobRunner.onError(e);
		}
	    }
	});	
    }

    /**
     * Sends the data from a completed job back to the master node.
     * Constructs a new JobResponseData from it's parameters
     * 
     * @param jobMetadata - the metadata of this job
     * @param data - The data to send back to the master node
     * @throws IOException - if sending the data failed
     */
    public void sendData(JobMetadata jobMetadata, 
	    Collection<Serializable> data) throws IOException{
	sendData(new JobResponseData(ipAddress, jobMetadata, data));
    }

    /**
     * Sends the data from a completed job back to the master node.
     * 
     * @param jobResponseData - the job response data
     * @throws IOException - if sending the data failed
     */
    public synchronized void sendData(JobResponseData jobResponseData) 
	    throws IOException{
	jobResponseData.send();
    }

}
