package bramble.networking;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.nustaq.serialization.FSTObjectInput;

import bramble.webserver.WebAPI;

public class ListenerServer extends ServerSocket {
	
	public ListenerServer(int port) throws IOException, BindException {
		super(port);
	}
	
	synchronized public Message listen() {
		Socket socket = null;
		InputStream inputStream = null;
		FSTObjectInput objectInputStream = null;
		
		try {
			socket = this.accept();
			inputStream = socket.getInputStream();
			objectInputStream = new FSTObjectInput(inputStream);
		} catch (IOException e) {
			System.out.println("Couldn't create socket.");
			System.exit(1);
		}
		
		Message output = null;
		
		try {
			while(output == null){		
				output = (Message) objectInputStream.readObject();
			}
		} catch (ClassNotFoundException | IOException e) {
			WebAPI.publishMessage("Recieved a bad object");
		}
		
		try {
			objectInputStream.close();
			inputStream.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Couldn't close socket.");
			System.exit(1);
		}
		
		return output;
	}
}
