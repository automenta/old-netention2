package automenta.netention.server;

import java.util.Map;


// -> implement Node subclass, with Agents as the sub-nodes? */
/** an organization of agents and their states that can be associated or matched */ 
public interface Network {

	
	public Map<String, Agent> getAgents();
	
	public Map<String, Node> getNodes();
	void addNode(Node n);
	void removeNode(String nodeID);
	
	public Schema getSchema();

	
}
