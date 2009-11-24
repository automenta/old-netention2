package automenta.netention.gwtdepr.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

public class NodeData implements Serializable {

	public static interface NodeHandler {
		public void onNameChanged(String previousName, String nextName);
		//public void onPropertiesChanged();
		//public void onPatternsChanged();
	}
	

	transient private Collection<NodeHandler> handlers = new HashSet<NodeHandler>();

	private String id;
	private String name;

	public NodeData(String id, String name) {
		this();
		this.id = id;
		this.name = name;
	}

	public NodeData() {
		super();
	}

	public String getID() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String nextName) {
		String oldName = this.name;
		this.name = nextName;
		for (NodeHandler nh : getHandlers()) {
			nh.onNameChanged(oldName, nextName);
		}
		
	}

	private Collection<NodeHandler> getHandlers() {
		return handlers ;
	}

	public void addHandler(NodeHandler nh) {
		getHandlers().add(nh);
	}
	public void removeHandler(NodeHandler nh) {
		getHandlers().remove(nh);
	}

}
