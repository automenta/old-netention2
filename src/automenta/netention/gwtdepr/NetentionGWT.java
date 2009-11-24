package automenta.netention.gwtdepr;

import automenta.netention.gwtdepr.ui.NetworkAgentsPanel;
import automenta.netention.gwtdepr.ui.linktest.LinkTestPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.RootPanel;

public class NetentionGWT implements EntryPoint {

	private final NetworkServiceAsync netService = GWT.create(NetworkService.class);

	public void onModuleLoad() {

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override public void onValueChange(ValueChangeEvent<String> event) {
				if (event.equals("linkTest")) {
					RootPanel.get().clear();
					RootPanel.get().add(new LinkTestPanel("tester"));
				}
			}
		});
		
		RootPanel.get().addStyleName("RootPanel");				
		RootPanel.get().add(new NetworkAgentsPanel());
		
		
		
		
		
		
//		Window.addResizeHandler(new ResizeHandler() {
//
//			public void onResize(ResizeEvent event) {
//				int height = event.getHeight();
//				networkPanel.setHeight(height + "px");
//			}
//
//		});

	}
}
