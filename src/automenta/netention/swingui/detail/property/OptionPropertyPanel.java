package automenta.netention.swingui.detail.property;

import automenta.netention.node.Detail;
import automenta.netention.Schema;
import automenta.netention.value.Property;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import automenta.netention.value.PropertyValue;
import automenta.netention.swingui.util.JHyperLink;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;


abstract public class OptionPropertyPanel extends JPanel {

	private JComboBox typeSelect;

	private JPanel editPanel;
	private PropertyValue value;

	private List<PropertyOption> options = new ArrayList();

	private PropertyOption currentOption;
    private final Schema schema;
    private final Detail detail;
    private final String propertyID;
    private final Property property;

	public OptionPropertyPanel(Schema s, Detail d, String propertyID, PropertyValue v) {
        super(new FlowLayout());

        setOpaque(false);
        
        this.schema = s;
        this.detail = d;
        this.propertyID = propertyID;
        this.property = s.getProperty(propertyID);
        this.value = v;

        setValue(value);

        //add(new JLabel(property.getName()));
        add(new JHyperLink(property.getName(), ""));

        initOptions(options);

        initPropertyPanel();


	}

	protected void initPropertyPanel() {
		//super.initPropertyPanel();
		

		typeSelect = new JComboBox();
		for (PropertyOption po : options) {
			typeSelect.addItem(po.getName());			
		}
        typeSelect.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
				int x = typeSelect.getSelectedIndex();
				
				PropertyOption po = options.get(x);
				setCurrentOption(po);

                if (!po.accepts(getValue()))
                    setValue(po.newDefaultValue());

				JPanel p = po.newEditPanel(value);
				
				editPanel.removeAll();
				editPanel.add(p);

                updateUI();
			}

		});
		add(typeSelect);
		
		editPanel = new JPanel();
        editPanel.setOpaque(false);
		add(editPanel);

		valueToWidget();

        updateUI();

	}

    protected void setIs() {

    }
    protected void setWillBe() {

    }

    protected void setValue(PropertyValue val) {
        this.value = val;
    }

	abstract protected void initOptions(List<PropertyOption> options);

	/** load */
	private void valueToWidget() {
		if (value==null)
			return;

		for (int i = 0; i < options.size(); i++) {
			PropertyOption po = options.get(i);
			if (po.accepts(value)) {

				typeSelect.setSelectedIndex(i);
				
				setCurrentOption(po);
				
				JPanel p = po.newEditPanel(value);
				editPanel.removeAll();
				editPanel.add(p);
				
				return;
			}
		}
	}

	private void setCurrentOption(PropertyOption po) {
		this.currentOption = po;		
	}

//	protected void setValue(PropertyValue newValue) {
//		PropertyValue oldValue = this.value;
//		this.value = newValue;
//
//		this.value.setProperty(getProperty());
//
//		//TODO replace old with new value, at original index
//		if (getNode()!=null) {
//			if (oldValue!=newValue) {
//				synchronized (getNode().getProperties()) {
//					getNode().getProperties().remove(oldValue);
//					getNode().getProperties().add(newValue);
//				}
//			}
//		}
//
//	}

//	@Override
//	public void setNode(DetailData node) {
//		super.setNode(node);
//		setValue(getValue());
//	}
	
	/** save */
	public void widgetToValue() {
		if (currentOption!=null) {
			//causes value to be updated by data presently in the widgets

			setValue(currentOption.getValue());
		}
	}

	public PropertyValue getValue() {
		return value;
	}

    public Property getProperty() {
        return property;
    }

    
}
