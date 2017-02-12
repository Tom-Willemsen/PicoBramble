package bramble.node.controller.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mockito;

import bramble.node.controller.ControllerNode;
import bramble.node.controller.IControllerNodeRunner;

public class ControllerNodeTest {

	@Test
	public void given_a_controller_node_runner_with_no_jobs_when_a_new_controller_node_is_created_it_has_no_jobs() {
		
		// Arrange
		IControllerNodeRunner runner = Mockito.mock(IControllerNodeRunner.class);
		Mockito.when(runner.getAllJobNumbers()).thenReturn(new ArrayList<>());
		
		// Act
		ControllerNode controllerNode = new ControllerNode(runner);
		
		// Assert
		assertEquals(controllerNode.getAllJobs().size(), 0);
		
	}
	
	@Test
	public void given_a_controller_node_runner_with_some_jobs_when_a_new_controller_node_is_created_it_populates_the_job_list() {
		
		// Arrange
		IControllerNodeRunner runner = Mockito.mock(IControllerNodeRunner.class);
		ArrayList<Integer> result = new ArrayList<>();
		for(int i=0; i<5; i++){
			result.add(i);
		}
		Mockito.when(runner.getAllJobNumbers()).thenReturn(result);
		
		// Act
		ControllerNode controllerNode = new ControllerNode(runner);
		
		// Assert
		assertEquals(controllerNode.getAllJobs().size(), 5);
		
	}

}
