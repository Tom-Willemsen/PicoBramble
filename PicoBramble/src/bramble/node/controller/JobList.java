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
    
    public boolean areAllJobsFinished(){
	
	if(allJobs.size() != completedJobs.size()){
	    return false;
	}

	return true;
    }
    
    public synchronized Integer getNextJob(){
	for(Integer i : allJobs){
	    if(!startedJobs.contains(i)){
		return i;
	    }
	}
	return null;
    }

    public synchronized void cancelJob(final Integer jobIdentifier) {
	startedJobs.remove(jobIdentifier);
    }

    public void jobCompleted(final Integer jobIdentifier) {
	completedJobs.add(jobIdentifier);
    }

    public void jobStarted(final Integer jobIdentifier) {
	startedJobs.add(jobIdentifier);
	nextAvailableJobIdentifier++;
    }
    
    public Integer getNextAvailableJobIdentifier(){
	return nextAvailableJobIdentifier;
    }
    
}
