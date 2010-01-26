/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.detail;

import automenta.netention.api.Detail;
import automenta.netention.api.Network;
import automenta.netention.api.Pattern;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author seh
 */
public class DetailPanel extends JPanel {

    JMenuBar menu = new JMenuBar();
    private JPanel content = new JPanel(new GridBagLayout());
    private final Network network;


    public DetailPanel(Network n) {
        super(new BorderLayout());

        this.network = n;
    }

    public DetailPanel(Network n, Detail d) {
        this(n);
        setDetail(d);
    }

    protected void updateMenu() {
        menu.removeAll();
        
        JMenu detailMenu = new JMenu("Detail");
        //detailMenu.add(new JMenuItem("Copy as mine"));
        detailMenu.add(new JMenuItem("Delete"));

        //additional possible types
        

        menu.add(detailMenu);
    }

    public void setDetail(Detail d) {
        removeAll();

        content.removeAll();
        
        add(menu, BorderLayout.NORTH);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;

        content.add(new JLabel(d.getName()), gc);


        gc.gridy++;
        
        //existing types (type heading, w/ remove button)
        //      existing parameters, per type
        //      additional possible parameters, per type
        for (String ps : d.getPatterns()) {
            Pattern p = network.getSchema().getPattern(ps);
            if (p!=null) {
                addPatternSection(p, gc);
            }
        }

        

        updateMenu();

        add(content, BorderLayout.CENTER);

        updateUI();

    }

    private void addPatternSection(Pattern p, GridBagConstraints gc) {
        gc.gridy++;
        
        JPanel patternSection = new JPanel(new FlowLayout());
        patternSection.add(new JButton(p.getName()));

        content.add(patternSection, gc);

    }
}
