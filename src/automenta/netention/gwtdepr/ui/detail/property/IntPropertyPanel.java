/**
 * 
 */
package automenta.netention.gwtdepr.ui.detail.property;

import java.util.List;

import automenta.netention.server.value.PropertyValue;
import automenta.netention.server.value.Value;
import automenta.netention.server.value.integer.IntegerEquals;
import automenta.netention.server.value.integer.IntegerIs;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

public class IntPropertyPanel extends OptionPropertyPanel {	

	private Label unitLabel;

	
	public IntPropertyPanel(String property) {
		super(property);		

	}

	public IntPropertyPanel(String property, PropertyValue v) {
		super(property, v);
	}


	@Override protected void initOptions(List<PropertyOption> options) {
		options.add(new PropertyOption<IntegerIs>("is") {			
			private TextBox isBox;

			@Override public Panel newEditPanel(IntegerIs v) {
				setValue(v);
				setIs();
				
				Panel p = new FlowPanel();
				isBox = new PropertyTextBox();
				isBox.setText( Integer.toString(v.getValue()) );
				p.add(isBox);
				return p;
			}

			@Override public IntegerIs widgetToValue(IntegerIs r) {
				r.setValue(Integer.valueOf( isBox.getText() ) );
				return r;
			}			

			@Override public boolean accepts(Value v) {		return v.getClass().equals(IntegerIs.class);			}
			
			@Override public IntegerIs newDefaultValue() {
				return new IntegerIs(0);
			}
		});

		options.add(new PropertyOption<IntegerEquals>("will equal") {			
			private TextBox equalsBox;

			@Override public Panel newEditPanel(IntegerEquals v) {
				setValue(v);
				setWillBe();

				Panel p = new FlowPanel();
				equalsBox = new PropertyTextBox();
				equalsBox.setText( Integer.toString(v.getValue()) );
				p.add(equalsBox);
				return p;
			}

			@Override public boolean accepts(Value v) {		return v.getClass().equals(IntegerEquals.class);			}

			@Override public IntegerEquals widgetToValue(IntegerEquals r) {
				r.setValue(Integer.valueOf( equalsBox.getText() ) );
				return r;
			}

			@Override public IntegerEquals newDefaultValue() {
				return new IntegerEquals(0);
			}
		});

//		options.add(new PropertyOption<RealMoreThan>("will be greater than") {			
//			private TextBox moreThanBox;
//
//			@Override public Panel newEditPanel(RealMoreThan v) {
//				setValue(v);
//				setWillBe();
//
//				Panel p = new FlowPanel();
//				moreThanBox = new PropertyTextBox();
//				moreThanBox.setText( Double.toString(v.getValue()) );
//				p.add(moreThanBox);
//				return p;
//			}
//
//			@Override public RealMoreThan widgetToValue(RealMoreThan r) {
//				r.setValue(Double.valueOf( moreThanBox.getText() ) );
//				return r;
//			}
//
//			@Override public boolean accepts(Value v) {		return v.getClass().equals(RealMoreThan.class);			}
//
//			
//			@Override public RealMoreThan newDefaultValue() {
//				return new RealMoreThan(0);
//			}
//		});
//
//		options.add(new PropertyOption<RealLessThan>("will be less than") {			
//			private TextBox lessThanBox;
//
//			@Override public Panel newEditPanel(RealLessThan v) {
//				setValue(v);
//				setWillBe();
//
//				Panel p = new FlowPanel();
//				lessThanBox = new PropertyTextBox();
//				lessThanBox.setText( Double.toString(v.getValue()) );
//				p.add(lessThanBox);
//				return p;
//			}
//
//			@Override
//			public RealLessThan widgetToValue(RealLessThan r) {
//				r.setValue(Double.valueOf( lessThanBox.getText() ) );
//				return r;
//			}
//			
//			@Override public boolean accepts(Value v) {		return v.getClass().equals(RealLessThan.class);			}
//			
//			@Override public RealLessThan newDefaultValue() {
//				return new RealLessThan(0);
//			}
//		});
//
//		options.add(new PropertyOption<RealBetween>("will be between") {			
//			private PropertyTextBox minBox;
//			private PropertyTextBox maxBox;
//
//			@Override public Panel newEditPanel(RealBetween v) {
//				setValue(v);
//				setWillBe();
//				
//				Panel p = new FlowPanel();
//				minBox = new PropertyTextBox();
//				minBox.setText( Double.toString(v.getValue()) );
//				p.add(minBox);
//
//				p.add(new Label(" and "));
//				
//				maxBox = new PropertyTextBox();
//				maxBox.setText( Double.toString(v.getValue()) );
//				p.add(maxBox);
//
//				return p;
//			}
//
//			@Override
//			public RealBetween widgetToValue(RealBetween r) {
//				//...
//				return r;
//			}
//			
//			@Override public boolean accepts(Value v) {	return v.getClass().equals(RealBetween.class);		}
//						
//			@Override public RealBetween newDefaultValue() {
//				return new RealBetween(0, 0);
//			}
//		});
		
	}
	


	
}