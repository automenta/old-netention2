package automenta.netention.server.value.integer;

import automenta.netention.server.value.PropertyValue;

public class IntegerEquals extends PropertyValue {

	private int value;

	public IntegerEquals() {
		super();
	}
	
	public IntegerEquals(int i) {
		super();		
		this.value = i;
	}
	
	public void setValue(Integer i) {
		this.value = i;		
	}

	public int getValue() {
		return value;
	}
	
	

}
