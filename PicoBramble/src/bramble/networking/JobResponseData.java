package bramble.networking;

import java.io.Serializable;
import java.util.Collection;

import bramble.configuration.BrambleConfiguration;
import bramble.node.controller.JobMetadata;

public class JobResponseData extends Message {

    private static final long serialVersionUID = 7284857353450411695L;

    private final JobMetadata jobMetadata;
    private final Collection<Serializable> dataArrayList;
    private final String senderIpAddress;

    /**
     * Constructor.
     * 
     * @param dataArrayList - an ArrayList of the data to be sent
     */
    public JobResponseData(String senderIpAddress, 
	    JobMetadata jobMetadata, Collection<Serializable> dataArrayList) {

	this.senderIpAddress = senderIpAddress;
	this.jobMetadata = jobMetadata;
	this.dataArrayList = dataArrayList;
	this.port = BrambleConfiguration.MASTER_PORT;
    }

    /**
     * Gets an ArrayList containing the data to be sent.
     * @return the data in an ArrayList
     */
    public synchronized Collection<Serializable> getData(){
	return dataArrayList;
    }

    /**
     * Gets the job identifier.
     * @return the numeric job ID.
     */
    public synchronized JobMetadata getJobMetadata(){
	return jobMetadata;
    }

    /**
     * Gets the ip address of the node that sent this data.
     * @return the ip address of the node that sent this data
     */
    public synchronized String getSenderIpAddress(){
	return senderIpAddress;
    }

}
