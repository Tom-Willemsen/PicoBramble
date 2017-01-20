package bramble.networking;

import java.util.ArrayList;

public class JobResponseData extends Message {
	
	private static final long serialVersionUID = 7284857353450411695L;
	
	private int jobIdentifier;
	private String message;
	private ArrayList<? extends Object> dataArrayList;
	
	/**
	 * Constructor
	 * 
	 * @param message - the message to send along with the data
	 * @param dataArrayList - an ArrayList of the data to be sent
	 */
	public JobResponseData(int jobIdentifier, String message, ArrayList<? extends Object> dataArrayList) {
		this.jobIdentifier = jobIdentifier;
		this.message = message;
		this.dataArrayList = dataArrayList;
	}
	
	/**
	 * Gets the message sent with the data
	 * @return the message sent with the data
	 */
	public String getMessage(){
		return message;
	}
	
	/**
	 * Gets an ArrayList containing the data to be sent
	 * @return the data in an ArrayList
	 */
	public ArrayList<? extends Object> getData(){
		return dataArrayList;
	}
	
	/**
	 * Gets the job identifier.
	 * @return the numeric job ID.
	 */
	public int getJobIdentifier(){
		return jobIdentifier;
	}
	
	/**
	 * Sets the ArrayList containing the data to be sent
	 * @param data - the data to be sent
	 */
	public void setData(ArrayList<? extends Object> data){
		this.dataArrayList = data;
	}
	
	/**
	 * Sets the message to be sent along with the data
	 * @param data - the message to be sent along with the data
	 */
	public void setMessage(ArrayList<? extends Object> data){
		this.dataArrayList = data;
	}

}
