package bramble.node.slave;

import java.io.Serializable;
import java.util.Collection;

public interface ISlaveNodeRunner{

    /**
     * Clients should override this method to call code relevant to their project.
     * 
     * @param initializationData - An array of data which is used to set the initial state, 
     * 				configuration, or limits of a computation.
     */
    public Collection<Serializable> runJob(Collection<Serializable> initializationData);

}
