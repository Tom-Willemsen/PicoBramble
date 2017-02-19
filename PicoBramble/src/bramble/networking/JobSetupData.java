package bramble.networking;

import java.io.Serializable;
import java.util.Collection;

import bramble.configuration.BrambleConfiguration;

public class JobSetupData extends Message {

    private static final long serialVersionUID = -4398535743362996636L;

    private final int jobIdentifier;
    private final Collection<Serializable> initializationData;

    /**
     * Constructor.
     * 
     * @param jobIdentifier - the job identifier of this job
     * @param initializationData - the initialization data of this job
     */
    public JobSetupData(int jobIdentifier, Collection<Serializable> initializationData){
	this.jobIdentifier = jobIdentifier;
	this.initializationData = initializationData;
	this.port = BrambleConfiguration.SLAVE_PORT;
    }

    /**
     * Gets the job identifier of this job.
     * @return the job identifier of this job
     */
    public int getJobIdentifier(){
	return jobIdentifier;
    }

    /**
     * Gets the initialization data of this job.
     * @return the initialization data of this job
     */
    public Collection<Serializable> getInitializationData(){
	return initializationData;
    }

}
