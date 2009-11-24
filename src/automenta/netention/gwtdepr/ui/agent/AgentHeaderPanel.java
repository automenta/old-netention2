/**
 * 
 */
package automenta.netention.gwtdepr.ui.agent;

import automenta.netention.gwtdepr.data.AgentData;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class AgentHeaderPanel extends HorizontalPanel {
	
	public AgentHeaderPanel(AgentData agent) {
		super();
		
		addStyleName("AgentHeaderPanel");
		
		HorizontalPanel avatarPanel = new HorizontalPanel();
		
		Image photo = new Image("public/default_photo.png");
		photo.addStyleName("AgentHeaderPanelImage");
		avatarPanel.add(photo);
		
		Label nameLabel = new Label(agent.getName());
		nameLabel.addStyleName("AgentHeaderNameLabel");
		avatarPanel.add(nameLabel);
		avatarPanel.setCellVerticalAlignment(nameLabel, ALIGN_MIDDLE);
		
	

		add(avatarPanel);
	}
	
}