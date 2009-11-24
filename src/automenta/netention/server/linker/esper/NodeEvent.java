/**
 * 
 */
package automenta.netention.server.linker.esper;

import java.util.List;
import java.util.Map;

import automenta.netention.server.value.DefiniteValue;

public class NodeEvent {
	String nodeID;
	String creatorID;
	Map<String, Object> properties;
	List<String> definedPatterns;
	List<String> inheritedPatterns;
	
	public NodeEvent(String nodeID, String creatorID, Map<String, Object> properties, List<String> definedPatterns, List<String> inheritedPatterns) {
		super();
		this.nodeID = nodeID;
		this.creatorID = creatorID;
		this.properties = properties;
		this.definedPatterns = definedPatterns;
		this.inheritedPatterns = inheritedPatterns;
		
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public String getNodeID() {
		return nodeID;
	}
	
	public String getCreator() {
		return creatorID;
	}
	
	public List<String> getDefinedPatterns() {
		return definedPatterns;
	}
	
	public List<String> getInheritedPatterns() {
		return inheritedPatterns;
	}
	
	@Override public String toString() {
		return getNodeID() + " " + getCreator() + " " + getDefinedPatterns() + " " + getProperties();
	}
	
}