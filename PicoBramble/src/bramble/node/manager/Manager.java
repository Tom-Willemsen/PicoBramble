package bramble.node.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import bramble.node.controller.ControllerNode;
import bramble.node.controller.IControllerNodeRunner;
import bramble.node.master.IMasterNodeRunner;
import bramble.node.master.MasterNode;
import bramble.webserver.WebServer;

public class Manager {
	
	private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private static MasterNode<IMasterNodeRunner> masterNode;
	private static ControllerNode controllerNode;
	
	public static final void setup(IMasterNodeRunner masterNodeRunner, IControllerNodeRunner controllerNodeRunner){
		masterNode = new MasterNode<>(masterNodeRunner);
		controllerNode = new ControllerNode(controllerNodeRunner);
	}
	
	public static final void launchAll(){
		startMasterNodeRunner();
		startControllerNodeRunner();
		startWebServer();
	}
	
	/**
	 * Starts the controller node runner in a new thread.
	 */
	public static final void startMasterNodeRunner(){
		try{
			executor.execute(masterNode);
		} catch (NullPointerException e){
			System.out.println("Couldn't start the master node - check it was set before being run");
		}
	}
	
	/**
	 * Starts the controller node runner in a new thread.
	 */
	public static final void startControllerNodeRunner(){
		try{
			executor.execute(controllerNode);
		} catch (NullPointerException e){
			System.out.println("Couldn't start the controller node - check it was set before being run");
		}
	}
	
	/**
	 * Starts a new webserver, which serves the web API
	 */
	public static void startWebServer(){
		executor.execute(new WebServer());
	}
	
	/**
	 * Getter for the controller node
	 */
	public static final ControllerNode getControllerNode(){
		return controllerNode;
	}
}
