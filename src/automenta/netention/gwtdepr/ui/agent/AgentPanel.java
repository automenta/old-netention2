/**
 * 
 */
package automenta.netention.gwtdepr.ui.agent;

import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.AgentData;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.gwtdepr.data.NodeData.NodeHandler;
import automenta.netention.gwtdepr.ui.NodeListPanel;
import automenta.netention.gwtdepr.ui.NodePanel;
import automenta.netention.gwtdepr.ui.NodePanel.NodePanelMode;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;

abstract public class AgentPanel extends DockPanel {

	protected NodeListPanel nodeListPanel;
	protected NodePanel nodePanel;
	
	final private NetworkServiceAsync netService = GWT.create(NetworkService.class);
	
	protected AgentHeaderPanel headerPanel;
	private AgentData agent;
	private String nextSelectedNode = null;

	public AgentPanel() {
		super();

		addStyleName("AgentPanel");
		setHeight("100%");


	}

	public void setAgent(String agent) {

		netService.getAgentData(agent, new AsyncCallback<AgentData>() {			
			@Override public void onSuccess(final AgentData agentData) {
				AgentPanel.this.agent = agentData;
				refresh(agentData);
				if (nextSelectedNode != null) {
					nodeListPanel.selectNode(nextSelectedNode);
				}
			}
			
			@Override public void onFailure(Throwable caught) { 
				Window.alert(caught.toString());
			}
		});
		

	}

	protected void refresh() {
		setAgent(getAgent().getID());
	}
	
	abstract protected void refresh(AgentData agentData);		
	

	protected NodeListPanel newListPanel(AgentData agentData) {
		return new NodeListPanel(agentData) {
			@Override protected void onNodeSelected(String nodeID, NodeHandler... handler) {
				nodePanel.setNode(nodeID, NodePanelMode.Edit, handler);
			}				
			@Override protected void onAddNode() {
				addNode();
			}
		};
	}

	protected NodePanel newNodePanel(AgentData agentData) {
		return new NodePanel(agentData) {
			@Override protected void afterNodeSaved() {
				super.afterNodeSaved();
				
				//TODO only refresh the nodeList , not the nodePanel (which would not have changed during a save) - causes a noticeable flicker
				refresh(nodePanel.getNode().getID());
			}
			@Override protected void afterNodeDeleted() {
				super.afterNodeDeleted();
				
				//TODO refresh to the Node listed above the deleted one?
				refresh();
			}
			
			
		};
	}

	protected void refresh(String nextNode) {
		nextSelectedNode = nextNode;
		refresh();
	}

	public AgentHeaderPanel getHeader() {
		return headerPanel;
	}

	protected void addNode() {
		//TODO start 'New Node' creation process (aka Wizard) to elicit, at minimum: Name, at least 1 Pattern
		
		netService.getNewDetail(getAgent().getID(), new AsyncCallback<DetailData>() {

			@Override public void onSuccess(DetailData node) {
				refresh(node.getID());
			}
			

			@Override public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
			}

		});
	}

	public AgentData getAgent() {
		return agent;
	}
	

}

