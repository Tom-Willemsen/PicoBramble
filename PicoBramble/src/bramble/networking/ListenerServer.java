package bramble.networking;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;

public class ListenerServer extends ServerSocket {
	
<<<<<<< Updated upstream
	private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
=======
	private static final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
>>>>>>> Stashed changes
	
	public ListenerServer(int port) throws IOException, BindException {
		super(port);
	}
	
	synchronized public Message listen() throws Exception {
		
		Socket socket = this.accept();
		
		InputStream inputStream = socket.getInputStream();
		FSTObjectInput objectInputStream = new FSTObjectInput(inputStream);
		
<<<<<<< Updated upstream
=======
		// Message output = (Message) objectInputStream.readObject();
		
>>>>>>> Stashed changes
		int len = objectInputStream.readInt();
		byte buffer[] = new byte[len]; // this could be reused !
		while (len > 0)
		    len -= objectInputStream.read(buffer, buffer.length - len, len);
		
<<<<<<< Updated upstream
		// skipped: check for stream close
		
=======
>>>>>>> Stashed changes
		Message output = (Message) conf.getObjectInput(buffer).readObject();
		
		objectInputStream.close();
		inputStream.close();
		socket.close();
		return output;
	}
}
