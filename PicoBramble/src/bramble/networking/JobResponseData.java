package bramble.networking;

import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;

public class JobResponseData extends Message {
	
	private static final long serialVersionUID = 7284857353450411695L;
	
	private final int jobIdentifier;
	private final String message;
	private final ArrayList<? extends Object> dataArrayList;
	private final String senderIP;
	
	/**
	 * Constructor
	 * 
	 * @param message - the message to send along with the data
	 * @param dataArrayList - an ArrayList of the data to be sent
	 */
	public JobResponseData(String senderIP, int jobIdentifier, String message, ArrayList<? extends Object> dataArrayList) {
		this.senderIP = senderIP;
		this.jobIdentifier = jobIdentifier;
		this.message = message;
		this.dataArrayList = dataArrayList;
		this.port = BrambleConfiguration.MASTER_PORT;
	}
	
	/**
	 * Gets the message sent with the data
	 * @return the message sent with the data
	 */
	public synchronized String getMessage(){
		return message;
	}
	
	/**
	 * Gets an ArrayList containing the data to be sent
	 * @return the data in an ArrayList
	 */
	public synchronized ArrayList<? extends Object> getData(){
		return dataArrayList;
	}
	
	/**
	 * Gets the job identifier.
	 * @return the numeric job ID.
	 */
	public synchronized int getJobID(){
		return jobIdentifier;
	}
	
	public synchronized String getSenderIP(){
		return senderIP;
	}

}
