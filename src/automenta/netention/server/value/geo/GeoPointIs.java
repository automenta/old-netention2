package automenta.netention.server.value.geo;

import automenta.netention.server.value.DefiniteValue;
import automenta.netention.server.value.IndefiniteValue;
import automenta.netention.server.value.PropertyValue;

public class GeoPointIs extends PropertyValue implements DefiniteValue /* ... */ {

	@Override public Object getValue() {
		return null;
	}

	@Override public double satisfies(IndefiniteValue i) {
		//TODO impl GeoPoint satisfy
		return 0;
	}
	
}
