package bramble.networking;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.nustaq.serialization.FSTObjectInput;

public class ListenerServer extends ServerSocket {
	
	public ListenerServer(int port) throws IOException {
		super(port);
	}
	
	synchronized public Message listen() throws IOException {
		Socket socket = null;
		InputStream inputStream = null;
		FSTObjectInput objectInputStream = null;
		
		socket = this.accept();
		inputStream = socket.getInputStream();
		objectInputStream = new FSTObjectInput(inputStream);
		
		Message output = null;
		
		try {
			while(output == null){		
				output = (Message) objectInputStream.readObject();
			}
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException();
		}
		
		objectInputStream.close();
		inputStream.close();
		socket.close();
		
		return output;
	}
}
