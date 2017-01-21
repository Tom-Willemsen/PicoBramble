package primes.bramble;

import bramble.masternode.MasterNode;
import bramble.networking.JobResponseData;

public class MasterNodeRunner extends MasterNode {
	
	public static void main(String[] args){	
		new Thread(new MasterNodeRunner()).start();
		(new MasterNodeRunner()).listenForever();
	}
	
	@Override
	protected void parse(JobResponseData jobResponseData) {
		System.out.println("Job [" + jobResponseData.getJobIdentifier() + "] replied with "
				+ (jobResponseData.getData()).size() + " primes.");
		
	}

}
