package bramble.webserver;

import java.util.ArrayList;

import bramble.configuration.BrambleConfiguration;
import bramble.controllernode.ControllerNode;
import bramble.controllernode.SlaveNodeInformation;

public final class WebAPI {

	private static ControllerNode controllerNode;
	
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
	
	public static final int getTotalJobSlots(){
		ArrayList<SlaveNodeInformation> slaveNodes = controllerNode.getSlaveNodes();
		return (slaveNodes.size()*BrambleConfiguration.THREADS_PER_NODE);
	}
	
	public static final int getTotalNodes(){
		ArrayList<SlaveNodeInformation> slaveNodes = controllerNode.getSlaveNodes();
		return (slaveNodes.size());
	}
	
}
