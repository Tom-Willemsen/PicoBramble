package bramble.node.controller.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import bramble.networking.Handshake;
import bramble.node.controller.ControllerNode;
import bramble.node.controller.IControllerNodeRunner;
import bramble.node.controller.SlaveNodeInformation;

public class ControllerNodeTest {
	
	private IControllerNodeRunner runner;
	
	@Before
	public void before(){
		runner = Mockito.mock(IControllerNodeRunner.class);
		Mockito.when(runner.getAllJobNumbers()).thenReturn(new ArrayList<>());
	}

	@Test
	public void test_that_when_a_new_controller_node_is_created_it_has_no_jobs_if_the_node_runner_has_no_jobs() {
		
		// Act
		ControllerNode<IControllerNodeRunner> controllerNode = new ControllerNode<>(runner);
		
		// Assert
		assertEquals(controllerNode.getAllJobs().size(), 0);
		
	}
	
	@Test
	public void test_that_when_a_new_controller_node_is_created_it_has_some_jobs_if_the_node_runner_has_some_jobs() {
		
		// Arrange
		ArrayList<Integer> result = new ArrayList<>();
		for(int i=0; i<5; i++){
			result.add(i);
		}
		Mockito.when(runner.getAllJobNumbers()).thenReturn(result);
		
		// Act
		ControllerNode<IControllerNodeRunner> controllerNode = new ControllerNode<>(runner);
		
		// Assert
		assertEquals(controllerNode.getAllJobs().size(), 5);
		
	}
	
	@Test
	public void test_that_a_new_controller_node_initially_has_no_slave_nodes() {
		
		// Act
		ControllerNode<IControllerNodeRunner> controllerNode = new ControllerNode<>(runner);
		
		// Assert
		assertEquals(controllerNode.getSlaveNodes().size(), 0);
		
	}
	
	@Test
	public void test_that_a_slave_node_can_be_added_by_slavenodeinformation() {
		
		// Arrange	
		SlaveNodeInformation slaveNodeInformation = Mockito.mock(SlaveNodeInformation.class);
		ControllerNode<IControllerNodeRunner> controllerNode = new ControllerNode<>(runner);
		
		// Act
		controllerNode.registerSlaveNode(slaveNodeInformation);
		
		// Assert
		assertEquals(controllerNode.getSlaveNodes().size(), 1);
		
	}
	
	@Test
	public void test_that_a_slave_node_can_be_added_by_handshake() {
		
		// Arrange	
		Handshake handshake = Mockito.mock(Handshake.class);
		Mockito.when(handshake.getSenderIP()).thenReturn("111.111.111.111");
		ControllerNode<IControllerNodeRunner> controllerNode = new ControllerNode<>(runner);
		
		// Act
		controllerNode.registerSlaveNodeByHandshake(handshake);
		
		// Assert
		assertEquals(controllerNode.getSlaveNodes().size(), 1);
		
	}
	
	@Test
	public void test_that_two_slave_node_cant_be_added_by_handshake_if_they_have_the_same_ip() {
		
		// Arrange	
		Handshake handshake = Mockito.mock(Handshake.class);
		Mockito.when(handshake.getSenderIP()).thenReturn("111.111.111.111");
		
		ControllerNode<IControllerNodeRunner> controllerNode = new ControllerNode<>(runner);
		
		// Act
		controllerNode.registerSlaveNodeByHandshake(handshake);
		controllerNode.registerSlaveNodeByHandshake(handshake);
		
		// Assert
		assertEquals(controllerNode.getSlaveNodes().size(), 1);
		
	}
	
	@Test
	public void test_that_two_slave_node_cant_be_added_by_handshake_if_they_have_different_ips() {
		
		// Arrange	
		Handshake handshake = Mockito.mock(Handshake.class);
		Handshake handshake2 = Mockito.mock(Handshake.class);
		
		Mockito.when(handshake.getSenderIP()).thenReturn("111.111.111.111");
		Mockito.when(handshake2.getSenderIP()).thenReturn("222.222.222.222");
		
		ControllerNode<IControllerNodeRunner> controllerNode = new ControllerNode<>(runner);
		
		// Act
		controllerNode.registerSlaveNodeByHandshake(handshake);
		controllerNode.registerSlaveNodeByHandshake(handshake2);
		
		// Assert
		assertEquals(controllerNode.getSlaveNodes().size(), 2);
		
	}

}
