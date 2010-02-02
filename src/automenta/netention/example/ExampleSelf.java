package automenta.netention.example;

import automenta.netention.Pattern;
import automenta.netention.node.Agent;
import automenta.netention.Self;
import automenta.netention.io.XMLSchemaBuilder;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExampleSelf extends Self {

    public ExampleSelf() {
        super();

        initSchema();
        initAgents();
    }

    protected void initSchema() {
        try {
            URI schema = ExampleSelf.class.getResource("citizen.xml").toURI();
            XMLSchemaBuilder.loadXML(this, schema);
        } catch (Exception ex) {
            Logger.getLogger(ExampleSelf.class.getName()).log(Level.SEVERE, null, ex);
        }

        //add schema elements to memory
        for (Pattern p : getSchema().getPatterns().values()) {
            addNode(p);
        }

    }

    protected void initAgents() {
        Agent s = addNode(new Agent("seh", "SeH"));
        newDetail(s, "Unnamed");
        newDetail(s, "Unnamed-2");
        newDetail(s, "Unnamed-3");
        
        addNode(new Agent("carlos", "Carlos"));
        addNode(new Agent("karl", "Karl"));
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
