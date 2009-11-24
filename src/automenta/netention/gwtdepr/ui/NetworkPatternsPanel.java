package automenta.netention.gwtdepr.ui;


import java.util.List;

import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.PatternData;
import automenta.netention.gwtdepr.ui.pattern.PatternPanel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;


public class NetworkPatternsPanel extends DockPanel {

	private Label messagePanel;
	private PatternPanel patternPanel;
	private NetworkServiceAsync netService;
	
	protected void error(String e) {
		messagePanel.setText(messagePanel.getText() + "\n" + e);		
	}
	
	public NetworkPatternsPanel(final NetworkServiceAsync netService) {
		super();

		this.netService = netService;
		
		messagePanel = new Label();
		add(messagePanel, SOUTH);

		final ListBox listPanel = new ListBox();
		netService.getPatterns(null, new AsyncCallback<List<PatternData>>() {
			
			@Override public void onSuccess(List<PatternData> patterns) {
				for (PatternData p : patterns) {
					listPanel.addItem(p.getID());
				}
				listPanel.setVisibleItemCount(patterns.size());
			}
			
			@Override public void onFailure(Throwable caught) {
				error(caught.toString());
			}
		});
		listPanel.addChangeHandler(new ChangeHandler() {
			@Override  public void onChange(ChangeEvent event) {
				netService.getPatternData(listPanel.getItemText(listPanel.getSelectedIndex()), new AsyncCallback<PatternData>() {
					@Override  public void onFailure(Throwable caught) {
						error(caught.toString());
					}
					
					@Override  public void onSuccess(PatternData p) {
						patternPanel.setPattern(p);
					}					
				});
			}			
		});
		listPanel.setHeight("100%");
		add(listPanel, WEST);
		
		patternPanel = new PatternPanel(null);
		patternPanel.setHeight("100%");
		
		add(patternPanel, CENTER);
		
		setHeight("100%");
		
	}

}
