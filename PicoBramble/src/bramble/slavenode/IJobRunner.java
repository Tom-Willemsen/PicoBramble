package bramble.slavenode;

import java.util.ArrayList;

public interface IJobRunner extends Runnable {
	public void initializeJob(int jobID, ArrayList<?> initializationData);
}
