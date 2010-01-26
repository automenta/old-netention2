/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.api;

import automenta.netention.api.linker.DetailLink;
import automenta.netention.api.linker.Linker;
import automenta.netention.api.linker.hueristic.DefaultHeuristicLinker;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

/**
 *
 * @author seh
 */
public class Network {
	private static final Logger logger = Logger.getLogger(Network.class.toString());

	private Map<String, Agent> agents = new HashMap();
	private Schema schema = new Schema();
	private Map<String, Node> nodes = new HashMap();

	private Linker linker;

	public Network() {
		super();

//		schema = new Schema() {
//			@Override public Pattern newPattern(String id, String name) {
//				Pattern p = super.newPattern(id, name);
//				addNode(p);
//				return p;
//			}
//		};

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

	public Map<String, Agent> getAgents() {
		return agents;
	}

	public Schema getSchema() {
		return schema;
	}


	public Agent newAgent(final String id, String name) {
		final Agent da = new Agent(id, name) {
//			@Override public Node newDetail(String name, String... patterns) {
//				return Network.this.newDetail(id, name, patterns);
//			}
		};
		addAgent(da);
		return da;
	}

	public Agent addAgent(Agent a) {
		synchronized (agents) {
			agents.put(a.getID(), a);
		}
        return a;
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

	public void addNode(Node n) {
		synchronized (nodes) {
			nodes.put(n.getID(), n);
		}
	}
	public void removeNode(Node n) {
		removeNode(n.getID());
	}

	public void removeNode(String nodeID) {
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
