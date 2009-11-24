/**
 * 
 */
package automenta.netention.gwtdepr.ui;


import java.util.List;

import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.AgentData;
import automenta.netention.gwtdepr.data.PatternData;
import automenta.netention.gwtdepr.data.NodeData.NodeHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

abstract public class NodeListPanel extends DockPanel {

	final private NetworkServiceAsync netService = GWT.create(NetworkService.class);

	//private ArrayList<NodeListButton> nodeButtons = new ArrayList();
	private String selectedNode;

	public class NodeTreeItem extends TreeItem {
		
		private String nodeID;

		public NodeTreeItem(String nodeID, String nodeName) {
			super(nodeName);
			this.nodeID = nodeID;
		}
		
		public String getNodeID() {
			return nodeID;
		}
		
	}
	
	public class AgentTreeItem extends TreeItem {
		
		private AgentData agent;

		public AgentTreeItem(AgentData agentData) {
			super();
			this.agent = agentData;
			
			refresh();
		}
		
		public AgentTreeItem(String agentID) {
			super();
			
			netService.getAgentData(agentID, new AsyncCallback<AgentData>() {
				@Override public void onFailure(Throwable caught) {
					Window.alert(caught.toString());					
				}
				@Override public void onSuccess(AgentData a) {
					AgentTreeItem.this.agent = a;
					refresh();
				}								
			});
		}

		public void refresh() {
			setText(getAgent().getName());

			for (String p : getAgent().getNodes().keySet()) {
				NodeTreeItem ti = new NodeTreeItem(p, getAgent().getNodes().get(p));
				addItem(ti );
			}					
		}

		public AgentData getAgent() {
			return agent;
		}
		
		public String getNodeID() {
			return getAgent().getID();
		}
		
	}

	public NodeListPanel(final AgentData myAgentData) {
		super();

		addStyleName("NodeListPanel");

		DockPanel topPanel = new DockPanel();
		topPanel.addStyleName("NodeListPanelTop");
		add(topPanel, NORTH);
		setCellVerticalAlignment(topPanel, ALIGN_BOTTOM);
		setCellHeight(topPanel, "0%");

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




		final Tree myTree = new Tree();
		myTree.addStyleName("NodeTree");

		int numNodes = myAgentData.getNodes().size();

		//Add 'my' nodes under 'Myself'
		AgentTreeItem myselfNode = new AgentTreeItem(myAgentData);
		myTree.addItem(myselfNode);		
		myselfNode.setState(true);

		
		//Add other agents
		final Tree community = new Tree();
		netService.getAgents(new AsyncCallback<String[]>() {		
			@Override public void onSuccess(String[] result) {
				for (String a : result) {
					if (!a.equals(myAgentData.getID()))
						community.addItem(new AgentTreeItem(a));
				}
			}			
			@Override public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
			}
		});
		
		//Add patterns
		final Tree patternTree = new Tree();

		netService.getPatterns(null, new AsyncCallback<List<PatternData>>() { 
			@Override public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
			}

			@Override public void onSuccess(List<PatternData> patterns) {
				for (PatternData pd : patterns) {
					patternTree.addItem(new NodeTreeItem(pd.getID(), pd.getName()));
				}
			}			
		});
				
		
		

		SelectionHandler<TreeItem> sh = new SelectionHandler<TreeItem>() {
			@Override public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem si = event.getSelectedItem();
				if (si instanceof NodeTreeItem)
					selectNode(((NodeTreeItem)si).getNodeID());
			}				
		};
		myTree.addSelectionHandler(sh);
		community.addSelectionHandler(sh);
		patternTree.addSelectionHandler(sh);


//		if (nodeButtons.size() > 0)
//			setPressedButton(nodeButtons.get(0));

		//add(nodeTree, CENTER);
		//setCellHeight(nodeTree, "100%");
		
		
		
		StackPanel nodeStack = new StackPanel();
		
		nodeStack.add(myTree, "Myself");		
		nodeStack.add(community, "Everyone");		
		nodeStack.add(patternTree, "Patterns");
		
		add(nodeStack, CENTER);
		
		nodeStack.setSize("100%", "100%");
		
		setCellHeight(nodeStack, "100%");
		
		
		

		setWidth("100%");



	}

	public void selectNode(String nodeID) {
		if (nodeID!=null) {
			if (selectedNode!=nodeID) {
				selectedNode = nodeID;
				onNodeSelected(selectedNode);
			}
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
