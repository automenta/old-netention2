package automenta.netention.gwtdepr;


import java.util.List;
import java.util.Map;

import automenta.netention.gwtdepr.data.AgentData;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.gwtdepr.data.DetailLinkData;
import automenta.netention.gwtdepr.data.NodeData;
import automenta.netention.gwtdepr.data.PatternData;
import automenta.netention.gwtdepr.data.PatternDataEx;
import automenta.netention.api.value.Property;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>NetworkService</code>.
 */
public interface NetworkServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback);
	
	void getPatterns(String rootPattern, AsyncCallback<List<PatternData>> asyncCallback);
	void getPatternData(String pattern, AsyncCallback<PatternData> callback);
	void getPatternDataEx(String pattern, AsyncCallback<PatternDataEx> callback);
	void getPatternDataEx(String[] patterns, AsyncCallback<Map<String,PatternDataEx>> callback);
	void getPropertyData(String[] properties, AsyncCallback<Property[]> callback);
	
	void getAgents(AsyncCallback<String[]> callback);
	void getAgentData(String agent, AsyncCallback<AgentData> callback);

	
	void getNodeData(String nodeID, AsyncCallback<NodeData> asyncCallback);
	void getDetailData(String nodeID, AsyncCallback<DetailData> callback);

	void getNewDetail(String agentID, AsyncCallback<DetailData> asyncCallback);

	void setDetail(DetailData nextNodeData, AsyncCallback<DetailData> asyncCallback);
	void deleteDetail(String agentID, String nodeID, AsyncCallback<Boolean> asyncCallback);

	void getInheritedPatterns(List<String> patterns, AsyncCallback<List<String>> asyncCallback);
	
	void getMessages(String agentID, String subjectID, AsyncCallback<List<DetailLinkData>> asyncCallback);

	void getLinkData(String detailA, String detailB, AsyncCallback<DetailLinkData> asyncCallback);

}
