package automenta.netention.gwtdepr.ui;

import automenta.netention.gwtdepr.NetworkServiceAsync;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;

public class NetworkPanel extends TabPanel {

	public NetworkPanel(NetworkServiceAsync netService) {
		super();
		
		
		final Panel agentsPanel = new NetworkAgentsPanel();
		final Panel patternsPanel = new NetworkPatternsPanel(netService);
		
		add(agentsPanel, "Agents");
		add(patternsPanel, "Patterns");
		selectTab(0);

		setSize("100%", "100%");
	}
	
}
