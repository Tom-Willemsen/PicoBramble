package bramble.configuration;

public abstract class BrambleConfiguration {
	
	public static final int MASTER_PORT = 5800;
	public static final int SLAVE_PORT = 5801;
	public static final int WEBSERVER_PORT = 5802;
	public static final String MASTER_NODE_IP = "169.254.48.35";
	
	public static final int LISTENER_DELAY_MS = 3;
	public static final int HANDSHAKE_FREQUENCY_MS = 5000;
	public static final int NODE_TIMEOUT_MS = HANDSHAKE_FREQUENCY_MS * 3;
	
	public static final int THREADS_PER_NODE = 5;

}
