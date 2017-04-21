package bramble.networking.data;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.Message;

public class JobResponseData extends Message {

    private static final long serialVersionUID = 7284857353450411695L;

    private final JobMetadata jobMetadata;
    private final Object data;
    private final String senderIpAddress;

    /**
     * Constructor.
     * 
     * @param data - the data to be sent
     */
    public JobResponseData(String senderIpAddress, 
	    JobMetadata jobMetadata, Object data) {

	this.senderIpAddress = senderIpAddress;
	this.jobMetadata = jobMetadata;
	this.data = data;
	this.port = BrambleConfiguration.MASTER_PORT;
    }

    /**
     * Gets the data to be sent.
     * @return the data
     */
    public synchronized Object getData(){
	return data;
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
