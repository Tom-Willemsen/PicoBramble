package bramble.networking;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.nustaq.serialization.FSTObjectInput;

public class ListenerServer extends ServerSocket {
	
	public ListenerServer(int port) throws IOException, BindException {
		super(port);
	}
	
	synchronized public Message listen() throws Exception {
		
		Socket socket = this.accept();
		
		InputStream inputStream = socket.getInputStream();
		FSTObjectInput objectInputStream = new FSTObjectInput(inputStream);
		
		Message output = null;
		
		while(output == null){
			output = (Message) objectInputStream.readObject();
		}
		
		objectInputStream.close();
		inputStream.close();
		socket.close();
		return output;
	}
}
