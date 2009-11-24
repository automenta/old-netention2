package automenta.netention.gwtdepr.data;

import java.io.Serializable;
import java.util.Date;

public class DetailLinkData implements Serializable {

	private DetailData fromNode;	
	private DetailData toNode;
	private double strength;
	private Date whenCreated;
	
	public DetailLinkData() {
		super();
	}
	
	public DetailLinkData(DetailData fromNode,	DetailData toNode, double strength, Date whenCreated) {
		this();
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.strength = strength;
		this.whenCreated = whenCreated;
	}

	public DetailData getFromNode() {
		return fromNode;
	}

	public DetailData getToNode() {
		return toNode;
	}
	
	public double getStrength() {
		return strength;
	}
	public Date getWhenCreated() {
		return whenCreated;
	}
	
}
