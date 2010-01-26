package automenta.netention.api.value.real;

import automenta.netention.api.value.PropertyValue;

public class RealEquals extends PropertyValue {

	protected double value;

	public RealEquals() {
		super();
	}

	public RealEquals(double value) {
		this();
		this.value = value;
	}


	public double getValue() {
		return value;
	}

	public void setValue(Double v) {
		this.value = v;
	}

	
}
