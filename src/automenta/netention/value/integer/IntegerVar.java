/**
 * 
 */
package automenta.netention.value.integer;

import automenta.netention.value.PropertyValue;
import automenta.netention.value.real.RealVar;


public class IntegerVar extends RealVar {

	public IntegerVar() {
		super();
	}
	
	public IntegerVar(String id, String name) {
		super(id, name);
	} 
	
	public boolean isInteger() { return true; }	

	@Override public PropertyValue newDefaultValue() {
		IntegerIs i = new IntegerIs(0);
		i.setProperty(getID());
		return i;
	}

}