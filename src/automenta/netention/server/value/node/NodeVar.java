package automenta.netention.server.value.node;

import automenta.netention.server.value.Property;
import automenta.netention.server.value.PropertyValue;

public class NodeVar extends Property {

	private String pattern;

	public NodeVar() {
		super();
	}
	
	public NodeVar(String id, String name, String pattern) {
		super(id, name);
		
		this.pattern = pattern;		
	}

	public String getPattern() {
		return pattern;
	}
	
	@Override
	public String toString() {
		return "(" + getID() + ": " + getName() + "<" + getPattern() + ">)";
	}
	
	
	@Override
	public PropertyValue newDefaultValue() {
		NodeIs ni = new NodeIs("");
		ni.setProperty(getID());
		return ni;
	}
	
}
