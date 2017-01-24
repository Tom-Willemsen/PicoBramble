package bramble.networking;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectOutput;

import bramble.configuration.BrambleConfiguration;

public abstract class Message implements Serializable {

	private static final long serialVersionUID = -7087359340979113438L;
	private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
	
	protected String targetHostname = BrambleConfiguration.MASTER_NODE_IP;
	protected int port;
	
	private static final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
	
	synchronized public void send() throws UnknownHostException, IOException{
		send(this.targetHostname, this.port);
	}
	
	/**
	 * Sends a Message to a node
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	synchronized private final void send(String targetHostname, int port) throws UnknownHostException, IOException{

		Socket socket = new Socket(targetHostname, port);
		
		byte barray[] = conf.asByteArray(this);
		
		FSTObjectOutput objectOutputStream = new FSTObjectOutput(socket.getOutputStream());
		
<<<<<<< Updated upstream
=======
		byte barray[] = conf.asByteArray(this);
		
>>>>>>> Stashed changes
		objectOutputStream.writeInt(barray.length);
		objectOutputStream.write(barray);
		
		objectOutputStream.flush();
		objectOutputStream.close();
		socket.close();
		
	}
	
	synchronized public final void setTargetHostname(String targetHostname){
		this.targetHostname = targetHostname;
	}

}
