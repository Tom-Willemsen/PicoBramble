package bramble.node.manager;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import bramble.node.controller.ControllerNode;
import bramble.node.controller.IControllerNodeRunner;
import bramble.node.master.IMasterNodeRunner;
import bramble.node.master.MasterNode;
import bramble.webserver.WebAPIServer;
import bramble.webserver.WebServer;

public class Manager {
	
	private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private MasterNode<? extends IMasterNodeRunner> masterNode;
	private ControllerNode<? extends IControllerNodeRunner> controllerNode;
	
	public Manager(IMasterNodeRunner masterNodeRunner, IControllerNodeRunner controllerNodeRunner) throws IOException {
		masterNode = new MasterNode<>(this, masterNodeRunner);
		controllerNode = new ControllerNode<>(controllerNodeRunner);
	}
	
	public void launchAll(){		
		startMasterNodeRunner();
		startControllerNodeRunner();
		startWebAPIServer();
		startWebServer();
	}
	
	/**
	 * Starts the controller node runner in a new thread.
	 */
	private void startMasterNodeRunner(){
		executor.execute(masterNode);
	}
	
	/**
	 * Starts the controller node runner in a new thread.
	 */
	private void startControllerNodeRunner(){
		executor.execute(controllerNode);
	}
	
	/**
	 * Starts a new webserver, which serves the web API
	 */
	private void startWebAPIServer(){
		executor.execute(new WebAPIServer());
	}
	
	/**
	 * Starts a new webserver, which serves the webpages
	 */
	private void startWebServer(){
		executor.execute(new WebServer());
	}
	
	/**
	 * Getter for the controller node
	 */
	public ControllerNode<? extends IControllerNodeRunner> getControllerNode(){
		return controllerNode;
	}
}
