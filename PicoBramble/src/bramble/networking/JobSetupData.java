package bramble.networking;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;

public class JobSetupData extends Message {

	private static final long serialVersionUID = -4398535743362996636L;
	protected int port = BrambleConfiguration.SLAVE_PORT;
	
	private int jobID;
	private ArrayList<Serializable> initializationData;
	
	public JobSetupData(int jobID, ArrayList<Serializable> initializationData){
		this.jobID = jobID;
		this.initializationData = initializationData;
	}
	
	public int getJobID(){
		return jobID;
	}
	
	public ArrayList<Serializable> getInitializationData(){
		return initializationData;
	}

}
