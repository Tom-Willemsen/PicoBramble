package bramble.node.master;

import bramble.networking.data.JobResponseData;

public interface IMasterNodeRunner {

    /**
     * Parses job response data. 
     * 
     * <p>This method could pass the data to an analysis class, 
     * or simply save it to a file for future inspection.</p>
     * 
     * @param jobResponseData - The jobResponseData to be parsed.
     */
    public void parseJobResponseData(JobResponseData jobResponseData);

}
