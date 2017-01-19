package bramble.masternode;

import java.util.ArrayList;

import bramble.slavenode.SlaveNode;

public class NodeChooser {
	
	private ArrayList<SlaveNode> slaveNodes = new ArrayList<SlaveNode>();
	private MasterNode masterNode;
	
	public void setMasterNode(MasterNode node){
		masterNode = node;
	}
	
	public MasterNode getMasterNode(){
		return masterNode;
	}
	
	public void addSlaveNode(SlaveNode node){
		slaveNodes.add(node);
	}
	
	public ArrayList<SlaveNode> getSlaveNodeArrayList(){
		return slaveNodes;
	}
	
	public void clearSlaveNodeList(){
		slaveNodes.clear();
	}
}
