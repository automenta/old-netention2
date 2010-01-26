package automenta.netention.api.value.geo;

import automenta.netention.api.value.DefiniteValue;
import automenta.netention.api.value.IndefiniteValue;
import automenta.netention.api.value.PropertyValue;

public class GeoPointIs extends PropertyValue implements DefiniteValue /* ... */ {

	@Override public Object getValue() {
		return null;
	}

	@Override public double satisfies(IndefiniteValue i) {
		//TODO impl GeoPoint satisfy
		return 0;
	}
	
}
