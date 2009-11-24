package automenta.netention.gwtdepr.data;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class AgentData /* extends NodeData */ implements Serializable {

	private String id;
	private String name;
	private LinkedHashMap<String, String> nodes;

	public AgentData() {
		super();
	}
	
	public AgentData(String id, String name, LinkedHashMap<String,String> nodeIDtoName) {
		this();
		this.id = id;
		this.name = name;
		this.nodes = nodeIDtoName;
	}
	
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public LinkedHashMap<String, String> getNodes() {
		return nodes;
	}
	
	
}
