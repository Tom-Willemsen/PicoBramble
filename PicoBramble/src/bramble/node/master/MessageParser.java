package bramble.node.master;

import bramble.exception.UnexpectedMessageException;
import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.Message;
import bramble.node.manager.Manager;
import bramble.webserver.WebAPI;

public class MessageParser {
	
	private final Manager manager;
	private final IMasterNodeRunner masterNodeRunner;

	public MessageParser(Manager manager, IMasterNodeRunner masterNodeRunner){
		this.manager = manager;
		this.masterNodeRunner = masterNodeRunner;
	}
	
	/**
	 * Parses a generic message.
	 * @param incomingData the message to parse
	 * @throws UnexpectedMessageException 
	 */
	public void parse(Message incomingData) throws UnexpectedMessageException{
		if(incomingData instanceof JobResponseData){
			manager.getControllerNode().jobFinished(((JobResponseData) incomingData));
			masterNodeRunner.parseJobResponseData((JobResponseData) incomingData);
		} else if (incomingData instanceof Handshake){
			parseHandshake((Handshake) incomingData);
		} else {
			throw new UnexpectedMessageException();
		}
	}
	
	/**
	 * Parses a handshake.
	 * @param handshake the handshake to parse
	 */
	private void parseHandshake(Handshake handshake){
		manager.getControllerNode().registerSlaveNodeByHandshake(handshake);
	}
}
