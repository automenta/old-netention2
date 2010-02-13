package automenta.netention;

import automenta.netention.node.*;
import java.util.Map;

import automenta.netention.value.Property;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/** schema/ontology */
public class Schema {

    private Map<String, Pattern> patterns = new HashMap();
    private Map<String, Property> properties = new HashMap();

    public Map<String, Pattern> getPatterns() {
        return patterns;
    }

    public Collection<Pattern> getRootPatterns() {
        List<Pattern> lp = new LinkedList();
        for (Pattern p : patterns.values()) {
            if (p.getExtends().length == 0) {
                lp.add(p);
            }
        }
        return lp;
    }

    public Collection<Pattern> getChildren(Pattern p) {
        Set<Pattern> sp = new HashSet();
        for (Pattern v : patterns.values()) {
            //TODO this is inefficient. either store p.extends as a list or do a search thru the array
            if (Arrays.asList(v.getExtends()).contains(p.getID())) {
                sp.add(v);
            }
        }

        return sp;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public Property getProperty(String id) {
        return properties.get(id);
    }

    public void reset() {
        patterns.clear();
        properties.clear();
    }

    public Pattern newPattern(final String id, String name, String... extendIDs) {
        Pattern p = new Pattern(id, name, extendIDs);

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

    public Pattern getPattern(String patternID) {
        return getPatterns().get(patternID);
    }

    public List<Pattern> getPatterns(Detail d) {
        ArrayList<Pattern> al = new ArrayList(d.getPatterns().size());
        for (String s : d.getPatterns()) {
            al.add(getPattern(s));
        }
        return al;
    }

    public Iterable<String> getInheritedPatterns(Pattern p) {
        return getProperties(p.getID());
    }

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
