package bramble.slavenode;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import bramble.genericnode.GenericNode;
import bramble.networking.JobResponseData;

public abstract class SlaveNode extends GenericNode {
	
	public SlaveNode() throws UnknownHostException {
		super();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static void sendData(int jobIdentifier, String message, ArrayList<? extends Object> data){
		try{
			(new JobResponseData(jobIdentifier, message, data)).send();
		} catch (IOException e) {
			System.out.println("Couldn't connect to master node. Aborting.");
			e.printStackTrace();
			System.exit(1);
		}
	}

}
