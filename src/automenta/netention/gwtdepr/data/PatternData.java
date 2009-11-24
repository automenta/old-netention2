package automenta.netention.gwtdepr.data;

import java.io.Serializable;

public class PatternData extends NodeData implements Serializable  {

	private String[] ext;
	private String[] properties;
	private String[] defProperties;
	
	//private Collection<Variable> var;
	private String desc;
	//private List<Variable> defVar;

	public PatternData() {
		super();
	}
	
	public PatternData(String id, String name, String description, String[] ext, String[] properties, String[] definedProperties) {
		super(id, name);
		this.desc = description;
		this.properties = properties;
		this.defProperties = definedProperties;
		this.ext = ext;
	}

//	public List<Variable> getDefinedVariables() {
//		return defVar;
//	}

	public String getDescription() {
		return desc;
	}

	public String[] getExtends() {
		return ext;
	}

	public String[] getDefProperties() {
		return defProperties;
	}
	public String[] getProperties() {
		return properties;
	}
	
}
