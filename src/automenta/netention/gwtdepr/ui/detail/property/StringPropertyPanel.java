package automenta.netention.gwtdepr.ui.detail.property;

import java.util.List;

import automenta.netention.server.value.PropertyValue;
import automenta.netention.server.value.Value;
import automenta.netention.server.value.string.StringContains;
import automenta.netention.server.value.string.StringEquals;
import automenta.netention.server.value.string.StringIs;
import automenta.netention.server.value.string.StringNotContains;
import automenta.netention.server.value.string.StringVar;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;

public class StringPropertyPanel extends OptionPropertyPanel {

	public StringPropertyPanel(String property) {
		super(property);		
	}
	
	public StringPropertyPanel(String prop, PropertyValue v) {
		super(prop, v);
	}
	
	@Override protected void initOptions(List<PropertyOption> options) {
		
		options.add(new PropertyOption<StringIs>("is") {

			private SuggestBox isBox;
			private RichTextArea rta;

			@Override public boolean accepts(Value v) { 	return v.getClass().equals(StringIs.class);		}

			@Override public StringIs newDefaultValue() {
				return new StringIs("");
			}

			@Override public StringIs widgetToValue(StringIs r) {
				if (rta !=null) {
					r.setValue( rta.getText() );
				}
				else {
					r.setValue( isBox.getText() );
				}
				return r;
			}
			
			@Override public Panel newEditPanel(StringIs value) {
				setValue(value);
				setIs();

				Panel p = new FlowPanel();

				
				StringVar sv = (StringVar) getPropertyData();
//				if (sv.isRich()) {
//					rta = new RichTextArea();
//					rta.setText(value.getValue());
//					p.add(rta);
//				}
//				else {
					MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();

					if (sv.getExampleValues()!=null) {
						oracle.setDefaultSuggestionsFromText(sv.getExampleValues());
					}
									
					isBox = new SuggestBox(oracle);
					
					isBox.setText(value.getValue());
					p.add(isBox);
					
//				}
				
				return p;
			}
			
		});
		
		options.add(new PropertyOption<StringEquals>("will equal") {

			private TextBox eqBox;

			@Override public boolean accepts(Value v) { 	return v.getClass().equals(StringEquals.class);		}

			
			@Override public StringEquals widgetToValue(StringEquals r) {
				r.setValue( eqBox.getText() );
				return r;
			}			

			@Override public StringEquals newDefaultValue() {
				return new StringEquals("");
			}

			@Override public Panel newEditPanel(StringEquals value) {
				setValue(value);
				setWillBe();
				
				Panel p = new FlowPanel();
				eqBox = new TextBox();
				eqBox.setText(value.getString());
				p.add(eqBox);
				
				return p;
			}
			
		});

		options.add(new PropertyOption<StringContains>("will contain") {

			private TextBox eqBox;

			@Override public boolean accepts(Value v) { 	return v.getClass().equals(StringContains.class);		}

			@Override public StringContains newDefaultValue() {
				return new StringContains("");
			}

			@Override public StringContains widgetToValue(StringContains r) {
				r.setValue( eqBox.getText() );
				return r;
			}
						
			@Override public Panel newEditPanel(StringContains value) {
				setValue(value);
				setWillBe();
				
				Panel p = new FlowPanel();
				eqBox = new TextBox();
				eqBox.setText(value.getString());
				p.add(eqBox);
				
				return p;
			}
			
		});

		options.add(new PropertyOption<StringNotContains>("will not contain") {

			private TextBox eqBox;

			@Override public boolean accepts(Value v) { 	return v.getClass().equals(StringNotContains.class);		}

			@Override public StringNotContains widgetToValue(StringNotContains r) {
				r.setValue( eqBox.getText() );
				return r;
			}

			@Override public StringNotContains newDefaultValue() {
				return new StringNotContains("");
			}

			@Override public Panel newEditPanel(StringNotContains value) {
				setValue(value);
				setWillBe();
				
				Panel p = new FlowPanel();
				eqBox = new TextBox();
				eqBox.setText(value.getString());
				p.add(eqBox);
				
				return p;
			}
			
		});

	}
	

}
