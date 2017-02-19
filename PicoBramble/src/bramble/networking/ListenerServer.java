package bramble.networking;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.nustaq.serialization.FSTObjectInput;

public class ListenerServer extends ServerSocket {
	
    /**
     * Constructor.
     * @param port - the port to listen on
     * @throws IOException - if the listener server could not be set up
     */
    public ListenerServer(int port) throws IOException {
	super(port);
    }
	
    /**
     * Listens for a Message.
     * @return the message
     * @throws IOException if there was an error
     */
    public synchronized Message listen() throws IOException {
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
