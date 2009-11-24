/**
 * 
 */
package automenta.netention.gwtdepr.ui;

import java.util.ArrayList;

import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.AgentData;
import automenta.netention.gwtdepr.data.NodeData.NodeHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ToggleButton;

abstract public class NodeListPanel1 extends DockPanel {

	private ArrayList<NodeListButton> nodeButtons = new ArrayList();
	private String selectedNode;

	public static class NodeListButton extends ToggleButton implements NodeHandler {
		private String node;

		public NodeListButton(String nodeID, String text) {
			super(text);
			//addStyleName("NodeListButton");
			this.node = nodeID;
		}

		public String getNode() {
			return node;
		}

		@Override public void onNameChanged(String previousName, String nextName) {
			setText(nextName);			
		}

	}

	public NodeListPanel1(NetworkServiceAsync netService, AgentData agentData) {
		super();

		addStyleName("NodeListPanel");

		DockPanel topPanel = new DockPanel();
		topPanel.addStyleName("NodeListPanelTop");
		add(topPanel, SOUTH);
		setCellVerticalAlignment(topPanel, ALIGN_BOTTOM);

		final Button addNodeButton = new Button("New...");
		addNodeButton.addClickHandler(new ClickHandler() {
			@Override public void onClick(ClickEvent event) {
				onAddNode();
			}			
		});
		topPanel.add(addNodeButton, WEST);

		ListBox typeFilter = new ListBox();
		typeFilter.addItem("All");
		typeFilter.addItem("Human");
		typeFilter.addItem("Organization");
		typeFilter.addItem("Event");
		typeFilter.addItem("Thing");

		topPanel.add(typeFilter, EAST);
		topPanel.setCellHorizontalAlignment(typeFilter, ALIGN_RIGHT);




		final FlowPanel nodeList = new FlowPanel();


		nodeList.addStyleName("NodeList");

		int numNodes = agentData.getNodes().size();

		nodeButtons.clear();
		nodeButtons.ensureCapacity(numNodes);

		for (String p : agentData.getNodes().keySet()) {
			final NodeListButton b = new NodeListButton(p, agentData.getNodes().get(p));
			b.addClickHandler(new ClickHandler() {
				@Override public void onClick(ClickEvent event) {
					setPressedButton(b);
				}
			});
			nodeButtons.add(b);


			Panel bPanel = new FlowPanel();
			bPanel.addStyleName("NodeListButtonPanel");
			bPanel.add(b);

			nodeList.add(bPanel);
		}

		if (nodeButtons.size() > 0)
			setPressedButton(nodeButtons.get(0));

		add(nodeList, CENTER);

		setWidth("100%");



	}

	public void selectNode(String nodeID) {
		NodeListButton nlb = getNodeButton(nodeID);
		if (nlb!=null) {
			setPressedButton(nlb);
		}
	}

	private NodeListButton getNodeButton(String nodeID) {
		//TODO use a Map instead of this linear search?
		for (NodeListButton nlb : nodeButtons) {
			if (nlb.getNode().equals(nodeID))
				return nlb;
		}
		return null;
	}

	private void setPressedButton(NodeListButton b) {
		
		//TODO more efficient to only setUp the previously setDown 
		for (NodeListButton tb : nodeButtons) {
			tb.setDown( tb == b);
		}

		if (selectedNode!=b.getNode()) {
			selectedNode = b.getNode();
			onNodeSelected(selectedNode, b);
		}
	}						

	abstract protected void onAddNode();
	abstract protected void onNodeSelected(String nodeID, NodeHandler... nodeHandler);

}


//public NodeListPanel(NetworkServiceAsync netService, String agent) {
//	super();
//
//	
//	Panel buttonPanel = new FlowPanel();
//	add(buttonPanel, SOUTH);
//	
//	final Button addNodeButton = new Button("Add...");			
//	buttonPanel.add(addNodeButton);
//	
//	
//
//	final ListBox nodeList = new ListBox();
//	nodeList.addStyleName("NodeList");
//	netService.getAgentData(agent, new AsyncCallback<AgentData>() {
//		@Override public void onFailure(Throwable caught) {
//			add(new Label(caught.toString()), SOUTH);
//		}
//		@Override public void onSuccess(AgentData a) {
//			for (String p : a.getNodes().keySet()) {
//				nodeList.addItem(a.getNodes().get(p), p);
//			}
//			
//			nodeList.setVisibleItemCount(Math.max(12, a.getNodes().size()));				
//
//			if (nodeList.getItemCount() > 0)
//				selectNode(nodeList.getValue(nodeList.getSelectedIndex()));
//
//		}
//	});
//	nodeList.addChangeHandler(new ChangeHandler() {
//		@Override public void onChange(ChangeEvent event) {
//			selectNode(nodeList.getValue(nodeList.getSelectedIndex()));
//		}			
//	});
//			
//	add(nodeList, CENTER);
//	
//	nodeList.setSize("100%", "100%");
//	setHeight("100%");
//	setWidth("100%");
//	addStyleName("NodeListPanel");
//	
//
//	
//}
