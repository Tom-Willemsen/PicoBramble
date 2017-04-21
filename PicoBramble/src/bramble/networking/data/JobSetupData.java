package bramble.networking.data;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.Message;

public class JobSetupData extends Message {

    private static final long serialVersionUID = -4398535743362996636L;

    private final JobMetadata jobMetadata;
    private final Object initializationData;

    /**
     * Constructor.
     * 
     * @param jobMetadata - the job identifier of this job
     * @param initializationData - the initialization data of this job
     */
    public JobSetupData(JobMetadata jobMetadata, Object initializationData){
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
    public Object getInitializationData(){
	return initializationData;
    }

}
