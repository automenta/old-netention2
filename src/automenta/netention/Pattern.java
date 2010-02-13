package automenta.netention;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import automenta.netention.value.Property;
import automenta.netention.value.Unit;
import automenta.netention.value.geo.GeoPointVar;
import automenta.netention.value.integer.IntegerVar;
import automenta.netention.value.node.NodeVar;
import automenta.netention.value.real.RealVar;
import automenta.netention.value.real.TimePointVar;
import automenta.netention.value.string.StringVar;

/** analgous to "part" of a RDF/Java "Class" */
public class Pattern extends Node {

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
	@Deprecated public List<String> getDefinedProperties() {
		return props ;
	}
	
	
	public Pattern description(String desc) {
		this.desc = desc;
		return this;
	}
    
	//abstract public Schema getSchema();

	protected Property addProperty(Schema s, Property p) {
		s.addProperty(p);
		getDefinedProperties().add(p.getID());
        return p;
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param patternURI - the pattern URI (restriction) to which the value that this variable selects must belong  
	 * @return
	 */
	public Property withNode(Schema s, String id, String name, String patternRestriction) {
		return addProperty(s, new NodeVar(id, name, patternRestriction));
	}

	public Property withNode(Schema s, String id, String name) {
		return addProperty(s, new NodeVar(id, name, null));
	}

	public Property withString(Schema s, String id, String name) {
		return addProperty(s, new StringVar(id, name));
	}
	
	public Property withString(Schema s, String id, String name, String description) {
		StringVar sv = new StringVar(id, name);
		sv.setDescription(description);
		return addProperty(s, sv);
	}

	public Property withRichString(Schema s, String id, String name) {
		StringVar sv = new StringVar(id, name);
		sv.setRich(true);
		return addProperty(s, sv);
	}

	public Property withString(Schema s, String id, String name, List<String> exampleValues) {
		return addProperty(s, new StringVar(id, name, exampleValues));
	}

	public Property withReal(Schema s, String id, String name) {
		return withReal(s, id, name, Unit.Number);
	}

	
	public Property withReal(Schema s, String id, String name, Unit unit) {
		return addProperty(s, new RealVar(id, name, unit));
	}

	public Property withInt(Schema s, String id, String name) {
		return addProperty(s, new IntegerVar(id, name));
	}

	public Property withGeoPoint(Schema s, String id, String name) {
		return addProperty(s, new GeoPointVar(id, name));
	}
	
	public Property withTimePoint(Schema s, String id, String name) {
		return addProperty(s, new TimePointVar(id, name));
	}
	
	public Pattern extending(String... patternIDs) {
		for (String p : patternIDs)
			ext.add(p);
		return this;
	}

	@Override
	public String toString() {
		//return "{ " + getID() + " (" + getName() + ") " + " <" + Arrays.asList(getExtends()) + "> [" + getInheritedProperties() + "] }";
        return getName();
	}

	public void setDescription(String patternDesc) {
		this.desc = patternDesc;		
	}
	
}
