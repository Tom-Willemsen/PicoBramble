package bramble.node.slave;

public interface ISlaveNodeRunner{

    /**
     * Clients should override this method to call code relevant to their project.
     * 
     * @param initializationData - Data which is used to set the initial state, 
     * 				configuration, or limits of a computation.
     */
    public Object runJob(Object initializationData);
    
    /**
     * Method called when there was an exception while sending data back to the master node. 
     * 
     * Use this method to call loggers etc.
     */
    public void onDataTransferError(Exception exception);
    
    /**
     * Method called when there was an exception while performing the calculation. 
     * 
     * Use this method to call loggers etc.
     */
    public void onCalculationError(Exception exception);

}
