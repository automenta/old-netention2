package automenta.netention.api.value.node;

import automenta.netention.api.value.PropertyValue;

public class NodeEquals extends PropertyValue {

	private String id;

	public NodeEquals() {
		super();
	}

	public NodeEquals(String id) {
		super();
		this.id = id;
	}
	
	public String getNode() {
		return id;
	}
}
