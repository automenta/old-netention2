package automenta.netention.swingui.pattern;

import automenta.netention.value.Property;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PropertyViewPanel extends JPanel {

    public PropertyViewPanel(Property dp, boolean editable) {
        super(new BorderLayout());
        String typeString = dp.getClass().getSimpleName();
        add(new JLabel(typeString + " " + dp.getName()), BorderLayout.CENTER);
        add(new JLabel(dp.getDescription()), BorderLayout.SOUTH);
    }
}
