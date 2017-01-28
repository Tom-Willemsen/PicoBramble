package bramble.webserver;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

public class TotalJobSlotsHandler extends HttpServlet {

	private static final long serialVersionUID = 5414855470057516723L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.setStatus(HttpStatus.OK_200);
		resp.getWriter().println(WebAPI.getTotalJobSlots());
	}
}
