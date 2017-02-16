package bramble.node.slave;

import java.io.IOException;

import bramble.configuration.BrambleConfiguration;
import bramble.networking.Handshake;

public class KeepAliveRunner implements Runnable {
	
	private final String ipAddress;

	public KeepAliveRunner(String ipAddress){
		this.ipAddress = ipAddress;
	}
	
	@Override
	public void run(){
		while(true){
			try {
				(new Handshake(ipAddress)).send();
			} catch (IOException e) {
				return;
			}
			try {
				Thread.sleep(BrambleConfiguration.HANDSHAKE_FREQUENCY_MS);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
