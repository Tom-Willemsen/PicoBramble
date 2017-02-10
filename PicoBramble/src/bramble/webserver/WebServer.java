package bramble.webserver;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import bramble.configuration.BrambleConfiguration;

public class WebServer implements Runnable {
	
	private Server server;
	
	public WebServer(){
		this.server = new Server();
	    ServerConnector connector = new ServerConnector(server);
	    connector.setPort(BrambleConfiguration.WEB_SERVER_PORT);
	    server.addConnector(connector);

	    ResourceHandler resourceHandler = new ResourceHandler();
	    resourceHandler.setDirectoriesListed(true);

	    resourceHandler.setResourceBase(BrambleConfiguration.WEB_SERVER_FILE_LOCATION);

	    HandlerList handlers = new HandlerList();
	    handlers.setHandlers(new Handler[] { resourceHandler });
	    server.setHandler(handlers);
	}

	@Override
	public void run() {
		try {
			server.start();
		} catch (Exception e) {
			System.out.println("Couldn't start jetty server");
		}
	}

}
