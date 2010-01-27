/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swingui.agent;

import automenta.netention.api.Agent;
import automenta.netention.api.Detail;
import automenta.netention.api.Network;
import automenta.netention.api.Node;
import automenta.netention.swingui.detail.DetailPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author seh
 */
public class AgentPanel extends JPanel implements ListSelectionListener {
    private final Network network;
    private final Agent agent;
    private final JList dList;
    private final DetailPanel dPanel;
    DefaultListModel dlm = new DefaultListModel();

    public static final Font FontH1 = new Font("Arial", Font.BOLD, 24);
    public static final Font FontH2 = new Font("Arial", Font.PLAIN, 20);

    public AgentPanel(final Network network, final Agent a) {
        super(new BorderLayout());

        this.network = network;
        this.agent = a;

        JMenuBar headerPanel = new JMenuBar();

        JMenu nameMenu = new JMenu(a.getName());
        nameMenu.add(new JMenuItem("Myself"));
        nameMenu.addSeparator();
        nameMenu.add(new JMenuItem("Log out..."));
        nameMenu.add(new JMenuItem("Exit"));
        
        headerPanel.add(nameMenu);

        JMenuItem newDetail = new JMenuItem("New...");
        newDetail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Detail d = network.newDetail(a, "Unnamed");
                updateDetailsModel();
            }
        });
        headerPanel.add(newDetail);

        add(headerPanel, BorderLayout.NORTH);

        JSplitPane detailsPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        //left: list of details
        dList = new JList(updateDetailsModel());
        dList.setCellRenderer(new DetailsListCellRenderer());
        detailsPanel.setLeftComponent(new JScrollPane(dList));
        dList.addListSelectionListener(this);

        //right: DetailPanel
        dPanel = new DetailPanel(network);
        detailsPanel.setRightComponent(new JScrollPane(dPanel));

        detailsPanel.setDividerLocation(0.25);
        
        add(detailsPanel, BorderLayout.CENTER);
    }

    protected ListModel updateDetailsModel() {

        dlm.removeAllElements();;
        for (String ns : agent.getNodes()) {
            Node n = network.getNode(ns);
            dlm.addElement(n);
        }
        return dlm;
    }

    public void valueChanged(ListSelectionEvent e) {
        Object o = dList.getSelectedValue();
        if (o instanceof Detail) {
            setDetail((Detail)o);
        }
    }

    public void setDetail(Detail d) {
        dPanel.setDetail(d);
    }
}
