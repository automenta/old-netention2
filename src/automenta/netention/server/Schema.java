package automenta.netention.server;

import java.util.Map;

import automenta.netention.server.value.Property;

/** schema/ontology */
public interface Schema {

	public Map<String, Pattern> getPatterns();
	public Map<String, Property> getProperties();

	public void reset();
	
}
