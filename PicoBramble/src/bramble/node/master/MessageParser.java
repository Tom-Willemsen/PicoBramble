package bramble.node.master;

import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.Message;
import bramble.node.manager.IManager;

public class MessageParser {

    private final IManager manager;
    private final IMasterNodeRunner masterNodeRunner;

    /**
     * Constructor.
     * @param manager 
     * 		- the manager to send handshakes back to
     * @param masterNodeRunner 
     * 		- the master node runner to send job setup data to
     */
    public MessageParser(IManager manager, IMasterNodeRunner masterNodeRunner){
	this.manager = manager;
	this.masterNodeRunner = masterNodeRunner;
    }

    /**
     * Parses a generic message.
     * @param incomingData 
     * 		- the message to parse
     * @throws IllegalArgumentException 
     * 	   	- if the Message was neither a handshake nor job response data
     */
    public void parse(Message incomingData) throws IllegalArgumentException{
	if(incomingData instanceof JobResponseData){
	    manager.getControllerNode().jobFinished(((JobResponseData) incomingData));
	    masterNodeRunner.parseJobResponseData((JobResponseData) incomingData);
	} else if (incomingData instanceof Handshake){
	    parseHandshake((Handshake) incomingData);
	} else {
	    throw new IllegalArgumentException();
	}
    }

    /**
     * Parses a handshake.
     * @param handshake - the handshake to parse
     */
    private void parseHandshake(Handshake handshake){
	manager.getControllerNode().registerSlaveNodeByHandshake(handshake);
    }
}
