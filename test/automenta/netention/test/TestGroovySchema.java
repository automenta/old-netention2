package automenta.netention.test;

import java.net.URI;

import junit.framework.TestCase;
import automenta.netention.server.Agent;
import automenta.netention.server.NetworkBuilder;
import automenta.netention.server.Pattern;
import automenta.netention.server.impl.DefaultNetwork;

public class TestGroovySchema extends TestCase {

	public void testSchemaLoad() throws Exception {
		
		DefaultNetwork n = new DefaultNetwork();
		URI schema1 = new URI("file:/work/ew/netention/automenta.netention.example/schema/schema1.groovy");
		
		NetworkBuilder.loadGroovyScript(n, schema1);
	
		for (Pattern p : n.getSchema().getPatterns().values()) {
			System.out.println(p);
		}
		
		System.out.println();

		for (Agent a : n.getAgents().values()) {
			System.out.println(a);
		}

	}
	
}
