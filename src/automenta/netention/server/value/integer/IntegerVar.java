/**
 * 
 */
package automenta.netention.server.value.integer;

import automenta.netention.server.value.PropertyValue;
import automenta.netention.server.value.real.RealVar;


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