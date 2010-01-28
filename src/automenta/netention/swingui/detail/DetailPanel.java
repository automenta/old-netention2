/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.detail;

import automenta.netention.api.Detail;
import automenta.netention.api.Network;
import automenta.netention.api.Pattern;
import automenta.netention.api.Schema;
import automenta.netention.api.value.Property;
import automenta.netention.api.value.PropertyValue;
import automenta.netention.api.value.integer.IntegerVar;
import automenta.netention.api.value.real.RealVar;
import automenta.netention.api.value.string.StringVar;
import automenta.netention.swingui.agent.AgentPanel;
import automenta.netention.swingui.detail.property.IntPropertyPanel;
import automenta.netention.swingui.detail.property.RealPropertyPanel;
import automenta.netention.swingui.detail.property.StringPropertyPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author seh
 */
public class DetailPanel extends JPanel {

    JMenuBar menu = new JMenuBar();
    private JPanel content = new JPanel(new BorderLayout());
    private final Network network;
    private Detail detail;

    public DetailPanel(Network n) {
        super(new BorderLayout());

        this.network = n;
    }

    public DetailPanel(Network n, Detail d) {
        this(n);
        setDetail(d);
    }

    protected void updateMenu(Detail d) {
        menu.removeAll();

        JMenu detailMenu = new JMenu(d.getName());
        detailMenu.setFont(AgentPanel.FontH1);

        //detailMenu.add(new JMenuItem("Copy as mine"));
        detailMenu.add(new JMenuItem("Rename"));
        detailMenu.add(new JMenuItem("Delete"));


        menu.add(detailMenu);


    }

    public class DetailsPropertyPanel extends JPanel {

        private DetailsPropertyPanel(Schema schema, final Detail d) {
            super(new GridBagLayout());

            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = gc.NORTHWEST;
            gc.gridx = 0;
            gc.gridy = 0;
            gc.weighty = 0;
            gc.weightx = 1.0;

            DetailEditMenu detailMenu = new DetailEditMenu(schema, d) {
                @Override protected void refreshProperties() {
                    DetailPanel.this.refresh();
                }
            };
            add(detailMenu, gc);

            gc.insets = new Insets(4,16,4,4);
//            JButton updateButton = new JButton("Update");
//            updateButton.setEnabled(false);
//            detailMenu.add(updateButton);

            gc.gridy++;
            for (PropertyValue p : d.getProperties()) {
                Property prop = schema.getProperty(p.getProperty());

                if (prop instanceof IntegerVar) {
                    add(new IntPropertyPanel(schema, d, p.getProperty(), p), gc);
                } else if (prop instanceof RealVar) {
                    add(new RealPropertyPanel(schema, d, p.getProperty(), p), gc);
                } else if (prop instanceof StringVar) {
                    add(new StringPropertyPanel(schema, d, p.getProperty(), p), gc);
                }

                gc.gridy++;
            }

            gc.weighty = 1.0;
            gc.fill = gc.BOTH;
            add(Box.createVerticalBox(), gc);

            updateUI();
        }
    }

    public static class LinkPanel extends JPanel {

        private LinkPanel(Network network, Detail d) {
            super(new BorderLayout());
            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Satisfying", new JPanel());
            tabs.addTab("Similar", new JPanel());
            tabs.setTabPlacement(JTabbedPane.BOTTOM);
            add(tabs, BorderLayout.CENTER);
        }
    }

    public void refresh() {
        setDetail(detail);
    }

    public void setDetail(Detail d) {
        removeAll();

        this.detail = d;

        content.removeAll();

        add(menu, BorderLayout.NORTH);

//        GridBagConstraints gc = new GridBagConstraints();
//        gc.gridy = 0;
//
//        content.add(new JLabel(d.getName()), gc);
//
//
//        gc.gridy++;
//
//        //existing types (type heading, w/ remove button)
//        //      existing parameters, per type
//        //      additional possible parameters, per type
//        for (String ps : d.getPatterns()) {
//            Pattern p = network.getSchema().getPattern(ps);
//            if (p!=null) {
//                addPatternSection(p, gc);
//            }
//        }

        DetailsPropertyPanel propPanel = new DetailsPropertyPanel(network.getSchema(), d);
        //propPanel.add(new JLabel("Properties"), BorderLayout.CENTER);
        propPanel.setBackground(Color.WHITE);

        LinkPanel linkPanel = new LinkPanel(network, d);

        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        sp.setTopComponent(new JScrollPane(propPanel));
        sp.setBottomComponent(new JScrollPane(linkPanel));
        content.add(sp, BorderLayout.CENTER);

        updateMenu(d);

        add(content, BorderLayout.CENTER);

        sp.setDividerLocation(0.9);

        updateUI();


    }

    private void addPatternSection(Pattern p, GridBagConstraints gc) {
        gc.gridy++;

        JPanel patternSection = new JPanel(new FlowLayout());
        patternSection.add(new JButton(p.getName()));

        content.add(patternSection, gc);

    }
}
