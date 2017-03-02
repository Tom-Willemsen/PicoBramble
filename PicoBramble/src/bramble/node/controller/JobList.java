package bramble.node.controller;

import java.util.ArrayList;
import java.util.Collection;

public class JobList {
    
    private Collection<Integer> allJobs;
    private Collection<Integer> completedJobs;
    private Collection<Integer> startedJobs;
    
    private int nextAvailableJobIdentifier = 0;
    
    public JobList(){
	this.completedJobs = new ArrayList<Integer>();
	this.startedJobs = new ArrayList<Integer>();
    }

    /**
     * Sets all the jobs to be contained in this list.
     * @param allJobNumbers all of the job numbers
     */
    public void setAllJobs(Collection<Integer> allJobNumbers) {
	this.allJobs = allJobNumbers;	
    }
    
    /**
     * Gets all the jobs that this controller node has.
     * @return all the jobs that this controller node has
     */
    public Collection<Integer> getAllJobs(){
	return allJobs;
    }

    /**
     * Gets all the jobs that have been completed.
     * @return all the jobs that have been completed
     */
    public Collection<Integer> getCompletedJobs(){
	return completedJobs;
    }

    /**
     * Gets all the jobs which have been started, including completed ones.
     * @return all the jobs which have been started, including completed ones
     */
    public Collection<Integer> getStartedJobs(){
	return startedJobs;
    }
    
    /**
     * Gets whether all the jobs have been completed.
     * @return true if all jobs are complete, false otherwise
     */
    public boolean areAllJobsFinished(){
	if(allJobs.size() == completedJobs.size()){
	    return true;
	}
	return false;
    }
    
    /**
     * Gets the job identifier of the next job that should be run.
     * @return the job identifier of the next job that should be run
     */
    public synchronized Integer getNextJobIdentifier(){
	for(Integer i : allJobs){
	    if(!startedJobs.contains(i)){
		return i;
	    }
	}
	return null;
    }

    /**
     * Cancels a job.
     * @param jobIdentifier the job to cancel
     */
    public synchronized void cancelJob(final Integer jobIdentifier) {
	startedJobs.remove(jobIdentifier);
    }

    /**
     * Marks a job as completed.
     * @param jobIdentifier the job that is complete
     */
    public void jobCompleted(final Integer jobIdentifier) {
	completedJobs.add(jobIdentifier);
    }

    /**
     * Marks a job as started.
     * @param jobIdentifier the job that has been started
     */
    public void jobStarted(final Integer jobIdentifier) {
	startedJobs.add(jobIdentifier);
	nextAvailableJobIdentifier++;
    }
    
    /**
     * Gets the next available job identifier.
     * @return the next available job identifier
     */
    public Integer getNextAvailableJobIdentifier(){
	return nextAvailableJobIdentifier;
    }
    
}
