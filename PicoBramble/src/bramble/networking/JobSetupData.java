package bramble.networking;

import java.io.Serializable;
import java.util.ArrayList;

public class JobSetupData extends Message {

	private static final long serialVersionUID = -4398535743362996636L;
	
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
