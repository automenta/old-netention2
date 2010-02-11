/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention;

import automenta.netention.io.Async;
import automenta.netention.node.*;
import automenta.netention.linker.DetailLink;
import automenta.netention.linker.Linker;
import automenta.netention.linker.hueristic.DefaultHeuristicLinker;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * object representing a user's first-person account
 */
public class Self {

    final int numThreads = 12;
    private Collection<Action> runningActions =  new CopyOnWriteArrayList();
    private ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
    private static final Logger logger = Logger.getLogger(Self.class.toString());
    private Memory memory = new Memory();
    private final Schema schema;
    //private Map<String, Agent> agents = new HashMap();
    private Map<String, Object> nodeIndex = new HashMap();
    private Linker linker;
    private final Agent agent;

    public Self(String uid, String name) {
        super();

        this.schema = new Schema();

        agent = addNode(new Agent(uid, name));

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

//	public Map<String, Agent> getAgents() {
//		return agents;
//	}
    public Schema getSchema() {
        return schema;
    }

//	public Agent newAgent(final String id, String name) {
//		final Agent da = new Agent(id, name) {
////			@Override public Node newDetail(String name, String... patterns) {
////				return Network.this.newDetail(id, name, patterns);
////			}
//		};
//		addAgent(da);
//		return da;
//	}
//	public Agent addAgent(Agent a) {
//		synchronized (agents) {
//			agents.put(a.getID(), a);
//		}
//        return a;
//	}
    public Detail newDetail(Agent a, String name, String... patterns) {
        Detail p = new Detail(newRandomID("node"), name, patterns);
        p.setCreator(a.getID());

        addNode(p);

        a.addNode(p.getID());

        updateNode(p);

        return p;
    }

    public void addVertex(Object x) {
        getMemory().addVertex(x);
    }

    public <N extends Node> N addNode(N n) {
        getMemory().addVertex(n);
        nodeIndex.put(n.getID(), n);
        return n;
    }

    public void removeNode(Node n) {
        removeNode(n.getID());
    }

    public void removeNode(String nodeID) {
        Object removed = nodeIndex.remove(nodeID);
        if (removed != null) {
            if (removed instanceof Detail) {
                getLinker().removeNode((Detail) removed);
            }
        }
        getMemory().removeVertex(removed);
    }

    private String newRandomID(String prefix) {
        return prefix + ":" + UUID.randomUUID().toString();
    }

    public Map<String, Object> getNodeIndex() {
        return nodeIndex;
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
            if (!l.contains(t)) {
                l.add(t);
            }
        }

        return l;
    }

    protected void addInheritedPatterns(Set<String> s, String x) {
        if (s.contains(x)) {
            return;
        }

        s.add(x);

        Pattern p = getSchema().getPattern(x);
        for (String pp : p.getExtends()) {
            addInheritedPatterns(s, pp);
        }

    }

    public Object getNode(String nodeID) {
        return getNodeIndex().get(nodeID);
    }

    @Deprecated public Agent getAgent(String agentID) {
        //TODO make <X> get(Class<X> c)
        Object o = getNodeIndex().get(agentID);
        if (o instanceof Agent) {
            return (Agent) o;
        }
        return null;
    }

    public Linker getLinker() {
        return linker;
    }

    public void updateNode(Detail n) {
        try {
            getLinker().updateNode(n);
            logger.info("updated node: " + n);
        } catch (Exception e) {
            logger.severe(e.toString());
            e.printStackTrace();
        } finally {
        }
    }

    public Memory getMemory() {
        return memory;
    }

    public Collection<Object> getNodes() {
        return getMemory().getVertices();
    }

    /** the agent concept of this self */
    public Agent getAgent() {
        return agent;
    }

    public <X> Collection<X> getAll(Class<? extends X> c) {
        List<X> l = new LinkedList<X>();
        for (Object y : getMemory().getVertices()) {
            if (c.isAssignableFrom(y.getClass())) {
                l.add((X) y);
            }
        }
        return l;
    }

    public <X> X getThe(Class<? extends X> c) {
        for (Object y : getMemory().getVertices()) {
            if (c.isAssignableFrom(y.getClass())) {
                return (X) y;
            }
        }
        return null;
    }

    public Collection<Action> getRunningActions() {
        return runningActions;
    }

    /** runs an action in the background */
    public synchronized void run(final Action a, final Async callback) {
        runningActions.add(a);

        threadPool.submit(new Callable() {

            public Object call() throws Exception {
                Object result = null;
                try {
                    result = a.call();
                    callback.onFinished(result);
                } catch (Exception e) {
                    callback.onError(e);
                    result = e;
                } finally {
                    runningActions.remove(a);
                }
                return result;
            }
        });
    }
}
