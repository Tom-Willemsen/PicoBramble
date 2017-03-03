package bramble.node.controller.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import bramble.networking.Handshake;
import bramble.node.controller.SlaveNodeList;

public class SlaveNodeListTest {
    
    private SlaveNodeList slaveNodes;
    
    private static final String IP_ADDRESS_1 = "123";
    private static final String IP_ADDRESS_2 = "321";

    private Handshake handshakeOne;
    private Handshake handshakeTwo;
    
    /**
     * Runs before each test.
     */
    @Before
    public void before(){
	this.slaveNodes = new SlaveNodeList();
	
	handshakeOne = Mockito.mock(Handshake.class);
	handshakeTwo = Mockito.mock(Handshake.class);
	
	Mockito.when(handshakeOne.getSenderIpAddress()).thenReturn(IP_ADDRESS_1);
	Mockito.when(handshakeTwo.getSenderIpAddress()).thenReturn(IP_ADDRESS_2);
    }

    @Test
    public void test_that_a_new_slave_node_list_has_no_nodes() {
	// Assert
	assertEquals(0, slaveNodes.getSlaveNodes().size());
    }
    
    @Test
    public void test_that_a_new_slave_node_can_be_added_to_the_list() {
	// Act
	slaveNodes.registerSlaveNodeHandshake(handshakeOne);
	
	// Assert
	assertEquals(1, slaveNodes.getSlaveNodes().size());
    }
    
    @Test
    public void test_that_two_different_slave_nodes_can_be_added_to_the_list() {
	// Act
	slaveNodes.registerSlaveNodeHandshake(handshakeOne);
	slaveNodes.registerSlaveNodeHandshake(handshakeTwo);
	
	// Assert
	assertEquals(2, slaveNodes.getSlaveNodes().size());
    }
    
    @Test
    public void test_that_two_of_the_same_slave_nodes_cant_be_added_to_the_list() {
	// Act
	slaveNodes.registerSlaveNodeHandshake(handshakeOne);
	slaveNodes.registerSlaveNodeHandshake(handshakeOne);
	
	// Assert
	assertEquals(1, slaveNodes.getSlaveNodes().size());
    }

}


