package bramble.node.master;

import bramble.networking.JobResponseData;

public interface IMasterNodeRunner {

	/**
	 * Parses job response data. 
	 * 
	 * This method could pass the data to an analysis class, 
	 * or simply save it to a file for future inspection.
	 * 
	 * @param jobResponseData - The jobResponseData to be parsed.
	 */
	public void parse(JobResponseData jobResponseData);

}
