package bramble.node.controller.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import bramble.networking.data.JobMetadata;
import bramble.node.controller.JobList;

public class JobListTest {
    
    private JobList jobList;
    private Collection<Integer> inputData;
    private JobMetadata jobOneMetadata;
    private JobMetadata jobTwoMetadata;

    /**
     * Runs before each test.
     */
    @Before
    public void before(){
	this.jobList = new JobList();
	this.inputData = new ArrayList<>();
	
	this.jobOneMetadata = new JobMetadata(1);
	this.jobTwoMetadata = new JobMetadata(2);
    }

    @Test
    public void test_that_a_new_job_list_has_no_jobs() {
	// Assert
	assertEquals(jobList.getTotalNumberOfJobs(), 0);
    }
    
    @Test
    public void test_that_a_single_job_can_be_added_to_the_job_list() {
	// Arrange
	inputData.add(jobOneMetadata.getJobNumber());
	
	// Act
	jobList.setUnstartedJobs(inputData);
	
	// Assert
	assertEquals(jobList.getTotalNumberOfJobs(), 1);
    }
    
    @Test
    public void test_that_multiple_different_jobs_can_be_added_to_the_job_list() {
	// Arrange
	inputData.add(jobOneMetadata.getJobNumber());
	inputData.add(jobTwoMetadata.getJobNumber());
	
	// Act
	jobList.setUnstartedJobs(inputData);
	
	// Assert
	assertEquals(jobList.getTotalNumberOfJobs(), 2);
    }
    
    @Test
    public void test_that_adding_two_of_the_same_job_only_adds_it_once() {
	// Arrange
	inputData.add(jobOneMetadata.getJobNumber());
	inputData.add(jobOneMetadata.getJobNumber());
	
	// Act
	jobList.setUnstartedJobs(inputData);
	
	// Assert
	assertEquals(jobList.getTotalNumberOfJobs(), 1);
    }
    
    @Test
    public void test_that_a_job_can_be_started() {
	// Arrange	
	inputData.add(jobOneMetadata.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobStarted(jobOneMetadata);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(1, jobList.getNumberOfJobsInProgress());
	assertEquals(0, jobList.getNumberOfCompletedJobs());
    }
    
    @Test
    public void test_that_a_job_can_be_started_and_then_completed() {
	// Arrange
	inputData.add(jobOneMetadata.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobStarted(jobOneMetadata);
	jobList.jobCompleted(jobOneMetadata);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(0, jobList.getNumberOfJobsInProgress());
	assertEquals(1, jobList.getNumberOfCompletedJobs());
    }
    
    @Test
    public void test_that_a_job_can_be_completed_before_being_started() {
	// Arrange
	inputData.add(jobOneMetadata.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobCompleted(jobOneMetadata);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(0, jobList.getNumberOfJobsInProgress());
	assertEquals(1, jobList.getNumberOfCompletedJobs());
    }
    
    @Test
    public void test_that_trying_to_complete_a_job_that_doesnt_exist_does_nothing() {
	// Arrange	
	inputData.add(jobOneMetadata.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobCompleted(jobTwoMetadata);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(0, jobList.getNumberOfJobsInProgress());
	assertEquals(0, jobList.getNumberOfCompletedJobs());
    }
    
    @Test
    public void test_that_a_job_can_be_started_and_then_cancelled() {
	// Arrange
	inputData.add(jobOneMetadata.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobStarted(jobOneMetadata);
	jobList.cancelJob(jobOneMetadata);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(0, jobList.getNumberOfJobsInProgress());
	assertEquals(0, jobList.getNumberOfCompletedJobs());
    }
    
    @Test
    public void test_that_cancelling_a_completed_job_does_nothing() {
	// Arrange
	inputData.add(jobOneMetadata.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobStarted(jobOneMetadata);
	jobList.jobCompleted(jobOneMetadata);
	jobList.cancelJob(jobOneMetadata);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(0, jobList.getNumberOfJobsInProgress());
	assertEquals(1, jobList.getNumberOfCompletedJobs());
    }

}

