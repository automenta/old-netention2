package automenta.netention.api;

import automenta.netention.api.Node;
import automenta.netention.api.Detail;
import automenta.netention.api.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.data.AgentData;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.gwtdepr.data.DetailLinkData;
import automenta.netention.gwtdepr.data.NodeData;
import automenta.netention.gwtdepr.data.PatternData;
import automenta.netention.gwtdepr.data.PatternDataEx;
import automenta.netention.api.value.Property;
import automenta.netention.example.ExampleNetwork;
import automenta.netention.api.linker.DetailLink;


/**
 * The server side implementation of the RPC service.
 */
public class NetworkServiceImpl implements NetworkService {

	private ExampleNetwork network;

//	@Override public String greetServer(String input) {
//		String serverInfo = getServletContext().getServerInfo();
//		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
//		return "Hello, " + input + "!<br><br>I am running " + serverInfo
//		+ ".<br><br>It looks like you are using:<br>" + userAgent;
//	}

	public NetworkServiceImpl() {
		super();
		network = new ExampleNetwork();
	}

	@Override public List<PatternData> getPatterns(String rootPattern) {
		if (rootPattern == null) {
			//return all
			List<String> allPatterns = new ArrayList(getNetwork().getSchema().getPatterns().keySet());
			Collections.sort(allPatterns);
			
			List<PatternData> pd = new LinkedList();
			int i = 0;
			for (String s : allPatterns) {
				pd.add(getPatternData(s));
			}
						
			return pd;
		}
		return null;
	}

	@Override public PatternData getPatternData(String patternID) {
		Pattern p = getNetwork().getSchema().getPatterns().get(patternID);		
		return new PatternData(	p.getID(), 
								p.getName(), 
								p.getDescription(), 
								p.getExtends(), 
								p.getInheritedProperties().toArray(new String[0]), 
								p.getDefinedProperties().toArray(new String[0]));
	}
	
	@Override public PatternDataEx getPatternDataEx(String patternID) {
		Pattern p = getNetwork().getSchema().getPatterns().get(patternID);
		
		Map<String, Property> dpd = new HashMap();
		for (String s : p.getDefinedProperties()) {
			dpd.put(s, getProperty(s));
		}
		
		return new PatternDataEx(	p.getID(), 
								p.getName(), 
								p.getDescription(), 
								p.getExtends(), 
								p.getInheritedProperties().toArray(new String[0]), 
								p.getDefinedProperties().toArray(new String[0]),
								dpd );
	}
	
	@Override public Map<String,PatternDataEx> getPatternDataEx(String[] patterns) {
		Map<String,PatternDataEx> pde = new HashMap();
		
		for (String p : patterns) {
			pde.put(p, getPatternDataEx(p));
		}
		
		return pde;
	}
	

	@Override public Property[] getPropertyData(String[] properties) {
		Property[] pd = new Property[properties.length];
		int i = 0;
		for (String property : properties) {
			pd[i++] = getProperty(property);
		}
		return pd;
	}
	
	private Property getProperty(String property) {
		Property p = getNetwork().getSchema().getProperties().get(property);
		if (p == null)
			return null;
		if (!(p instanceof Property))
			return null;
		return p;
	}

//	protected PropertyData getPropertyData(String property) {
//		Property p = getNetwork().getSchema().getProperties().get(property);
//		return new PropertyData(p.getID(), p.getName(), p.getDescription(), p.getClass().getSimpleName());
//	}

	@Override public String[] getAgents() {
		return getNetwork().getAgents().keySet().toArray(new String[0]);
	}
	
	
	@Override public AgentData getAgentData(String agent) {
		Agent a = getNetwork().getAgents().get(agent);
		if (a == null)
			return null;
		
		LinkedHashMap<String, String> nodeIDtoName = new LinkedHashMap();
		for (String id : a.getNodes()) {
			String name = getNetwork().getNodes().get(id).getName();
			nodeIDtoName.put(id, name);
		}
		
		return new AgentData(a.getID(), a.getName(), nodeIDtoName );
	}
	
	public NodeData getNodeData(Node n) {
		return new NodeData(n.getID(), n.getName());
	}
	
	public DetailData getDetailData(Detail p) {
		DetailData pd = new DetailData(p.getID(), p.getName(),
				p.getCreator(),
				p.getPatterns(),				
				p.getProperties());		
		return pd;		
	}
	
	@Override public List<String> getInheritedPatterns(List<String> patterns) {
		return getNetwork().getInheritedPatterns(patterns);
	}

	@Override public NodeData getNodeData(String nodeID) {
		Node p = getNetwork().getNodes().get(nodeID);
		if (p!=null) {
			if (p instanceof Detail) {
				return getDetailData((Detail)p);
			}
			else if (p instanceof Pattern) {
				return getPatternData(nodeID);
			}
			else {
				return getNodeData(p);
			}
		}
		return null;
		
	}
	
	@Override public DetailData getDetailData(String nodeID) {
		Node p = getNetwork().getNodes().get(nodeID);
		if (p!=null) {
			if (p instanceof Detail)
				return getDetailData((Detail)p);
		}
		return null;
	}
	
	@Override public Boolean deleteDetail(String agentID, String nodeID) {
		Agent agent = getNetwork().getAgent(agentID);
		if (agent!=null) {
			Node node = getNetwork().getNode(nodeID); 
			if (node!=null) {
				if (agent.getNodes().contains(nodeID)) {
					getNetwork().removeNode(nodeID);
					agent.removeNode(nodeID);
					return new Boolean(true);
				}
			}
		}
		return new Boolean(false);
	}
	
	@Override public DetailData setDetail(DetailData nextNodeData) {
		Node node = getNetwork().getNode(nextNodeData.getID());
		if (node instanceof Detail) {
			Detail n = (Detail) node;
			
			n.setName(nextNodeData.getName());
	
			n.getPatterns().clear();
			n.getPatterns().addAll(nextNodeData.getPatterns());
			
			n.getProperties().clear();
			n.getProperties().addAll(nextNodeData.getProperties());

			getNetwork().updateNode(n);
			
			return getDetailData(n);
		}
		
		return null;
		
	}

	
	@Override public DetailData getNewDetail(String agentID) {
		Detail n = getNetwork().newDetail(agentID, "Untitled");
		return getDetailData(n);
	}
	
	protected ExampleNetwork getNetwork() {
		return network;
	}

	@Override public List<DetailLinkData> getMessages(String agentID, String subjectID) {
		Agent a = getNetwork().getAgent(agentID);
		
		List<DetailLinkData> lwd = new LinkedList();
		
		Collection<Message> msgs = a.getMessages().get(subjectID);
		if (msgs!=null) {
			for (Message m : msgs) {
				if (m instanceof DetailLink) {
					DetailLink dl = (DetailLink)m;	
					lwd.add( getDetailLinkData(dl) ) ;
				}
			}
		}
		
		return lwd;
	}
	
	@Override public DetailLinkData getLinkData(String detailA, String detailB) {
		Detail a = (Detail) getNetwork().getNode(detailA);
		Detail b = (Detail) getNetwork().getNode(detailB);
		DetailLink dl = getNetwork().getLinker().getLink(a, b);
		return getDetailLinkData(dl);
	}

	protected DetailLinkData getDetailLinkData(DetailLink dl) {
		return new DetailLinkData( getDetailData(dl.getFromNode()), getDetailData(dl.getToNode()), dl.getStrength(), dl.getWhenCreated() );
	}
	
}
