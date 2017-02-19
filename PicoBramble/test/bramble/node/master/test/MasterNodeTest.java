package bramble.node.master.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import bramble.networking.ListenerServer;
import bramble.networking.Message;
import bramble.node.controller.ControllerNode;
import bramble.node.manager.IManager;
import bramble.node.master.MasterNode;
import bramble.node.master.MessageParser;

public class MasterNodeTest {

    private MessageParser messageParser;
    private ListenerServer listenerServer;

    private DummyManager manager;

    private static class DummyManager implements IManager{
	@Override
	public void execute(Runnable task) {
	    task.run();
	}
	@Override
	public ControllerNode getControllerNode() {
	    return null;
	}	
    }

    @Before
    public void before(){
	this.manager = new DummyManager();
	this.messageParser = Mockito.mock(MessageParser.class);
	this.listenerServer = Mockito.mock(ListenerServer.class);
    }

    @Test(timeout=5000)
    public void test_that_when_the_listener_server_gives_a_message_it_is_passed_to_the_message_parser() throws IOException {
	// Arrange
	Message message = Mockito.mock(Message.class);
	Mockito.when(listenerServer.listen()).thenReturn(message);

	MasterNode masterNode = new MasterNode(manager, listenerServer, messageParser);

	// Act
	masterNode.listen(listenerServer);

	// Assert		
	Mockito.verify(messageParser, Mockito.atLeast(1)).parse(message);

    }
}
