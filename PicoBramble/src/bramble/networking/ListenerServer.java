package bramble.networking;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import bramble.configuration.BrambleConfiguration;

public class ListenerServer extends ServerSocket {
	
	public ListenerServer() throws IOException {
		super(BrambleConfiguration.PORT);
	}
	
	public Object listen() throws IOException, ClassNotFoundException {
		
		Socket socket = this.accept();
		
		InputStream inputStream = socket.getInputStream();
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
		
		return objectInputStream.readObject();
	}
}