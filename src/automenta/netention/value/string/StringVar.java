/**
 * 
 */
package automenta.netention.value.string;

import java.util.List;

import automenta.netention.value.Property;
import automenta.netention.value.PropertyValue;


public class StringVar extends Property {


	private List<String> exampleValues;
	private boolean rich;

	public StringVar() {
		super();
	}
	
	public StringVar(String id, String name) {
		super(id, name);
	}

	public StringVar(String id, String name, List<String> exampleValues) {
		this(id, name);
		this.exampleValues = exampleValues;
	}

	@Override public PropertyValue newDefaultValue() {
		PropertyValue s = new StringIs("");
		s.setProperty(getID());
		return s;
	}

	public List<String> getExampleValues() {
		return exampleValues;
	}
	
	public boolean isRich() {
		return rich;
	}
	public void setRich(boolean rich) {
		this.rich = rich;
	}

	
	
}