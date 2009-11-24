package automenta.netention.server.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import automenta.netention.server.Agent;
import automenta.netention.server.Detail;
import automenta.netention.server.Network;
import automenta.netention.server.Node;
import automenta.netention.server.Pattern;
import automenta.netention.server.linker.DetailLink;
import automenta.netention.server.linker.Linker;
import automenta.netention.server.linker.hueristic.DefaultHeuristicLinker;


public class DefaultNetwork implements Network {
	private static final Logger logger = Logger.getLogger(DefaultNetwork.class.toString());
	
	private Map<String, Agent> agents = new HashMap();
	private DefaultSchema schema;
	private Map<String, Node> nodes = new HashMap();

	private Linker linker;

	public DefaultNetwork() {
		super();
		
		schema = new DefaultSchema() {
			@Override public Pattern newPattern(String id, String name) {
				Pattern p = super.newPattern(id, name);
				addNode(p);
				return p;
			}			
		};
		
		linker = new DefaultHeuristicLinker() {
			
			@Override public void emitLink(DetailLink link) {
				logger.info(" LINK: " + link);
				
				Detail fromNode = (Detail) getNode(link.getFromNode());
				Detail toNode = (Detail) getNode(link.getToNode());
				
				String fromCreator = fromNode.getCreator();
				String toCreator = toNode.getCreator();
				
				getAgent(fromCreator).addMessage(fromNode.getID(), link);
				getAgent(toCreator).addMessage(toNode.getID(), link);				
			}
		};
		
		/*(this) {
			@Override protected void emitLink(DetailLink link) {
				logger.info(" LINK: " + link);
				
				Detail fromNode = (Detail) getNode(link.getFromNode());
				Detail toNode = (Detail) getNode(link.getToNode());
				
				String fromCreator = fromNode.getCreator();
				String toCreator = toNode.getCreator();
				
				getAgent(fromCreator).addMessage(fromNode.getID(), link);
				getAgent(toCreator).addMessage(toNode.getID(), link);
			}

			@Override protected void onNewEvent(NodeEvent event) {
				logger.info(" EVENT: " + event);
			}

			@Override protected void onNewRule(EPStatement statement) {
				logger.info(" RULE: " + statement);				
			}			
		};
		*/
		
	}
	
	@Override public Map<String, Agent> getAgents() {
		return agents;
	}
 
	@Override public DefaultSchema getSchema() {
		return schema;
	}

	
	public Agent newAgent(final String id, String name) {
		final Agent da = new Agent(id, name) {
			@Override public Node newDetail(String name, String... patterns) {
				return DefaultNetwork.this.newDetail(id, name, patterns);				
			}			
		};
		addAgent(da);
		return da;
	}
	
	public void addAgent(Agent a) {
		synchronized (agents) {
			agents.put(a.getID(), a);
		}
	}
	
	public Detail newDetail(Agent creator, String name, String... patterns) {
		return newDetail(creator.getID(), name, patterns);
	}
	
	public Detail newDetail(String agentCreator, String name, String... patterns) {
		Detail p = new Detail(newRandomID("node"), name, patterns);
		p.setCreator(agentCreator);
		
		addNode(p);

		Agent a = getAgents().get(agentCreator);
		a.addNode(p.getID());
				
		updateNode(p);
		
		return p;
	}
	
	@Override public void addNode(Node n) {
		synchronized (nodes) {
			nodes.put(n.getID(), n);
		}
	}
	public void removeNode(Node n) {
		removeNode(n.getID());
	}
	
	@Override public void removeNode(String nodeID) {
		synchronized (nodes) {
			Node removed = nodes.remove(nodeID);
			if (removed!=null)
				if (removed instanceof Detail)
					getLinker().removeNode((Detail)removed);
		}
	}

	private String newRandomID(String prefix) {
		return prefix + ":" + UUID.randomUUID().toString();
	}
	
	public Map<String, Node> getNodes() {
		return nodes ;
	}

	public List<String> getInheritedPatterns(Detail n) {
		return getInheritedPatterns(n.getPatterns());
	}

	public List<String> getInheritedPatterns(List<String> definedPatterns) {
		Set<String> s = new HashSet();
		for (String x : definedPatterns) {
			addInheritedPatterns(s, x);
		}
		
		//create the list of patterns with the defined patterns first, and any remaining hierarchicals at the end
		List<String> l = new LinkedList();
		for (String x : definedPatterns) {
			l.add(x);
		}
		for (String t : s) {
			if (!l.contains(t))
				l.add(t);
		}

		return l;
	}

	protected void addInheritedPatterns(Set<String> s, String x) {
		if (s.contains(x))
			return;
		
		s.add(x);
		
		Pattern p = getSchema().getPattern(x);
		for (String pp : p.getExtends()) {
			addInheritedPatterns(s, pp);
		}
		
	}

	public Node getNode(String nodeID) {
		return getNodes().get(nodeID);
	}
	public Agent getAgent(String agentID) {
		return getAgents().get(agentID);
	}

	public Linker getLinker() {
		return linker;
	}
	

	public void updateNode(Detail n) {
		try {
			getLinker().updateNode(n);
			logger.info("updated node: " + n);			
		} 
		catch (Exception e) {
			logger.severe(e.toString());
			e.printStackTrace();
		}
		finally {
		}
	}

}
