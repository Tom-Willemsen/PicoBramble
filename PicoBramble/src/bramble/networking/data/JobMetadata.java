package bramble.networking.data;

import java.io.Serializable;

public class JobMetadata implements Serializable {
    
    private static final long serialVersionUID = 629462331377127553L;

    private static int nextUniqueIdentifier = 0;
    
    private Integer uniqueIdentifier;
    private final Integer jobNumber;
    
    public JobMetadata(int jobNumber){
	this.jobNumber = jobNumber;
	assignUniqueIdentifier();
    }
    
    private synchronized void assignUniqueIdentifier(){
	this.uniqueIdentifier = JobMetadata.nextUniqueIdentifier++;
    }
    
    public Integer getUniqueIdentifier(){
	return uniqueIdentifier;
    }
    
    public Integer getJobNumber(){
	return jobNumber;
    }

    @Override
    public int hashCode() {
	return jobNumber;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null || getClass() != obj.getClass()){
	    return false;
	}
	JobMetadata other = (JobMetadata) obj;
	if (!jobNumber.equals(other.jobNumber)){
	    return false;
	}
	return true;
    }
    

}
