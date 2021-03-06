package bramble.node.manager;

import java.io.IOException;
import bramble.concurrency.BrambleThreadPool;
import bramble.node.controller.ControllerNode;
import bramble.node.controller.IControllerNodeRunner;
import bramble.node.master.IMasterNodeRunner;
import bramble.node.master.MasterNode;
import bramble.webserver.WebApiServer;
import bramble.webserver.WebServer;

public class Manager implements IManager {

    private final BrambleThreadPool threadPool;
    private final MasterNode masterNode;
    private final ControllerNode controllerNode;

    /**
     * Constructor.
     * 
     * @param masterNodeRunner 
     * 	- A user-defined implementation of IMasterNodeRunner
     * @param controllerNodeRunner
     * 	- A user-defined implementation of IControllerNodeRunner
     * @throws IOException
     * 	- If there was an error creating the master or controller nodes
     */
    public Manager(IMasterNodeRunner masterNodeRunner, 
	    IControllerNodeRunner controllerNodeRunner) throws IOException {

	threadPool = new BrambleThreadPool();
	masterNode = new MasterNode(this, masterNodeRunner);
	controllerNode = new ControllerNode(this, controllerNodeRunner);
    }

    /**
     * Starts the master node, controller node, web API, and webserver.
     */
    public void launchAll(){		
	startMasterNodeRunner();
	startControllerNodeRunner();
	startWebApiServer();
	startWebServer();
    }

    /**
     * Starts the controller node runner in a new thread.
     */
    private void startMasterNodeRunner(){
	threadPool.run(masterNode);
    }

    /**
     * Starts the controller node runner in a new thread.
     */
    private void startControllerNodeRunner(){
	threadPool.run(controllerNode);
    }

    /**
     * Starts a new webserver, which serves the web API.
     */
    private void startWebApiServer(){
	threadPool.run(new WebApiServer());
    }

    /**
     * Starts a new webserver, which serves the webpages.
     */
    private void startWebServer(){
	threadPool.run(new WebServer());
    }

    /**
     * Getter for the controller node.
     */
    public ControllerNode getControllerNode(){
	return controllerNode;
    }

    /**
     * Runs a task.
     */
    public void runTask(Runnable task){
	threadPool.run(task);
    }
}
