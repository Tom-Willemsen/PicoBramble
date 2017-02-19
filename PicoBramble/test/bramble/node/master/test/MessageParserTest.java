package bramble.node.master.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import bramble.networking.Handshake;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;
import bramble.node.controller.ControllerNode;
import bramble.node.manager.Manager;
import bramble.node.master.IMasterNodeRunner;
import bramble.node.master.MessageParser;

public class MessageParserTest {

    private IMasterNodeRunner runner;
    private Manager manager;
    private ControllerNode controllerNode;

    @Before
    public void before(){
	this.runner = Mockito.mock(IMasterNodeRunner.class);
	this.manager = Mockito.mock(Manager.class);	
	this.controllerNode = Mockito.mock(ControllerNode.class);

	Mockito.doNothing().when(controllerNode).registerSlaveNodeByHandshake(ArgumentMatchers.any(Handshake.class));
	Mockito.when(manager.getControllerNode()).thenReturn(controllerNode);
    }

    @Test
    public void test_that_when_a_handshake_is_parsed_it_is_sent_to_the_controller_node() {
	// Arrange
	Handshake handshake = Mockito.mock(Handshake.class);	
	MessageParser messageParser = new MessageParser(manager, runner);

	// Act
	messageParser.parse(handshake);

	// Assert
	Mockito.verify(controllerNode).registerSlaveNodeByHandshake(handshake);

    }

    @Test
    public void test_that_when_job_response_data_is_parsed_it_is_sent_to_the_master_node_runner() {
	// Arrange
	JobResponseData jobResponseData = Mockito.mock(JobResponseData.class);		
	MessageParser messageParser = new MessageParser(manager, runner);

	// Act
	messageParser.parse(jobResponseData);

	// Assert
	Mockito.verify(runner).parseJobResponseData(jobResponseData);

    }

    @Test
    public void test_that_when_job_setup_data_is_parsed_it_throws_an_exception() {
	// Arrange
	JobSetupData jobSetupData = Mockito.mock(JobSetupData.class);		
	MessageParser messageParser = new MessageParser(manager, runner);

	// Act
	try{
	    messageParser.parse(jobSetupData);
	    Assert.fail("Didn't throw an IllegalArgumentException");
	} catch (IllegalArgumentException e){
	    return;
	}
    }

}
