package bramble.node.controller.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import bramble.networking.Handshake;
import bramble.networking.data.JobMetadata;
import bramble.node.controller.ControllerNode;
import bramble.node.controller.IControllerNodeRunner;
import bramble.node.manager.IManager;

public class ControllerNodeTest {

    private static final String IP_ADDRESS_1 = "123";
    private static final String IP_ADDRESS_2 = "321";

    private IControllerNodeRunner runner;
    private IManager manager;

    private static class DummyManager implements IManager{
	@Override
	public void runTask(Runnable task) {
	    task.run();
	}
	
	@Override
	public ControllerNode getControllerNode() {
	    return null;
	}	
    }

    /**
     * Runs before each test.
     */
    @Before
    public void before(){
	this.manager = new DummyManager();
	this.runner = Mockito.mock(IControllerNodeRunner.class);
	Mockito.when(runner.getAllJobNumbers()).thenReturn(new ArrayList<>());
    }

    @Test
    public void test_that_a_new_controller_node_has_no_jobs_if_the_node_runner_has_no_jobs() {

	// Act
	ControllerNode controllerNode = new ControllerNode(manager, runner);

	// Assert
	assertEquals(controllerNode.getTotalNumberOfJobs(), 0);

    }

    @Test
    public void test_that_a_new_controller_node_gets_jobs_from_the_node_runner() {

	// Arrange
	ArrayList<JobMetadata> result = new ArrayList<>();
	for(int i=0; i<5; i++){
	    result.add(new JobMetadata(i));
	}
	Mockito.when(runner.getAllJobNumbers()).thenReturn(result);

	// Act
	ControllerNode controllerNode = new ControllerNode(manager, runner);

	// Assert
	assertEquals(controllerNode.getTotalNumberOfJobs(), 5);

    }

    @Test
    public void test_that_a_new_controller_node_initially_has_no_slave_nodes() {

	// Act
	ControllerNode controllerNode = new ControllerNode(manager, runner);

	// Assert
	assertEquals(controllerNode.getSlaveNodes().size(), 0);

    }

    @Test
    public void test_that_a_slave_node_can_be_added_by_handshake() {

	// Arrange	
	Handshake handshake = Mockito.mock(Handshake.class);
	Mockito.when(handshake.getSenderIpAddress()).thenReturn(IP_ADDRESS_1);
	ControllerNode controllerNode = new ControllerNode(manager, runner);

	// Act
	controllerNode.registerSlaveNodeByHandshake(handshake);

	// Assert
	assertEquals(controllerNode.getSlaveNodes().size(), 1);

    }

    @Test
    public void test_that_two_slave_node_cant_be_added_by_handshake_if_they_have_the_same_ip() {

	// Arrange	
	Handshake handshake = Mockito.mock(Handshake.class);
	Mockito.when(handshake.getSenderIpAddress()).thenReturn(IP_ADDRESS_1);

	ControllerNode controllerNode = new ControllerNode(manager, runner);

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

	Mockito.when(handshake.getSenderIpAddress()).thenReturn(IP_ADDRESS_1);
	Mockito.when(handshake2.getSenderIpAddress()).thenReturn(IP_ADDRESS_2);

	ControllerNode controllerNode = new ControllerNode(manager, runner);

	// Act
	controllerNode.registerSlaveNodeByHandshake(handshake);
	controllerNode.registerSlaveNodeByHandshake(handshake2);

	// Assert
	assertEquals(controllerNode.getSlaveNodes().size(), 2);

    }

}
