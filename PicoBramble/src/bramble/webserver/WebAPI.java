package bramble.webserver;

import bramble.controllernode.ControllerNode;

public final class WebAPI {

	private static ControllerNode controllerNode;
	
	synchronized public final static void setControllerNode(ControllerNode controllerNode){
		WebAPI.controllerNode = controllerNode;
	}
	
	public static final int getFreeJobSlots(){
		try{
			return controllerNode.getJobSlotsAvailable();
		} catch (NullPointerException e){
			return 0;
		}
	}
}
