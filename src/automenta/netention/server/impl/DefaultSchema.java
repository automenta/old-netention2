package automenta.netention.server.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import automenta.netention.server.value.Property;
import automenta.netention.server.Pattern;
import automenta.netention.server.Schema;

abstract public class DefaultSchema implements Schema {


	private Map<String, Pattern> patterns = new HashMap();
	private Map<String, Property> properties = new HashMap();

	@Override public Map<String, Pattern> getPatterns() {
		return patterns;
	}

	@Override
	public Map<String, Property> getProperties() {
		return properties;
	}


	public void reset() {
		patterns.clear();
		properties.clear();
	}


	public Pattern newPattern(final String id, String name) {
		Pattern p = new Pattern(id, name) {
			@Override public Collection<String> getInheritedProperties() {
				return DefaultSchema.this.getProperties(id);
			}
			@Override
			public DefaultSchema getSchema() {
				return DefaultSchema.this;
			}
			
		};

		getPatterns().put(id, p);
		return p;
	}

	public void addProperty(Property p) {
		properties.put(p.getID(), p);
	}


	public Collection<String> getProperties(String patternID) {
		Set<String> m = new HashSet();

		for (String v : getPattern(patternID).getDefinedProperties()) {
			m.add(v);
		}
		for (String p : getPattern(patternID).getExtends()) {
			for (String v : getProperties(p)) {
				m.add(v);
			}
		}

		return m;
	}

	public Pattern getPattern(String patternID) { return getPatterns().get(patternID); }





	//	public void include(Part p) {
	//		for (Part x : p.getParts()) {
	//			Pattern pt = getPatterns(p);
	//			
	//			for (Variable f : p.getVariables()) {
	//				pt.include(f);
	//			}
	//		}
	//	}
	//
	//	private Pattern getType(String t) {
	//		Pattern p = types.get(t);
	//		if (p == null) {
	//			p = new DefaultPartType(t);
	//			types.put(t, p);
	//		}
	//		return p;
	//	}


}
