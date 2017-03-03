package bramble.networking;

import java.io.Serializable;
import java.util.Collection;

import bramble.configuration.BrambleConfiguration;
import bramble.node.controller.JobMetadata;

public class JobSetupData extends Message {

    private static final long serialVersionUID = -4398535743362996636L;

    private final JobMetadata jobMetadata;
    private final Collection<Serializable> initializationData;

    /**
     * Constructor.
     * 
     * @param jobMetadata - the job identifier of this job
     * @param initializationData - the initialization data of this job
     */
    public JobSetupData(JobMetadata jobMetadata, Collection<Serializable> initializationData){
	this.jobMetadata = jobMetadata;
	this.initializationData = initializationData;
	this.port = BrambleConfiguration.SLAVE_PORT;
    }

    /**
     * Gets the job identifier of this job.
     * @return the job identifier of this job
     */
    public JobMetadata getJobMetadata(){
	return jobMetadata;
    }

    /**
     * Gets the initialization data of this job.
     * @return the initialization data of this job
     */
    public Collection<Serializable> getInitializationData(){
	return initializationData;
    }

}
