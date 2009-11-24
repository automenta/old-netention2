package automenta.netention.server.linker;

import java.util.Date;

import automenta.netention.server.Message;
import automenta.netention.server.NodeToNode;

public class DetailLink implements NodeToNode, Message {

	private String fromNode;
	private String toNode;
	private Date created;
	private double strength = 1.0;

	public static class LinkReason {
		private String propertyInvolved;

	}

	public DetailLink() {
		super();
	}

	public DetailLink(String fromNode, String toNode) {
		this();
		this.created = new Date();
		this.fromNode = fromNode;
		this.toNode = toNode;
	}

	public DetailLink(String fromNode, String toNode, double strength) {
		this(fromNode, toNode);
		this.strength = strength;
	}

	@Override public int hashCode() {
		return getFromNode().hashCode() + getToNode().hashCode();		
	}

	@Override public boolean equals(Object obj) {
		//ignores whenCreated timestamp
		if (obj instanceof DetailLink) {
			DetailLink w = (DetailLink)obj;
			if (w.getFromNode().equals(getFromNode())) 
				if (w.getToNode().equals(getToNode())) { 
					return true;
				}
		}
		return false;
	}


	@Override public Date getWhenCreated() {
		return created;
	}

	public String getFromNode() {
		return fromNode;
	}

	public String getToNode() {
		return toNode;
	}

	public double getStrength() {
		return strength ;
	}

}
