package automenta.netention.swingui.pattern;

import automenta.netention.swingui.util.JHyperLink;
import automenta.netention.swingui.*;
import automenta.netention.Self;
import automenta.netention.Pattern;
import automenta.netention.value.Property;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PatternPanel extends JPanel {

    public PatternPanel(Self self, Pattern p) {
        super();
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.fill = gc.NONE;
        gc.weightx = 1.0;
        gc.weighty = 0;
        add(new JLabel(p.getName()), gc);
        gc.gridy++;
        add(new JLabel(p.getDescription()), gc);
        JPanel extendsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        extendsPanel.add(new JLabel("Extends: "));
        for (String ei : p.getExtends()) {
            Pattern ep = self.getSchema().getPattern(ei);
            extendsPanel.add(new JHyperLink(ep.getName(), "Go to Pattern " + ep.getName()));
        }
        gc.gridy++;
        add(extendsPanel, gc);
        JPanel localProperties = new JPanel();
        localProperties.setLayout(new BoxLayout(localProperties, BoxLayout.PAGE_AXIS));
        for (String sdp : p.getDefinedProperties()) {
            Property dp = self.getSchema().getProperty(sdp);
            localProperties.add(new PropertyViewPanel(dp, true));
        }
        gc.gridy++;
        add(localProperties, gc);
        JPanel inheritedProperties = new JPanel();
        inheritedProperties.setLayout(new BoxLayout(inheritedProperties, BoxLayout.PAGE_AXIS));
        inheritedProperties.add(new JLabel("Inherited:"));
        for (String sdp : self.getSchema().getInheritedPatterns(p)) {
            if (!p.getDefinedProperties().contains(sdp)) {
                Property dp = self.getSchema().getProperty(sdp);
                inheritedProperties.add(new PropertyViewPanel(dp, false));
            }
        }
        gc.gridy++;
        add(inheritedProperties, gc);

        gc.gridy++;
        gc.weighty = 1.0;
        add(Box.createVerticalBox(), gc);
    }
}
