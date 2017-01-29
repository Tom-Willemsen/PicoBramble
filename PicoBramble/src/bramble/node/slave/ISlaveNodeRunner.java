package bramble.node.slave;

import java.io.Serializable;
import java.util.ArrayList;

public interface ISlaveNodeRunner{
	
	/**
	 * Clients should override this method to call code relevant to their project.
	 * 
	 * @param jobID - the job identifier. This 
	 * 					should be saved and sent back to the master 
	 * 					node along with any relevant computation 
	 * 					data once the job is finished.
	 * 
	 * @param initializationData - An array of data which is used to 
	 * 							set the initial state, configuration, 
	 * 							or limits of a computation.
	 */
	public void runJob(int jobID, ArrayList<Serializable> initializationData);

}
