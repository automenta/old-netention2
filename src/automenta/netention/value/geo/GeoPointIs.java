package automenta.netention.value.geo;

import automenta.netention.value.DefiniteValue;
import automenta.netention.value.IndefiniteValue;
import automenta.netention.value.PropertyValue;

public class GeoPointIs extends PropertyValue implements DefiniteValue /* ... */ {

	@Override public Object getValue() {
		return null;
	}

	@Override public double satisfies(IndefiniteValue i) {
		//TODO impl GeoPoint satisfy
		return 0;
	}
	
}
