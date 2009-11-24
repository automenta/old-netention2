package automenta.netention.gwtdepr.ui;

import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.AgentData;
import automenta.netention.gwtdepr.ui.agent.AgentPanel;
import automenta.netention.gwtdepr.ui.agent.AgentPanelFixed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class NetworkAgentsPanel extends FlowPanel {

	public class AgentSelectPanel extends FlowPanel {
		
		public AgentSelectPanel() {
			super();
		
			
			add(new Label("Become "));
			
			addStyleName("AgentSelectPanel");
			
			final ListBox agentSelector = new ListBox();

			netService.getAgents(new AsyncCallback<String[]>() {			
				@Override public void onSuccess(String[] result) {
					for (String s : result) {
						agentSelector.addItem(s);
					}
					
					if (result.length > 0)
						agentPanel.setAgent(agentSelector.getItemText(0));

				}
				
				@Override  public void onFailure(Throwable caught) {			
				}
			});
			agentSelector.addChangeHandler(new ChangeHandler() {
				@Override public void onChange(ChangeEvent event) {
					agentPanel.setAgent(agentSelector.getItemText(agentSelector.getSelectedIndex()));
				}				
			});
			
			add(agentSelector);
			
		}

		
	}


	final private NetworkServiceAsync netService = GWT.create(NetworkService.class);

	private AgentPanel agentPanel;
	
	public NetworkAgentsPanel() {
		super();
		
		addStyleName("NetworkAgentsPanel");
		
		final AgentSelectPanel agentSelectPanel = new AgentSelectPanel();
		
		agentPanel = new AgentPanelFixed() {
			@Override protected void refresh(AgentData agentData) {
				super.refresh(agentData);
				
				getHeader().add(agentSelectPanel);
				getHeader().setCellHorizontalAlignment(agentSelectPanel, DockPanel.ALIGN_RIGHT);
				getHeader().setCellVerticalAlignment(agentSelectPanel, DockPanel.ALIGN_TOP);
			}
			
		};
		add(agentPanel);
		agentPanel.setWidth("100%");

	}
	
}
