package automenta.netention.server;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import automenta.netention.server.value.Value;


/** analogous to an RDF resource */
public class Node implements Value {

	private String id;
	private String name;
	
	public Node() {
		super();
	}
	
	public Node(String id) {
		this();
		this.id = id;
		this.name = id;
	}
	
	public Node(String id, String name) {
		this(id);

		this.name = name;		
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}
	


	public void setName(String nextName) {
		this.name = nextName;		
	}


	@Override public String toString() {
		return getID() + " (" + getName() + ")";
	}

}
