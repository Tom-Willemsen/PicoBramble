package bramble.node.manager;

import bramble.node.controller.ControllerNode;

public interface IManager {
	
	public void execute(Runnable task);
	
	public ControllerNode getControllerNode();

}
