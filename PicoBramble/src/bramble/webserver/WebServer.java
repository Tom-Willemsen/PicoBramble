package bramble.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import bramble.configuration.BrambleConfiguration;
import bramble.webserver.handlers.AvailableJobSlotsHandler;
import bramble.webserver.handlers.AvgClusterTemperatureHandler;
import bramble.webserver.handlers.CompletedJobsHandler;
import bramble.webserver.handlers.JobsInProgressHandler;
import bramble.webserver.handlers.MaxClusterTemperatureHandler;
import bramble.webserver.handlers.TotalJobSlotsHandler;
import bramble.webserver.handlers.TotalJobsHandler;
import bramble.webserver.handlers.TotalNodesHandler;

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
		handler.addServlet(CompletedJobsHandler.class, "/api/completed_jobs");
		handler.addServlet(JobsInProgressHandler.class, "/api/jobs_in_progress");
		handler.addServlet(TotalJobsHandler.class, "/api/total_jobs");
		handler.addServlet(MaxClusterTemperatureHandler.class, "/api/max_temperature");
		handler.addServlet(AvgClusterTemperatureHandler.class, "/api/avg_temperature");
		
	}
	
	public void run() {
		try {
			server.start();
		} catch (Exception e) {
			System.out.println("Couldn't start jetty server");
		}
	}

}
