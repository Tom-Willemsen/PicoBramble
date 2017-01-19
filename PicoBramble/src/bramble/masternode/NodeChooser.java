package bramble.masternode;

import java.util.ArrayList;

import bramble.slavenode.SlaveNode;

public abstract class NodeChooser {
	
	private static ArrayList<SlaveNode> slaveNodes = new ArrayList<SlaveNode>();
	private static MasterNode masterNode;
	
	public static void setMasterNode(MasterNode node){
		masterNode = node;
	}
	
	public static MasterNode getMasterNode(){
		return masterNode;
	}
	
	public static void addSlaveNode(SlaveNode node){
		slaveNodes.add(node);
	}
	
	public static ArrayList<SlaveNode> getSlaveNodeArrayList(){
		return slaveNodes;
	}
	
	public static void clearSlaveNodeList(){
		slaveNodes.clear();
	}
}
