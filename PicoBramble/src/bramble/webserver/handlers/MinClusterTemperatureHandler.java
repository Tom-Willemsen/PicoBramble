package bramble.webserver.handlers;

import bramble.webserver.WebApi;

public class MinClusterTemperatureHandler extends BaseHandler {
	
	private static final long serialVersionUID = 5414855470057516723L;

	@Override
	protected String getData() {
		return WebApi.getMinClusterTemperature();
	}
}
