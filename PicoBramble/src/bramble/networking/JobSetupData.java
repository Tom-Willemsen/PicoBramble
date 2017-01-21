package bramble.networking;

import java.io.Serializable;
import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;

public class JobSetupData extends Message {

	private static final long serialVersionUID = -4398535743362996636L;
	
	private final int jobID;
	private final ArrayList<Serializable> initializationData;
	
	public JobSetupData(int jobID, ArrayList<Serializable> initializationData){
		this.jobID = jobID;
		this.initializationData = initializationData;
		this.port = BrambleConfiguration.SLAVE_PORT;
	}
	
	public final int getJobID(){
		return jobID;
	}
	
	public final ArrayList<Serializable> getInitializationData(){
		return initializationData;
	}

}
