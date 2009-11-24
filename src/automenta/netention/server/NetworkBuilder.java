package automenta.netention.server;


import java.io.File;
import java.net.URI;
import java.util.logging.Logger;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import automenta.netention.server.value.Unit;
import automenta.netention.server.value.real.RealVar;
import automenta.netention.server.impl.DefaultNetwork;

public class NetworkBuilder {
	private static final Logger logger = Logger.getLogger(NetworkBuilder.class.toString());
	
	
	protected static void updateAllDetails(DefaultNetwork n) {
		//TODO use a more efficient method than updating all nodes after load
		for (Node node : n.getNodes().values()) {
			if (node instanceof Detail) {
				n.updateNode((Detail)node);
			}
		}
	}
	
//	public static Object loadGroovyScript(DefaultNetwork n, URI groovyScript) throws Exception {
//		Binding binding = new Binding();
//		binding.setVariable("network", n);
//		binding.setVariable("schema", n.getSchema());
//
//
//		GroovyShell shell = new GroovyShell(binding);
//
//		Object value = shell.evaluate(new File(groovyScript));
//
//		updateAllDetails(n);
//
//		return value;
//	}

	public static void loadXML(DefaultNetwork n, URI schema1) throws Exception {
		Builder parser = new Builder();
		Document doc = parser.build(new File(schema1));

		Element root = doc.getRootElement();
		if (root.getQualifiedName().equals("schema")) {
			loadSchemaXML(n, root);
		}
		

		updateAllDetails(n);
	}

	protected static void loadSchemaXML(DefaultNetwork n, Element schemaRoot) {
		for (int i = 0; i < schemaRoot.getChildElements().size(); i++) {
			Element child = schemaRoot.getChildElements().get(i);
			if (child.getQualifiedName().equals("pattern")) {
				loadPatternXML(child, n);
			}
		}
	}

	private static void loadPatternXML(Element patternElement, DefaultNetwork n) {
		String id = patternElement.getAttributeValue("id");
		String name = patternElement.getAttributeValue("name");
		if (name == null)
			name = id;
		
		String patternDesc = "";
		if (patternElement.getChildCount() > 0)
			patternDesc = patternElement.getChild(0).getValue();

		Pattern pattern = n.getSchema().newPattern(id, name);
		pattern.setDescription(patternDesc);
		
		Elements patternData = patternElement.getChildElements();
		for (int i = 0; i < patternData.size(); i++) {
			Element d = patternData.get(i);
			
			String propID = d.getAttributeValue("id");
			String propName = d.getAttributeValue("name");
			if ((propName == null) && (propID!=null)) {
				propName = propID.substring(0,1).toUpperCase() + propID.substring(1, propID.length());
			}
			
			//TODO parse cardinality

			String description = "";
			if (d.getChildCount() > 0)
				description = d.getChild(0).getValue();

			//TODO (XML) set description

			if (d.getQualifiedName().equals("extend")) {
				String extendsID = propID;
				pattern.extending(extendsID);
			}
			else if (d.getQualifiedName().equals("string")) {			
				pattern.withString(propID, propName);
				//TODO (XML) parse preset values
			}
			else if (d.getQualifiedName().equals("int")) {			
				pattern.withInt(propID, propName);
			}
			//TODO (XML) parse Boolean
			else if (d.getQualifiedName().equals("real")) {							
				String unitString = d.getAttributeValue("unit");
				pattern.withReal(propID, propName, RealVar.getUnit(unitString));
			}
			else if (d.getQualifiedName().equals("timepoint")) {							
				pattern.withReal(propID, propName, Unit.TimePoint);
			}
			else if (d.getQualifiedName().equals("node")) {
				String classID = d.getAttributeValue("class");
				pattern.withNode(propID, propName, classID);
			}
			else if (d.getQualifiedName().equals("geopoint")) {
				pattern.withGeoPoint(propID, propName);
			}
			else {
				logger.severe("unhandled element: " + d);
			}
			
		}
		
	}


}
