package automenta.netention.gwtdepr.ui.detail.property;

import java.util.ArrayList;
import java.util.List;

import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.value.PropertyValue;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;

abstract public class OptionPropertyPanel extends PropertyPanel {

	private ListBox typeSelect;

	private FlowPanel editPanel;
	private PropertyValue value;

	private List<PropertyOption> options = new ArrayList();

	private PropertyOption currentOption;

	public OptionPropertyPanel(String property) {
		super(property);
		


	}

	@Override protected void initPropertyPanel() {
		super.initPropertyPanel();
		
		initOptions(options);

		typeSelect = new ListBox();
		typeSelect.addStyleName("PropertySelect");
		for (PropertyOption po : options) {
			typeSelect.addItem(po.getName());			
		}
		typeSelect.addChangeHandler(new ChangeHandler() {
			@Override public void onChange(ChangeEvent event) {
				int x = typeSelect.getSelectedIndex();
				
				PropertyOption po = options.get(x);
				setCurrentOption(po);

				setValue(po.newDefaultValue());

				Panel p = po.newEditPanel(value);
				
				editPanel.clear();
				editPanel.add(p);
			}			
		});
		add(typeSelect);
		
		editPanel = new FlowPanel();
		add(editPanel);

		valueToWidget();

	}
	

	public OptionPropertyPanel(String property, PropertyValue v) {
		this(property);
		this.value = v;
	}

	abstract protected void initOptions(List<PropertyOption> options);

	/** load */
	private void valueToWidget() {
		if (value==null)
			return;

		for (int i = 0; i < options.size(); i++) {
			PropertyOption po = options.get(i);
			if (po.accepts(value)) {

				typeSelect.setSelectedIndex(i);
				
				setCurrentOption(po);
				
				Panel p = po.newEditPanel(value);
				editPanel.clear();
				editPanel.add(p);
				
				return;
			}
		}
	}

	private void setCurrentOption(PropertyOption po) {
		this.currentOption = po;		
	}

	protected void setValue(PropertyValue newValue) {
		PropertyValue oldValue = this.value;
		this.value = newValue;
		
		this.value.setProperty(getProperty());
		
		//TODO replace old with new value, at original index
		if (getNode()!=null) {
			if (oldValue!=newValue) {
				synchronized (getNode().getProperties()) {
					getNode().getProperties().remove(oldValue);
					getNode().getProperties().add(newValue);
				}
			}
		}
		
	}

	@Override
	public void setNode(DetailData node) {
		super.setNode(node);
		setValue(getValue());
	}
	
	/** save */
	@Override public void widgetToValue() {
		if (currentOption!=null) {
			//causes value to be updated by data presently in the widgets

			setValue(currentOption.getValue());
		}
	}

	public PropertyValue getValue() {
		return value;
	}
	
}
