package automenta.netention.example;

import automenta.netention.api.Agent;
import automenta.netention.api.Network;
import automenta.netention.api.NetworkBuilder;
import automenta.netention.api.Schema;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExampleNetwork extends Network {

    public ExampleNetwork() {
        super();

        initSchema(getSchema());
        initAgents();
    }

    protected void initSchema(Schema s) {
        try {
            URI schema = ExampleNetwork.class.getResource("citizen.xml").toURI();
            NetworkBuilder.loadXML(this, schema);
        } catch (Exception ex) {
            Logger.getLogger(ExampleNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected void initAgents() {
        Agent s = addAgent(new Agent("seh", "SeH"));
        newDetail(s, "Unnamed");
        newDetail(s, "Unnamed-2");
        newDetail(s, "Unnamed-3");
        
        addAgent(new Agent("carlos", "Carlos"));
        addAgent(new Agent("karl", "Karl"));
    }
    
//	private static final Logger logger = Logger.getLogger(ExampleNetwork.class.toString());
//
//	private List<Agent> agents = new LinkedList();
//
//	public ExampleNetwork() {
//		super();
//
//		try {
//			URI schema2 = new URI("file:/work/ew/netention/automenta.netention.gwt/schema/schema2.xml");
//			NetworkBuilder.loadXML(this, schema2);
//
////			URI schema1 = new URI("file:/work/ew/netention/automenta.netention.gwt/schema/schema1.groovy");
////			NetworkBuilder.loadGroovyScript(this, schema1);
//		}
//		catch (Exception e) {
//			logger.severe(e.toString());
//		}
//
//	}
}
