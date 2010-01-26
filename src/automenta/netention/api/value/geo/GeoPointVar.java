package automenta.netention.api.value.geo;

import automenta.netention.api.value.Property;
import automenta.netention.api.value.PropertyValue;
import automenta.netention.api.value.real.RealIs;

public class GeoPointVar extends Property {

	public GeoPointVar() {
		super();
	}
	
	public GeoPointVar(String id, String name) {
		super(id, name);
	}

	@Override
	public PropertyValue newDefaultValue() {
		//TODO GeoPointIs..
		PropertyValue pv = new RealIs(0.0);
		pv.setProperty(getID());
		return pv;
	}
	
}
