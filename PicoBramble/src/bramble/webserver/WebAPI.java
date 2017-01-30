package bramble.webserver;

import bramble.configuration.BrambleConfiguration;
import bramble.node.controller.ControllerNode;
import bramble.node.controller.SlaveNodeInformation;

public final class WebAPI {

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
			if(controllerNode.getSlaveNodes().size() > 0){
				for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
					maxTemp = Double.max(maxTemp, slaveNode.getTemperature());
				}
				return Math.round(maxTemp*10)/10.0 + "C";
			} else {
				return "0 C";
			}
		} catch (NullPointerException e){
			return "0 C";
		}
	}
	
	public static final String getAvgClusterTemperature() {
		Double totalTemp = 0.0;
		try{
			if(controllerNode.getSlaveNodes().size() > 0){
				for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
					totalTemp += slaveNode.getTemperature();
				}
				
				totalTemp /= controllerNode.getSlaveNodes().size();
				
				return Math.round(totalTemp*10)/10.0 + "C";
			} else {
				return "0 C";
			}
		} catch (NullPointerException e){
			return "0 C";
		}
	}
	
	public synchronized static void publishMessage(String message){
		logMessages = message + "\r\n" + logMessages;
	}
	
	public static String getLogMessages(){
		return logMessages;
	}
	
}
