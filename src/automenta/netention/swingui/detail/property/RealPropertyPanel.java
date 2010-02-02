/**
 * 
 */
package automenta.netention.swingui.detail.property;

import automenta.netention.node.Detail;
import automenta.netention.Schema;
import java.util.List;

import automenta.netention.value.PropertyValue;
import automenta.netention.value.Unit;
import automenta.netention.value.Value;
import automenta.netention.value.real.RealBetween;
import automenta.netention.value.real.RealEquals;
import automenta.netention.value.real.RealIs;
import automenta.netention.value.real.RealLessThan;
import automenta.netention.value.real.RealMoreThan;
import automenta.netention.value.real.RealVar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RealPropertyPanel extends OptionPropertyPanel {	

	private JLabel unitLabel;

	
	public RealPropertyPanel(Schema s, Detail d, String property, PropertyValue v) {
		super(s, d, property, v);
	}


	@Override protected void initOptions(List<PropertyOption> options) {
		options.add(new PropertyOption<RealIs>("is") {			
			private JTextField isBox;

			@Override public JPanel newEditPanel(RealIs v) {
				setValue(v);
				setIs();
				JPanel p =  new TransparentFlowPanel();
				isBox = new JTextField(Double.toString(v.getValue()));
				p.add(isBox);
				return p;
			}

			@Override public RealIs widgetToValue(RealIs r) {
				r.setValue(Double.valueOf( isBox.getText() ) );
				return r;
			}			

			@Override public boolean accepts(Value v) {		return v.getClass().equals(RealIs.class);			}
			
			@Override public RealIs newDefaultValue() {
				return new RealIs(0);
			}
		});

		options.add(new PropertyOption<RealEquals>("will equal") {			
			private JTextField equalsBox;

			@Override public JPanel newEditPanel(RealEquals v) {
				setValue(v);
				setWillBe();

				JPanel p =  new TransparentFlowPanel();
				equalsBox = new JTextField(Double.toString(v.getValue()));
				p.add(equalsBox);
				return p;
			}

			@Override public boolean accepts(Value v) {		return v.getClass().equals(RealEquals.class);			}

			@Override public RealEquals widgetToValue(RealEquals r) {
				r.setValue(Double.valueOf( equalsBox.getText() ) );
				return r;
			}

			@Override public RealEquals newDefaultValue() {
				return new RealEquals(0);
			}
		});

		options.add(new PropertyOption<RealMoreThan>("will be greater than") {			
			private JTextField moreThanBox;

			@Override public JPanel newEditPanel(RealMoreThan v) {
				setValue(v);
				setWillBe();

				JPanel p =  new TransparentFlowPanel();
				moreThanBox = new JTextField(Double.toString(v.getValue()));
				p.add(moreThanBox);
				return p;
			}

			@Override public RealMoreThan widgetToValue(RealMoreThan r) {
				r.setValue(Double.valueOf( moreThanBox.getText() ) );
				return r;
			}

			@Override public boolean accepts(Value v) {		return v.getClass().equals(RealMoreThan.class);			}
			
			@Override public RealMoreThan newDefaultValue() {
				return new RealMoreThan(0);
			}
		});

		options.add(new PropertyOption<RealLessThan>("will be less than") {			
			private JTextField lessThanBox;

			@Override public JPanel newEditPanel(RealLessThan v) {
				setValue(v);
				setWillBe();

				JPanel p =  new TransparentFlowPanel();
				lessThanBox = new JTextField(Double.toString(v.getValue()));
				p.add(lessThanBox);
				return p;
			}

			@Override
			public RealLessThan widgetToValue(RealLessThan r) {
				r.setValue(Double.valueOf( lessThanBox.getText() ) );
				return r;
			}
			
			@Override public boolean accepts(Value v) {		return v.getClass().equals(RealLessThan.class);			}
			
			@Override public RealLessThan newDefaultValue() {
				return new RealLessThan(0);
			}
		});

		options.add(new PropertyOption<RealBetween>("will be between") {			
			private JTextField minBox;
			private JTextField maxBox;

			//TODO add inclusive checkbox
			
			@Override public JPanel newEditPanel(RealBetween v) {
				setValue(v);
				setWillBe();
				
				JPanel p =  new TransparentFlowPanel();
				minBox = new JTextField(Double.toString(v.getMin()));
				p.add(minBox);

                JLabel l = new JLabel(" and ");
				p.add(l);
				
				maxBox = new JTextField(Double.toString(v.getMax()));
				p.add(maxBox);

				return p;
			}

			@Override
			public RealBetween widgetToValue(RealBetween r) {
				r.setMin( Double.parseDouble( minBox.getText() ) );
				r.setMax( Double.parseDouble( maxBox.getText() ) );
				return r;
			}
			
			@Override public boolean accepts(Value v) {	return v.getClass().equals(RealBetween.class);		}
						
			@Override public RealBetween newDefaultValue() {
				return new RealBetween(0, 1, true);
			}
		});
		
	}
	

	private String getUnitText(Unit unit) {
		if (unit == Unit.Distance)	return "meters";
		if (unit == Unit.Mass)	return "kilograms";
		if (unit == Unit.Speed)	return "meters/second";
		if (unit == Unit.Volume)	return "cm^3";
		if (unit == Unit.TimeDuration)	return "seconds";
		if (unit == Unit.Currency)	return "dollars";		
		return "";
	}

	@Override
	protected void initPropertyPanel() {
		super.initPropertyPanel();

		unitLabel = new JLabel();

		if (getProperty()!=null) {
			if (getProperty() instanceof RealVar) {
				RealVar rv = (RealVar) getProperty();
				Unit unit = rv.getUnit();
				if (unit!=null)
					unitLabel.setText(getUnitText(unit));
			}
		}

		add(unitLabel);

	}
	


	
}