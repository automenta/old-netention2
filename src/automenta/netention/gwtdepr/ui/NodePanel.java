package automenta.netention.gwtdepr.ui;

import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.AgentData;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.gwtdepr.data.NodeData;
import automenta.netention.gwtdepr.data.PatternData;
import automenta.netention.gwtdepr.data.NodeData.NodeHandler;
import automenta.netention.gwtdepr.ui.detail.NodeEditPanel;
import automenta.netention.gwtdepr.ui.link.NodeLinksPanel;
import automenta.netention.gwtdepr.ui.pattern.PatternPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;

public class NodePanel extends DockPanel {

	private TextBox nameEdit;

	private ToggleButton editButton;
	private ToggleButton linksButton;

	private NodeEditPanel nodeEditPanel;
	private NodeLinksPanel nodeLinksPanel;

	private DetailData node;
	private AgentData agent;

	public static enum NodePanelMode {
		Edit, Links
	}

	final NetworkServiceAsync netService = GWT.create(NetworkService.class);

	public NodePanel(AgentData agentData) {
		super();
		this.agent = agentData;

		addStyleName("NodePanel");


	}

	public void setNode(String nodeID, final NodePanelMode mode, final NodeHandler... nodeHandlers) {
		netService.getNodeData(nodeID, new AsyncCallback<NodeData>() {	
			@Override public void onSuccess(NodeData node) {
				clear();

				if (node instanceof PatternData) {
					add(new PatternPanel((PatternData)node), CENTER);
				}
				else if (node instanceof DetailData) {
					DetailData n = (DetailData) node;
					NodePanel.this.node = n;
					
					for (NodeHandler nh : nodeHandlers) {
						n.addHandler(nh);
					}

					node = n;

					DockPanel topPanel = new DockPanel();
					topPanel.addStyleName("NodePanelTop");
					add(topPanel, NORTH);
					topPanel.setCellHeight(topPanel, "0%");


					nameEdit = new TextBox();
					nameEdit.setText(n.getName());
					nameEdit.addStyleName("NameEdit");
					topPanel.add(nameEdit, DockPanel.WEST);


					nodeEditPanel = new NodeEditPanel(n) {
						@Override protected void onDeleteNode() {
							deleteNode();
						}
						@Override protected void onSaveNode() {
							saveNode();
						}
					};	
					nodeEditPanel.setHeight("100%");

					nodeLinksPanel = new NodeLinksPanel(getAgent(), n);
					nodeLinksPanel.setHeight("100%");

					editButton = new ToggleButton("Edit");
					editButton.addStyleName("NodePanelViewButton");
					linksButton = new ToggleButton("Links");
					linksButton.addStyleName("NodePanelViewButton");

					ClickHandler handler = new ClickHandler() {
						@Override  public void onClick(ClickEvent event) {
							if (event.getSource() == editButton) {
								setEdit();
							}
							else if (event.getSource() == linksButton) {
								setLinks();

							}																		
						}

					};

					editButton.addClickHandler(handler);
					linksButton.addClickHandler(handler);


					Panel buttonPanel = new HorizontalPanel();
					buttonPanel.add(editButton);
					buttonPanel.add(linksButton);


					topPanel.add(buttonPanel, DockPanel.EAST);

					topPanel.setCellHorizontalAlignment(buttonPanel, DockPanel.ALIGN_RIGHT);
					topPanel.setCellVerticalAlignment(buttonPanel, DockPanel.ALIGN_BOTTOM);

					setEdit();
				}

			}

			@Override public void onFailure(Throwable caught) { 	}

		});

	}

	private void setLinks() {
		editButton.setDown(false);
		linksButton.setDown(true);

		if (!getChildren().contains(nodeLinksPanel)) {								
			remove(nodeEditPanel);
			remove(nodeLinksPanel);
			add(nodeLinksPanel, CENTER);
			setCellHeight(nodeLinksPanel, "100%");
			nameEdit.setReadOnly(true);
		}
	}

	private void setEdit() {
		linksButton.setDown(false);
		editButton.setDown(true);

		if (!getChildren().contains(nodeEditPanel)) {
			remove(nodeEditPanel);
			remove(nodeLinksPanel);
			add(nodeEditPanel, CENTER);
			setCellHeight(nodeEditPanel, "100%");
			nameEdit.setReadOnly(false);
		}
	}					

	public void widgetsToNode() {
		nodeEditPanel.widgetsToValue();
		getNode().setName(nameEdit.getText());
	}

	protected void saveNode() {
		widgetsToNode();

		netService.setDetail(getNode(), new AsyncCallback<DetailData>() {
			@Override public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
			}

			@Override public void onSuccess(DetailData result) {
				afterNodeSaved();
			}
		});
	}


	public DetailData getNode() {
		return node;
	}

	public AgentData getAgent() {
		return agent;
	}

	protected void deleteNode() {
		if (Window.confirm("Delete \"" + getNode().getName() + "\"?")) {
			netService.deleteDetail(getAgent().getID(), getNode().getID(), new AsyncCallback<Boolean>() {
				@Override public void onFailure(Throwable caught) {
					Window.alert("Unable to delete:\n" + caught);					
				}
				@Override public void onSuccess(Boolean result) {
					afterNodeDeleted();
				}				
			});
		}
	}

	protected void afterNodeSaved() { }
	protected void afterNodeDeleted() { }

}
