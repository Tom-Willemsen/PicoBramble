package bramble.node.master.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.Mockito;

import bramble.exception.UnexpectedMessageException;
import bramble.networking.ListenerServer;
import bramble.networking.Message;
import bramble.node.master.IMasterNodeRunner;
import bramble.node.master.MasterNode;
import bramble.node.master.MessageParser;

public class MasterNodeTest {
	
	private MessageParser messageParser;
	private ListenerServer listenerServer;

	@Before
	public void before(){
		this.messageParser = Mockito.mock(MessageParser.class);
		this.listenerServer = Mockito.mock(ListenerServer.class);
	}

	@Test(timeout=5000)
	public void test_that_when_the_listener_server_gives_a_message_it_is_passed_to_the_message_parser() {
		// Arrange
		Message message = Mockito.mock(Message.class);
		Mockito.when(listenerServer.listen()).thenReturn(message);
		
		MasterNode<IMasterNodeRunner> masterNode = new MasterNode<>(listenerServer, messageParser);
		
		// Act
		masterNode.listen(listenerServer);
		
		// Assert
		while(Mockito.mockingDetails(messageParser).getInvocations().size() == 0){
			// Do nothing while the mock hasn't been interacted with.
			// The test will time out if the mock is never touched.
		}
		
		try {
			Mockito.verify(messageParser).parse(message);
		} catch (UnexpectedMessageException e) {
			Assert.fail();
		}
	}
}
