/**
 * 
 */
package automenta.netention.gwtdepr.ui.detail.property;

import java.util.Arrays;

import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.api.value.Property;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

abstract public class PropertyPanel extends HorizontalPanel { 
	//data to widgets: get(property) -> widgets
	//widgets to data: widgets -> set(property, value)

	private NetworkServiceAsync netService = GWT.create(NetworkService.class);

	private String property;

	private Label propLabel;

	private FlowPanel nameHolder;

	private Property propertyData;

	private DetailData node;

	public static class PropertyTextBox extends TextBox {
		
		public PropertyTextBox() {
			super();
			addStyleName("PropertyTextBox");
		}
		
	}

	public PropertyPanel(String property) {
		super();

		this.property = property;

		propLabel = new Label();

		addStyleName("PropertyPanel");


		nameHolder = new FlowPanel();
		add(nameHolder);
		
		netService.getPropertyData(new String[] { property }, new AsyncCallback<Property[]>() {
			

			@Override public void onFailure(Throwable caught) {
				add(new Label(caught.toString() + Arrays.asList(caught.getStackTrace())));				
			}
			
			@Override public void onSuccess(Property[] result) {

				propertyData = result[0];
				
				initPropertyPanel();

			}
		});

	}

	/** called after getPropertyData() becomes available */
	protected void initPropertyPanel() {
		String name = getPropertyData().getName();

		propLabel.setText(name);
		propLabel.addStyleName("PropertyLabel");

		nameHolder.add(propLabel);		
	}

	public Property getPropertyData() {
		return propertyData;
	}
	

	protected String getProperty() {
		return property;
	}


	protected void setIs() {
		//getElement().getParentElement().s
		removeStyleName("PropertyWillBe");
		addStyleName("PropertyIs");
	}

	protected void setWillBe() {
		removeStyleName("PropertyIs");
		addStyleName("PropertyWillBe");
	}


	public Label getPropertyLabel() {
		return propLabel;
	}

	abstract public void widgetToValue();

	public void setNode(DetailData node) {
		this.node = node;		
	}
	public DetailData getNode() {
		return node;
	}
	


}