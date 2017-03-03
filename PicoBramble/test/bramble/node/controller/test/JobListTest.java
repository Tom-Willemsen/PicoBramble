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

    /**
     * Runs before each test.
     */
    @Before
    public void before(){
	this.jobList = new JobList();
	this.inputData = new ArrayList<>();
    }

    @Test
    public void test_that_a_new_job_list_has_no_jobs() {
	// Assert
	assertEquals(jobList.getTotalNumberOfJobs(), 0);
    }
    
    @Test
    public void test_that_a_single_job_can_be_added_to_the_job_list() {
	// Arrange
	inputData.add(1);
	
	// Act
	jobList.setUnstartedJobs(inputData);
	
	// Assert
	assertEquals(jobList.getTotalNumberOfJobs(), 1);
    }
    
    @Test
    public void test_that_multiple_different_jobs_can_be_added_to_the_job_list() {
	// Arrange
	inputData.add(1);
	inputData.add(2);
	
	// Act
	jobList.setUnstartedJobs(inputData);
	
	// Assert
	assertEquals(jobList.getTotalNumberOfJobs(), 2);
    }
    
    @Test
    public void test_that_adding_two_of_the_same_job_only_adds_it_once() {
	// Arrange
	inputData.add(1);
	inputData.add(1);
	
	// Act
	jobList.setUnstartedJobs(inputData);
	
	// Assert
	assertEquals(jobList.getTotalNumberOfJobs(), 1);
    }
    
    @Test
    public void test_that_a_job_can_be_started() {
	// Arrange
	JobMetadata metaData = new JobMetadata(1);
	
	inputData.add(metaData.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobStarted(metaData);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(1, jobList.getNumberOfJobsInProgress());
	assertEquals(0, jobList.getNumberOfCompletedJobs());
    }
    
    @Test
    public void test_that_a_job_can_be_started_and_then_completed() {
	// Arrange
	JobMetadata metaData = new JobMetadata(1);
	
	inputData.add(metaData.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobStarted(metaData);
	jobList.jobCompleted(metaData);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(0, jobList.getNumberOfJobsInProgress());
	assertEquals(1, jobList.getNumberOfCompletedJobs());
    }
    
    @Test
    public void test_that_a_job_can_be_completed_before_being_started() {
	// Arrange
	JobMetadata metaData = new JobMetadata(1);
	
	inputData.add(metaData.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobCompleted(metaData);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(0, jobList.getNumberOfJobsInProgress());
	assertEquals(1, jobList.getNumberOfCompletedJobs());
    }
    
    @Test
    public void test_that_trying_to_complete_a_job_that_doesnt_exist_does_nothing() {
	// Arrange	
	inputData.add(1);
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobCompleted(new JobMetadata(2));
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(0, jobList.getNumberOfJobsInProgress());
	assertEquals(0, jobList.getNumberOfCompletedJobs());
    }
    
    @Test
    public void test_that_a_job_can_be_started_and_then_cancelled() {
	// Arrange
	JobMetadata metaData = new JobMetadata(1);
	
	inputData.add(metaData.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobStarted(metaData);
	jobList.cancelJob(metaData);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(0, jobList.getNumberOfJobsInProgress());
	assertEquals(0, jobList.getNumberOfCompletedJobs());
    }
    
    @Test
    public void test_that_cancelling_a_completed_job_does_nothing() {
	// Arrange
	JobMetadata metaData = new JobMetadata(1);
	
	inputData.add(metaData.getJobNumber());
	jobList.setUnstartedJobs(inputData);
	
	// Act
	jobList.jobStarted(metaData);
	jobList.jobCompleted(metaData);
	jobList.cancelJob(metaData);
	
	// Assert
	assertEquals(1, jobList.getTotalNumberOfJobs());
	assertEquals(0, jobList.getNumberOfJobsInProgress());
	assertEquals(1, jobList.getNumberOfCompletedJobs());
    }

}

