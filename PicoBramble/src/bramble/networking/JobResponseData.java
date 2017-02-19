package bramble.networking;

import java.io.Serializable;
import java.util.Collection;

import bramble.configuration.BrambleConfiguration;

public class JobResponseData extends Message {
	
	private static final long serialVersionUID = 7284857353450411695L;
	
	private final int jobIdentifier;
	private final String message;
	private final Collection<Serializable> dataArrayList;
	private final String senderIpAddress;
	
	/**
	 * Constructor.
	 * 
	 * @param message - the message to send along with the data
	 * @param dataArrayList - an ArrayList of the data to be sent
	 */
	public JobResponseData(String senderIpAddress, int jobIdentifier, 
		String message, Collection<Serializable> dataArrayList) {
	    
		this.senderIpAddress = senderIpAddress;
		this.jobIdentifier = jobIdentifier;
		this.message = message;
		this.dataArrayList = dataArrayList;
		this.port = BrambleConfiguration.MASTER_PORT;
	}
	
	/**
	 * Gets the message sent with the data.
	 * @return the message sent with the data
	 */
	public synchronized String getMessage(){
		return message;
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
	public synchronized int getJobIdentifier(){
		return jobIdentifier;
	}
	
	/**
	 * Gets the ip address of the node that sent this data.
	 * @return the ip address of the node that sent this data
	 */
	public synchronized String getSenderIpAddress(){
		return senderIpAddress;
	}

}
