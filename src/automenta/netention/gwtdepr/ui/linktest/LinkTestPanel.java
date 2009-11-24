package automenta.netention.gwtdepr.ui.linktest;

import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.gwtdepr.data.DetailLinkData;
import automenta.netention.gwtdepr.ui.detail.NodeEditPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LinkTestPanel extends DockPanel {

	final NetworkServiceAsync netService = GWT.create(NetworkService.class);

	DetailData a, b;

	private NodeEditPanel aPanel;

	private TestNodeEditPanel bPanel;

	private LinkViewPanel linkPanel;

	private HorizontalPanel centerPanel;

	public class TestNodeEditPanel extends NodeEditPanel {
		
		private DetailData d;

		public TestNodeEditPanel(DetailData d) {
			super(d);
			this.d = d;
			setWidth("100%");
		}

		@Override protected void onDeleteNode() {
			//no action
		}
		
		@Override protected void onSaveNode() {
			widgetsToValue();
			netService.setDetail(getNode(), new AsyncCallback<DetailData>() {

				@Override public void onFailure(Throwable caught) {
					Window.alert(caught.toString());					
				}

				@Override public void onSuccess(DetailData result) {
					updateLink();								
				}
			});
		}
	
	}
	
	public class LinkViewPanel extends VerticalPanel {
		
		public void update(DetailLinkData link) {
			clear();

			setHeight("0%");
			setVerticalAlignment(ALIGN_BOTTOM);
			
			add(new Label(link.getWhenCreated().toString()));			

			add(new Label(Double.toString(link.getStrength())));			
		}
	}
	
	public LinkTestPanel(final String agentID) {
		super();
		
		setWidth("100%");
		setHeight("100%");
		
		centerPanel = new HorizontalPanel();
		centerPanel.setWidth("100%");
		centerPanel.setHeight("100%");
		add(centerPanel, CENTER);
		
		netService.getNewDetail(agentID, new AsyncCallback<DetailData>() {			
			@Override public void onSuccess(DetailData result) {
				a = result;
				
				netService.getNewDetail(agentID, new AsyncCallback<DetailData>() {
					@ Override public void onSuccess(DetailData result) {
						b = result;					
						updateDetails();
					}
					@Override public void onFailure(Throwable caught) {		Window.alert(caught.toString());	}
				});
			}
			
			@Override public void onFailure(Throwable caught) {		Window.alert(caught.toString());	}
		});
		
		//rightNode = new NodeEditPanel(d2);
		
	}
	
	protected void updateLink() {
		netService.getLinkData(a.getID(), b.getID(), new AsyncCallback<DetailLinkData>() {
			@Override public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
			}
			
			@Override public void onSuccess(DetailLinkData result) {
				linkPanel.update(result);
			}
		});
	}
	
	/** update node edit panels and link details */
	protected void updateDetails() {
		if (aPanel == null) {
			aPanel = new TestNodeEditPanel(a);
			centerPanel.add(aPanel);
			centerPanel.setCellWidth(aPanel, "50%");
			bPanel = new TestNodeEditPanel(b);
			centerPanel.add(bPanel);
			centerPanel.setCellWidth(bPanel, "50%");
			linkPanel = new LinkViewPanel();
			add(linkPanel, SOUTH);
		}
		updateLink();
	}
}
