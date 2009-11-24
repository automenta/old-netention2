package automenta.netention.test;


import automenta.netention.server.Agent;
import automenta.netention.server.NetworkBuilder;
import automenta.netention.server.Pattern;
import automenta.netention.server.impl.DefaultNetwork;
import java.io.File;
import java.net.URI;
import junit.framework.TestCase;

public class TestXMLSchema extends TestCase {

	public void testSchemaLoad() throws Exception {


        String rootPath = "file:" + new File(".").getAbsolutePath() + "/schema/";
        String schemaPath = rootPath + "schema2.xml";

        System.out.println(rootPath + " : " + schemaPath);

        
		DefaultNetwork n = new DefaultNetwork();
		URI schema1 = new URI(schemaPath);

		NetworkBuilder.loadXML(n, schema1);

		for (Pattern p : n.getSchema().getPatterns().values()) {
			System.out.println(p);
		}

		System.out.println();

		for (Agent a : n.getAgents().values()) {
			System.out.println(a);
		}

	}
	
}
