package bramble.node.controller;

import java.util.ArrayList;
import java.util.Collection;

public class JobList {

    private Collection<JobMetadata> unstartedJobs;
    private Collection<JobMetadata> startedJobs;
    private Collection<JobMetadata> completedJobs;

    /**
     * Initializes an empty job list.
     */
    public JobList(){
	this.unstartedJobs = new ArrayList<>();
	this.startedJobs = new ArrayList<>();
	this.completedJobs = new ArrayList<>();
    }

    /**
     * Sets all the jobs to be contained in this list.
     * @param allJobNumbers all of the job numbers
     */
    public synchronized void setUnstartedJobs(Collection<Integer> allJobNumbers) {
	for(Integer jobNumber : allJobNumbers){

	    JobMetadata job = new JobMetadata(jobNumber);

	    if(!isJobInAnyList(job)){
		unstartedJobs.add(job);
	    }
	}
    }

    private boolean isJobInAnyList(final JobMetadata job){
	if(unstartedJobs.contains(job)){
	    return false;
	}
	if(startedJobs.contains(job)){
	    return false;
	}
	if(completedJobs.contains(job)){
	    return false;
	}
	return true;
    }

    /**
     * Gets all the jobs that this controller node has.
     * @return all the jobs that this controller node has
     */
    public int getTotalNumberOfJobs(){
	return unstartedJobs.size() + startedJobs.size() + completedJobs.size();
    }

    /**
     * Gets all the jobs that have been completed.
     * @return all the jobs that have been completed
     */
    public int getNumberOfCompletedJobs(){
	return completedJobs.size();
    }

    /**
     * Gets all the jobs which have been started, including completed ones.
     * @return all the jobs which have been started, including completed ones
     */
    public int getNumberOfJobsInProgress(){
	return startedJobs.size();
    }

    /**
     * Gets whether all the jobs have been completed.
     * @return true if all jobs are complete, false otherwise
     */
    public boolean areAllJobsFinished(){
	if(unstartedJobs.size() == 0 && startedJobs.size() == 0){
	    return true;
	}
	return false;
    }

    /**
     * Gets the job identifier of the next job that should be run.
     * @return the job identifier of the next job that should be run
     */
    public synchronized JobMetadata getNextJob(){
	/*
	 *  Can't get a specific element from a collection as it may be implemented
	 *  in a non-indexed way. So we just iterate over it and return the first
	 *  element that we get instead.
	 */
	for(JobMetadata job : unstartedJobs){
	    return job;
	}
	return null;
    }

    /**
     * Cancels a job.
     * @param job the job to cancel
     */
    public synchronized void cancelJob(final JobMetadata job) {	
	startedJobs.remove(job);
	unstartedJobs.add(job);
    }

    /**
     * Marks a job as completed.
     * @param job the job that is complete
     */
    public synchronized void jobCompleted(final JobMetadata job) {
	startedJobs.remove(job);
	completedJobs.add(job);
    }

    /**
     * Marks a job as started.
     * @param job the job that has been started
     */
    public synchronized void jobStarted(final JobMetadata job) {
	unstartedJobs.remove(job);
	startedJobs.add(job);
    }

}
