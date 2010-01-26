package automenta.netention.api.value.real;

import automenta.netention.api.value.Unit;

public class TimePointVar extends RealVar {

	public TimePointVar() {
		super();
	}
	
	public TimePointVar(String id, String name) {
		super(id, name, Unit.TimePoint);
	}

}
