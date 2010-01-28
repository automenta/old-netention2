package automenta.netention.swingui.detail.property;

import automenta.netention.api.Detail;
import automenta.netention.api.Schema;
import java.util.List;

import automenta.netention.api.value.PropertyValue;
import automenta.netention.api.value.Value;
import automenta.netention.api.value.string.StringContains;
import automenta.netention.api.value.string.StringEquals;
import automenta.netention.api.value.string.StringIs;
import automenta.netention.api.value.string.StringNotContains;
import automenta.netention.api.value.string.StringVar;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StringPropertyPanel extends OptionPropertyPanel {

	public StringPropertyPanel(Schema s, Detail d, String prop, PropertyValue v) {
		super(s, d, prop, v);
	}

	@Override protected void initOptions(List<PropertyOption> options) {

		options.add(new PropertyOption<StringIs>("is") {

			//private SuggestBox isBox;
			//private RichTextArea rta;
            JTextField rta;

			@Override public boolean accepts(Value v) { 	return v.getClass().equals(StringIs.class);		}

			@Override public StringIs newDefaultValue() {
				return new StringIs("");
			}

			@Override public StringIs widgetToValue(StringIs r) {
                r.setValue(rta.getText());
//				if (rta !=null) {
//					r.setValue( rta.getText() );
//				}
//				else {
//					r.setValue( isBox.getText() );
//				}
				return r;
			}

			@Override public JPanel newEditPanel(StringIs value) {

				setIs();

				JPanel p = new JPanel(new FlowLayout());
                rta = new JTextField(value.getString());
                p.add(rta);


				//StringVar sv = (StringVar) getPropertyData();

//				if (sv.isRich()) {
//					rta = new RichTextArea();
//					rta.setText(value.getValue());
//					p.add(rta);
//				}
//				else {
//					MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
//
//					if (sv.getExampleValues()!=null) {
//						oracle.setDefaultSuggestionsFromText(sv.getExampleValues());
//					}
//
//					isBox = new SuggestBox(oracle);
//
//					isBox.setText(value.getValue());
//					p.add(isBox);

//				}

				return p;
			}

		});

		options.add(new PropertyOption<StringEquals>("will equal") {

			//private TextBox eqBox;
            private JTextField eqBox;

			@Override public boolean accepts(Value v) { 	return v.getClass().equals(StringEquals.class);		}


			@Override public StringEquals widgetToValue(StringEquals r) {
				r.setValue( eqBox.getText() );
				return r;
			}

			@Override public StringEquals newDefaultValue() {
				return new StringEquals("");
			}

			@Override public JPanel newEditPanel(StringEquals value) {
				setValue(value);
				setWillBe();

				JPanel p = new JPanel(new FlowLayout());
				eqBox = new JTextField();
				eqBox.setText(value.getString());
				p.add(eqBox);

				return p;
			}

		});

		options.add(new PropertyOption<StringContains>("will contain") {

			private JTextField eqBox;

			@Override public boolean accepts(Value v) { 	return v.getClass().equals(StringContains.class);		}

			@Override public StringContains newDefaultValue() {
				return new StringContains("");
			}

			@Override public StringContains widgetToValue(StringContains r) {
				r.setValue( eqBox.getText() );
				return r;
			}

			@Override public JPanel newEditPanel(StringContains value) {
				setValue(value);
				setWillBe();

				JPanel p = new JPanel(new FlowLayout());
				eqBox = new JTextField();
				eqBox.setText(value.getString());
				p.add(eqBox);

				return p;
			}

		});

		options.add(new PropertyOption<StringNotContains>("will not contain") {

			private JTextField eqBox;

			@Override public boolean accepts(Value v) { 	return v.getClass().equals(StringNotContains.class);		}

			@Override public StringNotContains widgetToValue(StringNotContains r) {
				r.setValue( eqBox.getText() );
				return r;
			}

			@Override public StringNotContains newDefaultValue() {
				return new StringNotContains("");
			}

			@Override public JPanel newEditPanel(StringNotContains value) {
				setValue(value);
				setWillBe();

				JPanel p = new JPanel(new FlowLayout());
				eqBox = new JTextField();
				eqBox.setText(value.getString());
				p.add(eqBox);

				return p;
			}

		});

	}


}
