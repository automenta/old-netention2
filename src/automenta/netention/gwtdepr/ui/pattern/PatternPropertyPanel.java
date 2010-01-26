/**
 * 
 */
package automenta.netention.gwtdepr.ui.pattern;

import automenta.netention.api.value.Property;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class PatternPropertyPanel extends FlowPanel {

	public PatternPropertyPanel(Property pd, boolean isDefined) {
		super();
		String x = pd.getID() + " - " + pd.getName() + " - " + pd.getDescription();
		
		Label l = new Label(x);
		add(l);
		
		addStyleName("PropertyPanel");
		
		if (!isDefined)
			addStyleName("PropertyPanelInherited");
		
		
	}
	
}