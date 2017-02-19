package bramble.node.slave.test;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import bramble.networking.JobSetupData;
import bramble.networking.ListenerServer;
import bramble.node.slave.ISlaveNodeRunner;
import bramble.node.slave.KeepAliveRunner;
import bramble.node.slave.SlaveNode;

public class SlaveNodeTest {

    private KeepAliveRunner keepAliveRunner;
    private ISlaveNodeRunner slaveNodeRunner;
    private ListenerServer listenerServer;

    @Before
    public void before(){
	this.keepAliveRunner = Mockito.mock(KeepAliveRunner.class);
	this.slaveNodeRunner = Mockito.mock(ISlaveNodeRunner.class);
	this.listenerServer = Mockito.mock(ListenerServer.class);
    }

    @Test(timeout=5000)
    public void test_that_when_a_slave_node_is_started_it_starts_a_keep_alive_runner() 
	    throws IOException {
	// Arrange
	Mockito.when(listenerServer.listen()).thenReturn(null);
	SlaveNode slaveNode = new SlaveNode("", slaveNodeRunner, keepAliveRunner, listenerServer);

	// Act
	slaveNode.run();

	// Assert
	while(Mockito.mockingDetails(keepAliveRunner).getInvocations().size() == 0){
	    // Do nothing while the mock hasn't been interacted with.
	    // The test will time out if the mock is never touched.
	}

	Mockito.verify(keepAliveRunner).run();
    }

    @Test
    public void test_that_when_a_slave_node_is_given_job_setup_data_it_runs_it() 
	    throws IOException {
	// Arrange		
	JobSetupData jobSetupData = new JobSetupData(1, new ArrayList<>());
	Mockito.when(listenerServer.listen()).thenReturn(jobSetupData);

	SlaveNode slaveNode = new SlaveNode("", slaveNodeRunner, keepAliveRunner, listenerServer);

	// Act
	slaveNode.run();

	// Assert
	while(Mockito.mockingDetails(slaveNodeRunner).getInvocations().size() == 0){
	    // Do nothing while the mock hasn't been interacted with.
	    // The test will time out if the mock is never touched.
	}

	Mockito.verify(slaveNodeRunner, Mockito.atLeast(1)).runJob(1, new ArrayList<>());
    }
}
