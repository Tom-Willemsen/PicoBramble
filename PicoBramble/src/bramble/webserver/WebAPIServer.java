package bramble.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import bramble.configuration.BrambleConfiguration;
import bramble.webserver.handlers.AvailableJobSlotsHandler;
import bramble.webserver.handlers.AvgClusterTemperatureHandler;
import bramble.webserver.handlers.AvgCpuSpeedHandler;
import bramble.webserver.handlers.BrambleNameHandler;
import bramble.webserver.handlers.CompletedJobsHandler;
import bramble.webserver.handlers.JobsInProgressHandler;
import bramble.webserver.handlers.LogMessagesHandler;
import bramble.webserver.handlers.MaxClusterTemperatureHandler;
import bramble.webserver.handlers.MaxCpuSpeedHandler;
import bramble.webserver.handlers.MinClusterTemperatureHandler;
import bramble.webserver.handlers.MinCpuSpeedHandler;
import bramble.webserver.handlers.TotalJobSlotsHandler;
import bramble.webserver.handlers.TotalJobsHandler;
import bramble.webserver.handlers.TotalNodesHandler;

public class WebAPIServer implements Runnable {
	
	private Server server;
	
	public WebAPIServer(){
		server = new Server(BrambleConfiguration.WEB_API_SERVER_PORT);
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
		handler.addServlet(MinClusterTemperatureHandler.class, "/api/min_temperature");
		handler.addServlet(MaxCpuSpeedHandler.class, "/api/max_cpu_speed");
		handler.addServlet(AvgCpuSpeedHandler.class, "/api/avg_cpu_speed");
		handler.addServlet(MinCpuSpeedHandler.class, "/api/min_cpu_speed");
		handler.addServlet(LogMessagesHandler.class, "/api/log_messages");
		handler.addServlet(BrambleNameHandler.class, "/api/name");
		
	}
	
	@Override
	public void run() {
		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
