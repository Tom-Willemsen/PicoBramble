package bramble.networking;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenerServer extends ServerSocket {
	
	public ListenerServer(int port) throws IOException, BindException {
		super(port);
	}
	
	public Message listen() throws IOException, ClassNotFoundException {
		
		Socket socket = this.accept();
		
		InputStream inputStream = socket.getInputStream();
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
		
		return (Message) objectInputStream.readObject();
	}
}
