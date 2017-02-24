package bramble.webserver;

import java.util.Collection;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import bramble.configuration.BrambleConfiguration;
import bramble.node.controller.ControllerNode;
import bramble.node.controller.SlaveNodeInformation;

public class WebApi {

    /**
     *  The minimum sensible temperature of a node.
     */
    private static Double MIN_TEMPERATURE = 10.0;

    /**
     * The maximum sensible temperature of a node.
     */
    private static Double MAX_TEMPERATURE = 200.0;

    /**
     * The minimum sensible clock speed of a node.
     */
    private static Double MIN_CPU_SPEED = 1.0;

    /**
     * The maximum sensible clock speed of a node.
     */
    private static Double MAX_CPU_SPEED = 10000000000.0;

    private static ControllerNode controllerNode;
    private static Collection<String> logMessages = 
	    new CircularFifoQueue<>(BrambleConfiguration.WEB_API_MESSAGE_QUEUE_LENGTH);
    
    static{
	logMessages.add("Log messages");
	logMessages.add("Most recent messages will appear last");
    }

    /**
     * Sets the controllerNode used by this API.
     * This is called whenever the controllerNode changes.
     * @param controllerNode the new controller node to use
     */
    public static void setControllerNode(ControllerNode controllerNode){
	WebApi.controllerNode = controllerNode;
    }

    /**
     * Gets the number of free nodes in the cluster.
     * @return the number of free nodes in the cluster
     */
    public static int getFreeJobSlots(){
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
    public static int getTotalJobSlots(){
	try{
	    return (controllerNode.getSlaveNodes().size()
		    *BrambleConfiguration.THREADS_PER_NODE);
	} catch (NullPointerException e){
	    return 0;
	}
    }

    /**
     * Gets the amount of slave nodes in the cluster.
     * @return the amount of slave nodes in the cluster
     */
    public static int getTotalNodes(){
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
    public static int getCompletedJobsCount() {
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
    public static int getJobsInProgressCount() {
	try{
	    return (controllerNode.getStartedJobs().size() 
		    - controllerNode.getCompletedJobs().size());
	} catch (NullPointerException e){
	    return 0;
	}
    }

    /**
     * Gets the total amount of jobs.
     * @return the total amount of jobs
     */
    public static int getTotalJobsCount() {
	try{
	    return (controllerNode.getAllJobs().size());
	} catch (NullPointerException e){
	    return 0;
	}
    }

    /**
     * Gets the maximum temperature of any node in the cluster.
     * @return the maximum temperature of any node in the cluster
     */
    public static String getMaxClusterTemperature() {
	Double maxTemp = 0.0;
	try{

	    for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
		maxTemp = Double.max(maxTemp, slaveNode.getNodeDiagnostics().getTemperature());
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

    /**
     * Gets the average temperature of the cluster.
     * @return the average temperature of the cluster
     */
    public static String getAvgClusterTemperature() {
	Double totalTemp = 0.0;
	try{
	    int size = controllerNode.getSlaveNodes().size();

	    for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
		Double temperature = slaveNode.getNodeDiagnostics().getTemperature();
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

    /**
     * Gets the minimum temperature of any node in the cluster.
     * @return the minimum temperature of any node in the cluster
     */
    public static String getMinClusterTemperature() {
	Double minTemp = MAX_TEMPERATURE;
	try{
	    for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
		Double temperature = slaveNode.getNodeDiagnostics().getTemperature();
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

    /**
     * Gets the minimum CPU speed of any node in the cluster.
     * @return the minimum CPU speed of any node in the cluster
     */
    public static String getMinClusterCpuSpeed(){
	Double minClockSpeed = MAX_CPU_SPEED;
	try{
	    for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
		Double clockSpeed = slaveNode.getNodeDiagnostics().getClockSpeed();
		if(clockSpeed > MIN_CPU_SPEED){
		    minClockSpeed = Double.min(minClockSpeed, clockSpeed);
		} 
	    }

	    if(minClockSpeed < MAX_CPU_SPEED){	
		return String.format( "%.2f GHz", minClockSpeed/1000000 );
	    } else {
		return "None";
	    }
	} catch (NullPointerException e){
	    return "None";
	}
    }

    /**
     * Gets the average CPU speed of all the nodes in the cluster.
     * @return the average CPU speed of all the nodes in the cluster
     */
    public static String getAvgClusterCpuSpeed() {
	Double totalSpeed = 0.0;
	try{
	    int size = controllerNode.getSlaveNodes().size();

	    for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
		Double speed = slaveNode.getNodeDiagnostics().getClockSpeed();
		if(speed > MIN_CPU_SPEED){
		    totalSpeed += speed;
		} else {
		    size -= 1;
		}
	    }

	    if(size > 0){	
		return String.format( "%.2f GHz", totalSpeed/(size*1000000) );
	    } else {
		return "None";
	    }
	} catch (NullPointerException e){
	    return "None";
	}
    }
    
    /**
     * Gets the maximum CPU speed of any node in the cluster.
     * @return the maximum CPU speed of any node in the cluster
     */
    public static String getMaxClusterCpuSpeed() {
	Double maxSpeed = 0.0;
	try{

	    for(SlaveNodeInformation slaveNode : controllerNode.getSlaveNodes()){
		maxSpeed = Double.max(maxSpeed, slaveNode.getNodeDiagnostics().getClockSpeed());
	    }

	    if(maxSpeed > MIN_CPU_SPEED){
		return String.format( "%.2f GHz", maxSpeed/1000000 );
	    } else {
		return "None";
	    }
	} catch (NullPointerException e){
	    return "None";
	}
    }

    /**
     * Publishes a message to the web API.
     * @param message - a message to publish
     */
    public static synchronized void publishMessage(String message){
	logMessages.add(message);
    }

    /**
     * Gets all the messages published by the web API.
     * @return all the messages published by the web API
     */
    public static String getLogMessages(){
	StringBuilder result = new StringBuilder();
	
	for(String message : logMessages){
	    result.append(message).append("\n");
	}
	return result.toString();
    }

    /**
     * Gets the name of this cluster.
     * @return the name of this cluster
     */
    public static String getName() {
	return BrambleConfiguration.NAME;
    }

}
