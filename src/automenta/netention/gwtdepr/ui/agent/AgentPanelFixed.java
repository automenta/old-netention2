package automenta.netention.gwtdepr.ui.agent;

import automenta.netention.gwtdepr.data.AgentData;

import com.google.gwt.user.client.ui.HorizontalPanel;

public class AgentPanelFixed extends AgentPanel {

	protected void refresh(AgentData agentData) {		
		clear();
		
		HorizontalPanel d = new HorizontalPanel();
		
		headerPanel = new AgentHeaderPanel(agentData);		
		add(headerPanel, NORTH);
		setCellHeight(headerPanel, "0%");
		
		add(d, CENTER);
		setCellHeight(d, "98%");
		
		d.setWidth("100%");
		
		nodePanel = newNodePanel(agentData);
		nodeListPanel = newListPanel(agentData);

		d.add(nodeListPanel);
		d.setCellWidth(nodeListPanel, "25%");
		d.add(nodePanel);
		d.setCellWidth(nodePanel, "75%");
		

	}

}
