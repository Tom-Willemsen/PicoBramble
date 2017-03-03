package bramble.node.controller;

import java.util.Collection;

import bramble.networking.data.JobMetadata;
import bramble.networking.data.JobSetupData;

public interface IControllerNodeRunner {

    /**
     * Defines a job based on it's job number.
     */
    public JobSetupData getJobSetupData(JobMetadata jobMetadata);

    /**
     * Gets the job numbers of all the jobs that need to be performed.
     * 
     * @return a collection containing the job numbers of all the jobs that need to be performed
     */
    public Collection<Integer> getAllJobNumbers();

}
