package bramble.webserver;

import bramble.configuration.BrambleConfiguration;
import bramble.node.controller.ControllerNode;
import bramble.node.controller.SlaveNodeInformation;

public final class WebAPI {
	
	/**
	 *  The minimum sensible temperature of a node.
	 */
	private static final Double MIN_TEMPERATURE = 10.0;
	
	/**
	 * The maximum sensible temperature of a node.
	 */
	private static final Double MAX_TEMPERATURE = 200.0;

	private static ControllerNode controllerNode;
	private static String logMessages = "Log messages";
	
	/**
	 * Sets the controllerNode used by this API.
	 * This is called whenever the controllerNode changes.
	 * @param controllerNode the new controller node to use
	 */
	synchronized public final static void setControllerNode(ControllerNode controllerNode){
		WebAPI.controllerNode = controllerNode;
	}
	
	/**
	 * Gets the number of free nodes in the cluster.
	 * @return the number of free nodes in the cluster
	 */
	public static final int getFreeJobSlots(){
		try{
			return controllerNode.getJobSlotsAvailable();
		} catch (NullPointerException e){
			return 0;
		}
	}
	
	/**
	 * Gets the number of free job slots in the cluster.
	 * @return the number of free job slots in the cluster
	 */
	public static final int getTotalJobSlots(){
		try{
			return (controllerNode.getSlaveNodes().size()*BrambleConfiguration.THREADS_PER_NODE);
		} catch (NullPointerException e){
			return 0;
		}
	}
	
	/**
	 * Gets the amount of slave nodes in the cluster.
	 * @return the amount of slave nodes in the cluster
	 */
	public static final int getTotalNodes(){
		try{
			return (controllerNode.getSlaveNodes().size());
		} catch (NullPointerException e){
			return 0;
		}
	}

	/**
	 * Gets the amount of jobs that have been completed.
	 * @return the amount of jobs that have been completed
	 */
	public static final int getCompletedJobsCount() {
		try{
			return (controllerNode.getCompletedJobs().size());
		} catch (NullPointerException e){
			return 0;
		}
	}

	/**
	 * Gets the amount of jobs that are in progress.
	 * @return the amount of jobs that are in progress.
	 */
	public static final int getJobsInProgressCount() {
		try{
			return (controllerNode.getStartedJobs().size() - controllerNode.getCompletedJobs().size());
		} catch (NullPointerException e){
			return 0;
		}
	}

	/**
	 * Gets the total amount of jobs.
	 * @return the total amount of jobs
	 */
	public static final int getTotalJobsCount() {
		try{
			return (controllerNode.getAllJobs().size());
		} catch (NullPointerException e){
			return 0;
		}
	}

	public static final String getMaxClusterTemperature() {
		Double maxTemp = 0.0;
		try{
			
			for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
				maxTemp = Double.max(maxTemp, slaveNode.getTemperature());
			}
			
			if(maxTemp > MIN_TEMPERATURE){
				return String.format( "%.1f C", maxTemp );
			} else {
				return "None";
			}
		} catch (NullPointerException e){
			return "None";
		}
	}
	
	public static final String getAvgClusterTemperature() {
		Double totalTemp = 0.0;
		try{
			int size = controllerNode.getSlaveNodes().size();
			
			for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
				Double temperature = slaveNode.getTemperature();
				if(temperature > MIN_TEMPERATURE){
					totalTemp += temperature;
				} else {
					size -= 1;
				}
			}
			
			if(size > 0){	
				return String.format( "%.1f C", totalTemp/size );
			} else {
				return "None";
			}
		} catch (NullPointerException e){
			return "None";
		}
	}
	
	public static final String getMinClusterTemperature() {
		Double minTemp = MAX_TEMPERATURE;
		try{
			for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
				Double temperature = slaveNode.getTemperature();
				if(temperature > MIN_TEMPERATURE){
					minTemp = Double.min(minTemp, temperature);
				} 
			}
			
			if(minTemp < MAX_TEMPERATURE){	
				return String.format( "%.1f C", minTemp );
			} else {
				return "None";
			}
		} catch (NullPointerException e){
			return "None";
		}
	}
	
	public synchronized static void publishMessage(String message){
		logMessages = message + "\r\n" + logMessages;
	}
	
	public static String getLogMessages(){
		return logMessages;
	}
	
}
