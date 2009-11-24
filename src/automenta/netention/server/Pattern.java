package automenta.netention.server;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import automenta.netention.server.value.Property;
import automenta.netention.server.value.Unit;
import automenta.netention.server.value.geo.GeoPointVar;
import automenta.netention.server.value.integer.IntegerVar;
import automenta.netention.server.value.node.NodeVar;
import automenta.netention.server.value.real.RealVar;
import automenta.netention.server.value.real.TimePointVar;
import automenta.netention.server.value.string.StringVar;
import automenta.netention.server.impl.DefaultSchema;

abstract public class Pattern extends Node {

	private List<String> ext = new LinkedList();
	private List<String> props = new LinkedList();
	private String desc;
	

	public Pattern() {
		super();
	}
	
	public Pattern(String id, String name) {
		super(id, name);
	}

	public Pattern(String id, String name, String... exts) {
		this(id, name);

		for (String s : exts)
			ext.add(s);
	}

	public String[] getExtends() {
		String[] s = new String[ext.size()];
		return ext.toArray(s);
	}


	public String getDescription() { return desc; }
	
	/** properties that have a domain equal to this pattern */ 
	public List<String> getDefinedProperties() {
		return props ;
	}
	
	
	public Pattern description(String desc) {
		this.desc = desc;
		return this;
	}
	
	
	abstract public DefaultSchema getSchema();

	protected void addProperty(Property p) {
		getSchema().addProperty(p);
		getDefinedProperties().add(p.getID());
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param patternURI - the pattern URI (restriction) to which the value that this variable selects must belong  
	 * @return
	 */
	public Pattern withNode(String id, String name, String patternRestriction) {
		addProperty(new NodeVar(id, name, patternRestriction));
		return this;
	}

	public Pattern withNode(String id, String name) {
		addProperty(new NodeVar(id, name, null));
		return this;
	}

	public Pattern withString(String id, String name) {
		addProperty(new StringVar(id, name));
		return this;
	}
	
	public Pattern withString(String id, String name, String description) {
		StringVar sv = new StringVar(id, name);
		sv.setDescription(description);
		addProperty(sv);
		return this;
	}

	public Pattern withRichString(String id, String name) {
		StringVar sv = new StringVar(id, name);
		sv.setRich(true);
		addProperty(sv);
		return this;
	}

	public Pattern withString(String id, String name, List<String> exampleValues) {
		addProperty(new StringVar(id, name, exampleValues));
		return this;
	}

	public Pattern withReal(String id, String name) {
		withReal(id, name, Unit.Number);
		return this;
	}

	
	public Pattern withReal(String id, String name, Unit unit) {
		addProperty(new RealVar(id, name, unit));
		return this;
	}

	public Pattern withInt(String id, String name) {
		addProperty(new IntegerVar(id, name));
		return this;
	}

	public Pattern withGeoPoint(String id, String name) {
		addProperty(new GeoPointVar(id, name));
		return this;
	}
	
	public Pattern withTimePoint(String id, String name) {
		addProperty(new TimePointVar(id, name));
		return this;
	}
	
	public Pattern extending(String... patternIDs) {
		for (String p : patternIDs)
			ext.add(p);
		return this;
	}

	@Override
	public String toString() {
		return "{ " + getID() + " (" + getName() + ") " + " <" + Arrays.asList(getExtends()) + "> [" + getInheritedProperties() + "] }";
	}

	abstract public Collection<String> getInheritedProperties();

	public void setDescription(String patternDesc) {
		this.desc = patternDesc;		
	}
	
}
