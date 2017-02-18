package bramble.webserver.handlers;

import bramble.webserver.WebApi;

public class TotalJobsHandler extends BaseHandler {

	private static final long serialVersionUID = 5414855470057516723L;

	@Override
	protected String getData() {
		return Integer.toString(WebApi.getTotalJobsCount());
	}
}
