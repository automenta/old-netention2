/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.detail;

import automenta.netention.api.Detail;
import automenta.netention.api.Network;
import automenta.netention.api.Pattern;
import automenta.netention.api.Schema;
import automenta.netention.api.value.PropertyValue;
import automenta.netention.swingui.agent.AgentPanel;
import automenta.netention.swingui.detail.property.PropertyPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.List;
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


    public DetailPanel(Network n) {
        super(new BorderLayout());

        this.network = n;
    }

    public DetailPanel(Network n, Detail d) {
        this(n);
        setDetail(d);
    }

    public static class PatternMenu extends JMenu {
        private final List<Pattern> ep;
        private final Schema schema;
        public PatternMenu(Schema s, Detail d) {
            super("It's a...");

            this.schema = s;
            this.ep = s.getPatterns(d);

            for (Pattern p : s.getRootPatterns()) {
                add(newSubMenu(p));
            }
        }

        public JMenuItem newSubMenu(Pattern p) {
            Collection<Pattern> children = schema.getChildren(p);
            if (children.size() > 0) {
                JMenu s = new JMenu(p.getName());

                JMenuItem mp = new JMenuItem(p.getName());
                if (ep.contains(p)) {
                    mp.setEnabled(false);
                }
                s.add(mp);

                s.addSeparator();

                for (Pattern c : children) {
                    s.add(newSubMenu(c));
                }
                return s;
            }
            else {
                JMenuItem mp = new JMenuItem(p.getName());
                if (ep.contains(p)) {
                    mp.setEnabled(false);
                }
                return mp;
            }
        }
    }

    protected void updateMenu(Detail d) {
        menu.removeAll();
        
        JMenu detailMenu = new JMenu(d.getName());
        detailMenu.setFont(AgentPanel.FontH1);

        //detailMenu.add(new JMenuItem("Copy as mine"));
        detailMenu.add(new JMenuItem("Rename"));
        detailMenu.add(new JMenuItem("Delete"));

        
        menu.add(detailMenu);

        PatternMenu patternMenu = new PatternMenu(network.getSchema(), d);
        menu.add(patternMenu);

        JButton updateButton = new JButton("Update");
        updateButton.setEnabled(false);
        updateButton.setAlignmentX(JButton.RIGHT_ALIGNMENT);
        menu.add(updateButton);
    }

    public static class DetailsPropertyPanel extends JPanel {

        private DetailsPropertyPanel(Schema schema, Detail d) {
            super(new GridBagLayout());


            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = gc.NORTHWEST;
            gc.gridy = 0;
            gc.weighty = 0;
            gc.weightx = 1.0;
            

            for (PropertyValue p : d.getProperties()) {
                add(new PropertyPanel(schema, d, p), gc);
                gc.gridy++;
            }

            gc.weighty = 1.0;
            gc.fill = gc.BOTH;
            add(Box.createVerticalBox(), gc);
        }

    }

    public static class LinkPanel extends JPanel {

        private LinkPanel(Network network, Detail d) {
            super(new BorderLayout());
            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Satisfied", new JPanel());
            tabs.addTab("Similar", new JPanel());
            tabs.setTabPlacement(JTabbedPane.BOTTOM);
            add(tabs, BorderLayout.CENTER);
        }

    }

    public void setDetail(Detail d) {
        removeAll();

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

        updateUI();

        sp.setDividerLocation(0.9);


    }

    private void addPatternSection(Pattern p, GridBagConstraints gc) {
        gc.gridy++;
        
        JPanel patternSection = new JPanel(new FlowLayout());
        patternSection.add(new JButton(p.getName()));

        content.add(patternSection, gc);

    }
}
