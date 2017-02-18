package bramble.webserver.handlers;

import bramble.webserver.WebApi;

public class BrambleNameHandler extends BaseHandler {

	private static final long serialVersionUID = -6211320982595718268L;

	@Override
	protected String getData() {
		return WebApi.getName();
	}

}
