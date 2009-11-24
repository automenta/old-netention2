package automenta.netention.gwtdepr.ui.agent;

import automenta.netention.gwtdepr.data.AgentData;

import com.google.gwt.user.client.ui.HorizontalSplitPanel;

public class AgentPanelSplit extends AgentPanel {

	private HorizontalSplitPanel split;

	protected void refresh(AgentData agentData) {		
		clear();
		
		headerPanel = new AgentHeaderPanel(agentData);		
		add(headerPanel, NORTH);
		setCellHeight(headerPanel, "0%");
		
		split = new HorizontalSplitPanel();
		add(split, CENTER);
		setCellHeight(split, "98%");
		
		split.setWidth("100%");
		
		nodePanel = newNodePanel(agentData);
		nodeListPanel = newListPanel(agentData);

		split.setLeftWidget(nodeListPanel);
		split.setRightWidget(nodePanel);
		split.setSplitPosition("25%");

	}

}
