package automenta.netention.gwtdepr;


import java.util.List;
import java.util.Map;

import automenta.netention.gwtdepr.data.AgentData;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.gwtdepr.data.NodeData;
import automenta.netention.gwtdepr.data.PatternData;
import automenta.netention.gwtdepr.data.PatternDataEx;
import automenta.netention.gwtdepr.data.DetailLinkData;
import automenta.netention.value.Property;


/**
 * The client side stub for the RPC service.
 */
public interface NetworkService  {

	List<PatternData> getPatterns(String rootPatternID);

	PatternData getPatternData(String patternID);
	PatternDataEx getPatternDataEx(String patternID);
	Map<String,PatternDataEx> getPatternDataEx(String patterns[]);
	List<String> getInheritedPatterns(List<String> patterns);

	Property[] getPropertyData(String[] propertyIDs);
	
	String[] getAgents();
	AgentData getAgentData(String agentID);

	NodeData getNodeData(String nodeID);
	DetailData getDetailData(String nodeID);
	
	DetailData getNewDetail(String agentID);	
	DetailData setDetail(DetailData nextNodeData);
	Boolean deleteDetail(String agentID, String nodeID);

	List<DetailLinkData> getMessages(String agentID, String subjectID);

	DetailLinkData getLinkData(String detailA, String detailB);
}
