package primes.bramble;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import bramble.masternode.MasterNode;
import bramble.networking.JobResponseData;
import bramble.networking.JobSetupData;

public class MasterNodeRunner extends MasterNode {
	
	public static void main(String[] args){	
		new Thread(new MasterNodeRunner()).start();
		(new MasterNodeRunner()).listenForever();
	}

	public void sendJobData() {
		
		ArrayList<Serializable> init2 = new ArrayList<Serializable>();
		init2.add(new Long(10000000));
		init2.add(new Long(20000000));
		
		try {
			(new JobSetupData(1, init2)).send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<Serializable> init3 = new ArrayList<Serializable>();
		init3.add(new Long(20000000));
		init3.add(new Long(30000000));
		
		try {
			(new JobSetupData(2, init3)).send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void parse(JobResponseData jobResponseData) {
		System.out.println("Job [" + jobResponseData.getJobIdentifier() + "] replied with "
				+ (jobResponseData.getData()).size() + " primes.");
		
	}

}
