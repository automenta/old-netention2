package automenta.netention.server.linker.esper;


import automenta.netention.server.linker.Linker;

/** weaver implementation using the Esper event-stream processing engine */
@Deprecated abstract public class EsperLinker implements Linker {
//	private static final Logger logger = Logger.getLogger(EsperLinker.class.toString());
//
//	private EPAdministrator admin;
//	private EPRuntime runtime;
//	private DefaultNetwork network;
//	private Map<String, EPStatement> nodeRules = new HashMap();
//
//	private EPServiceProvider service;
//
//	public EsperLinker(DefaultNetwork network) {
//		super();
//
//		this.network = network;
//
//		Configuration config = new Configuration();
//		config.addEventType("NodeEvent", NodeEvent.class);
//		config.getEngineDefaults().getThreading().setInternalTimerEnabled(false);
//
//		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);
//		epService.initialize();
//
//		this.service = epService;
//		this.admin = epService.getEPAdministrator();
//		this.runtime = epService.getEPRuntime();
//
//
//	}
//
//	@Override public void updateNode(Detail n) throws Exception {
//		String creator = n.getCreator();
//
//		final NodeEvent event = newEvent(creator, n);
//		if (event.getProperties().size() > 0) {
//			runtime.sendEvent(event);			
//			onNewEvent(event);
//		}
//
//		String rule = newRule(creator, n);
//		if (rule!=null) {
//
//			EPStatement p = admin.createPattern(rule);
//			nodeRules.put(n.getID(), p);
//
//			p.addListener(new UpdateListener() {
//				@Override public void update(EventBean[] newEvents, EventBean[] oldEvents) {
//					final NodeEvent other = (NodeEvent)newEvents[0].get("other");
//
//					emitLink(new DetailLink(event.getNodeID(), other.getNodeID()) {
//						@Override public String toString() {
//							return event.toString() + " -> " + other.toString();
//						}
//					});
//
//				}				
//			});
//
//			onNewRule(p);
//
//		}
//
//	}
//
//	@Override public DetailLink getLink(Detail a, Detail b) {
//		return null;
//	}
//
//	abstract protected void onNewEvent(NodeEvent event);
//	abstract protected void onNewRule(EPStatement statement);
//	abstract protected void emitLink(DetailLink link);
//
//	private String newRule(String creator, Detail n) {
//		Map<String, Expression> expressions = new HashMap();
//		for (PropertyValue p : n.getProperties()) {
//			if (p instanceof Expression) {
//				expressions.put(p.getProperty(), (Expression)p);
//			}
//		}
//		if (expressions.size() == 0)
//			return null;
//
//		String p = "every other=NodeEvent(";
//
//		//add pattern restrictions
//		//...
//
//		//add property expressions
//		for (String k : expressions.keySet()) {
//			Expression e = expressions.get(k);
//			//String exp = e.getExpression().replace(k, "properties('" + k + "')?");
//			String exp = "";
//			p += exp + ",";
//		}
//
//		p = p.substring(0, p.length() - 1); //HACK to remove the last comma
//		p +=")";
//
//		return p;
//	}
//
//	private NodeEvent newEvent(String creator, Detail n) {
//		return new NodeEvent(n.getID(), creator, getStatedProperties(n.getProperties()), n.getPatterns(), getNetwork().getInheritedPatterns(n));
//	}
//
//	private Map<String, Object> getStatedProperties(List<PropertyValue> list) {
//		Map<String, Object> m = new HashMap();
//		for (PropertyValue p : list) {
//			if (p instanceof DefiniteValue) {
//				Object value = ((DefiniteValue) p).getValue();
//				Object previous = m.put(p.getProperty(), value);
//				if (previous!=null) {
//					logger.severe(this + " only supports properties with singular multiplicity; " + p.getProperty() + " replaced " + previous + " with " + value);
//				}
//			}
//		}
//		return m;
//	}
//
//
//	@Override public void removeNode(Detail n) {
//	}
//
//	public DefaultNetwork getNetwork() {
//		return network;
//	}
//
//	public void printMetrics() {
//		System.out.println(" # events evaluated: " + runtime.getNumEventsEvaluated());
//		System.out.println(" # rules: " + admin.getStatementNames().length);
//	}

	
}
