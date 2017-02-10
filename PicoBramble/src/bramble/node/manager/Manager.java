package bramble.node.manager;

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
	private static MasterNode<IMasterNodeRunner> masterNode = null;
	private static ControllerNode controllerNode = null;
	
	public static final void setup(IMasterNodeRunner masterNodeRunner, IControllerNodeRunner controllerNodeRunner){
		masterNode = new MasterNode<>(masterNodeRunner);
		controllerNode = new ControllerNode(controllerNodeRunner);
	}
	
	public static final void launchAll(){
		
		if(masterNode == null || controllerNode == null){
			System.out.println("Can't launch modules before setup.");
			return;
		}
		
		startMasterNodeRunner();
		startControllerNodeRunner();
		startWebAPIServer();
		startWebServer();
	}
	
	/**
	 * Starts the controller node runner in a new thread.
	 */
	private static final void startMasterNodeRunner(){
		try{
			executor.execute(masterNode);
		} catch (NullPointerException e){
			System.out.println("Couldn't start the master node - check it was set before being run");
		}
	}
	
	/**
	 * Starts the controller node runner in a new thread.
	 */
	private static final void startControllerNodeRunner(){
		try{
			executor.execute(controllerNode);
		} catch (NullPointerException e){
			System.out.println("Couldn't start the controller node - check it was set before being run");
		}
	}
	
	/**
	 * Starts a new webserver, which serves the web API
	 */
	private static void startWebAPIServer(){
		executor.execute(new WebAPIServer());
	}
	
	/**
	 * Starts a new webserver, which serves the webpages
	 */
	private static void startWebServer(){
		executor.execute(new WebServer());
	}
	
	/**
	 * Getter for the controller node
	 */
	public static final ControllerNode getControllerNode(){
		return controllerNode;
	}
}
