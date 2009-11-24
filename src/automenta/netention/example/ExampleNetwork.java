package automenta.netention.example;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import automenta.netention.server.Agent;
import automenta.netention.server.Detail;
import automenta.netention.server.NetworkBuilder;
import automenta.netention.server.impl.DefaultNetwork;


public class ExampleNetwork extends DefaultNetwork {
	private static final Logger logger = Logger.getLogger(ExampleNetwork.class.toString());
	
	private List<Agent> agents = new LinkedList();

	public ExampleNetwork() {
		super();
		
		try {
			URI schema2 = new URI("file:/work/ew/netention/automenta.netention.gwt/schema/schema2.xml");
			NetworkBuilder.loadXML(this, schema2);

//			URI schema1 = new URI("file:/work/ew/netention/automenta.netention.gwt/schema/schema1.groovy");
//			NetworkBuilder.loadGroovyScript(this, schema1);
		}
		catch (Exception e) {
			logger.severe(e.toString());
		}
		
	}

}
