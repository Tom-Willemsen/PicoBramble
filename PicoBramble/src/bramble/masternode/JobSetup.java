package bramble.masternode;

import java.util.ArrayList;

import bramble.slavenode.SlaveNode;

public abstract class JobSetup {
	
	public abstract void sendJobSetupData();
	
	private static ArrayList<SlaveNode> slaveNodes = new ArrayList<SlaveNode>();
	private static MasterNode masterNode;
	
	
}
