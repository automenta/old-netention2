package automenta.netention.gwtdepr.data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import automenta.netention.gwtdepr.ui.detail.NodeEditPanel;
import automenta.netention.server.value.Property;
import automenta.netention.server.value.PropertyValue;
import automenta.netention.server.value.Value;

public class DetailData extends NodeData {

	private List<String> patterns;
	private List<PropertyValue> properties;
	private String creator;

	public DetailData() {
		super();
	}
	
	public DetailData(String id, String name, String creator, List<String> patterns, List<PropertyValue> properties) {
		super(id, name);

		this.patterns = patterns;
		this.properties = properties;
		this.creator = creator;
		
	}
	
	public List<String> getPatterns() {
		return patterns;
	}


	public List<PropertyValue> getProperties() {
		return properties;
	}

	public int getNumPropertiesDefined(String property) {
		int count = 0;
		for (PropertyValue pv : getProperties()) {
			if (pv.getProperty().equals(property))
				count++;
		}
		return count;
	}

	public String getCreator() {
		return creator;
	}
	
}
