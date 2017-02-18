package bramble.webserver.handlers;

import bramble.webserver.WebAPI;

public class LogMessagesHandler extends BaseHandler {

	private static final long serialVersionUID = 5414855470057516723L;

	@Override
	protected String getData() {
		return WebAPI.getLogMessages();
	}
}
