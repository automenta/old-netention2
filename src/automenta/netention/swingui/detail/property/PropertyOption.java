/**
 * 
 */
package automenta.netention.swingui.detail.property;

import automenta.netention.api.value.PropertyValue;
import automenta.netention.api.value.Value;

import com.google.gwt.user.client.ui.Panel;

abstract public class PropertyOption<V extends PropertyValue> {
	
	private String name;
	private V value;

	public PropertyOption(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public V getValue() {
		return widgetToValue(value);
	}
	
	
	public void setValue(V newValue) {
		this.value = newValue;	
	}
	
	abstract public V widgetToValue(V value);
	
	/** note: does not setProperty(), caller's responsible for setProperty() otherwise it will be null */
	abstract public V newDefaultValue();

	abstract public Panel newEditPanel(V value);

	abstract public boolean accepts(Value v);
	
	//abstract public Panel newReadPanel();		
}