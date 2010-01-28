/**
 * 
 */
package automenta.netention.swingui.detail.property;


import automenta.netention.api.Detail;
import automenta.netention.api.Schema;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.api.value.Property;

import automenta.netention.api.value.PropertyValue;
import java.awt.FlowLayout;
import java.awt.Label;
import javax.swing.JLabel;
import javax.swing.JPanel;

@Deprecated abstract public class PropertyPanel extends JPanel {
	//data to widgets: get(property) -> widgets
	//widgets to data: widgets -> set(property, value)

	private String propID;

	private Label propLabel;

	private JPanel nameHolder;

	private DetailData node;
    private final PropertyValue propValue;
    private final Property property;
    private final Schema schema;
    private final Detail detail;

	public PropertyPanel(Schema s, Detail d, String propID, PropertyValue currentValue) {
		super(new FlowLayout());

        this.schema = s;
        this.detail = d;
        this.propValue = currentValue;
		this.propID = propID; //currentValue.getProperty();
        this.property = s.getProperty(propID);

        if (property == null) {
            add(new JLabel("missing propertyID=" + propID));
            return;
        }

        setValue(currentValue);




//		nameHolder = new FlowPanel();
//		add(nameHolder);
//
//		netService.getPropertyData(new String[] { property }, new AsyncCallback<Property[]>() {
//
//
//			@Override public void onFailure(Throwable caught) {
//				add(new Label(caught.toString() + Arrays.asList(caught.getStackTrace())));
//			}
//
//			@Override public void onSuccess(Property[] result) {
//
//				propertyData = result[0];
//
//				initPropertyPanel();
//
//			}
//		});

	}
    abstract public void setValue(PropertyValue currentValue);
//    public void setValue(PropertyValue currentValue) {
//        removeAll();
//
//		add(new JLabel(property.getName()));
//
//        if (property instanceof IntegerVar) {
//            add(new IntPropertyPanel(schema, detail, propID, propValue));
//        }
//
////        JComboBox modeCombo = new JComboBox();
////        modeCombo.addItem("is");
////        modeCombo.addItem("will");
////        add(modeCombo);
//
//    }


	/** called after getPropertyData() becomes available */
	protected void initPropertyPanel() {
		String name = getProperty().getName();

		propLabel.setText(name);

		nameHolder.add(propLabel);		
	}

	public Property getProperty() {
		return property;
	}
	
	protected String getPropertyID() {
		return propID;
	}


	protected void setIs() {
		//getElement().getParentElement().s
//		removeStyleName("PropertyWillBe");
//		addStyleName("PropertyIs");
	}

	protected void setWillBe() {
//		removeStyleName("PropertyIs");
//		addStyleName("PropertyWillBe");
	}


	public Label getPropertyLabel() {
		return propLabel;
	}

	//abstract public void widgetToValue();

	public void setNode(DetailData node) {
		this.node = node;		
	}
	public DetailData getNode() {
		return node;
	}

	


}