package automenta.netention.value.real;

import automenta.netention.value.Unit;

public class TimePointVar extends RealVar {

	public TimePointVar() {
		super();
	}
	
	public TimePointVar(String id, String name) {
		super(id, name, Unit.TimePoint);
	}

}
