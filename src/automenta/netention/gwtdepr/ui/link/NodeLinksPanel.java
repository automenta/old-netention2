package automenta.netention.gwtdepr.ui.link;

import java.util.List;

import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.AgentData;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.gwtdepr.data.DetailLinkData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;


public class NodeLinksPanel extends FlowPanel {

	final NetworkServiceAsync netService = GWT.create(NetworkService.class);

	private FlexTable linkTable;

	private DetailData pd;

	private AgentData agent;

	public NodeLinksPanel(AgentData agent, final DetailData pd) {
		super();
		
		addStyleName("NodeLinksPanel");
		
		this.agent = agent;
		this.pd = pd;

		linkTable = new FlexTable();
		add(linkTable);
		linkTable.setWidth("100%");
		linkTable.setBorderWidth(1);

		refresh();
		
		
	}

	protected void refresh() {
		netService.getMessages(pd.getCreator(), pd.getID(), new AsyncCallback<List<DetailLinkData>>() {			
			@Override public void onSuccess(List<DetailLinkData> result) {				
				linkTable.clear();
				
				if (result.size() > 0) {					
					for (DetailLinkData dld : result) {
						addLink(dld);
					}					
				}
				else {
					add(new Label("No links."));
				}
			}
			
			@Override public void onFailure(Throwable caught) { 	Window.alert(caught.toString());	}
		});
		
		
	}

	protected void addLink(DetailLinkData dld) {
		int nextRow = linkTable.getRowCount() + 1;
		
		linkTable.setWidget(nextRow, 0, new Label(dld.getWhenCreated().toString()));
		linkTable.setWidget(nextRow, 1, new Label(Double.toString(dld.getStrength())));
		linkTable.setWidget(nextRow, 2, new Label(dld.getFromNode().getName() + " -> " + dld.getToNode().getName()));
		linkTable.setWidget(nextRow, 3, new Label(dld.getFromNode().getCreator() + " -> " + dld.getToNode().getCreator()));
		
	}
}
