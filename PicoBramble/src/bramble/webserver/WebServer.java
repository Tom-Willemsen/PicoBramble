package bramble.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import bramble.configuration.BrambleConfiguration;
import bramble.webserver.AvailableJobSlotsHandler;

public class WebServer implements Runnable {
	
	private Server server;
	
	public WebServer(){
		server = new Server(BrambleConfiguration.WEBSERVER_PORT);
		addContextHandlers();
	}
	
	private void addContextHandlers(){
		ServletContextHandler handler = new ServletContextHandler(server, "/");		
		handler.addServlet(AvailableJobSlotsHandler.class, "/api/available_job_slots");
		handler.addServlet(TotalJobSlotsHandler.class, "/api/total_job_slots");
		handler.addServlet(TotalNodesHandler.class, "/api/total_nodes");
		
	}
	
	public void run() {
		try {
			server.start();
		} catch (Exception e) {
			System.out.println("Couldn't start jetty server");
		}
	}

}
