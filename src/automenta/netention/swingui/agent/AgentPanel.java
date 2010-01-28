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
import javax.swing.JComboBox;
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
    private JList dList;
    private final DetailPanel dPanel;
    DefaultListModel dlm = new DefaultListModel();
    public static final Font FontH1 = new Font("Arial", Font.BOLD, 24);
    public static final Font FontH2 = new Font("Arial", Font.PLAIN, 20);

    public class DetailSelectPanel extends JPanel {

        public DetailSelectPanel() {
            super(new BorderLayout());

            //left: list of details
            dList = new JList(updateDetailsModel());
            dList.setCellRenderer(new DetailsListCellRenderer());
            dList.addListSelectionListener(AgentPanel.this);

            add(new JScrollPane(dList), BorderLayout.CENTER);

            JPanel filterPanel = new JPanel(new BorderLayout());

            JComboBox selectBox = new JComboBox();

            JMenu selectMenu = new JMenu("All");
            selectBox.addItem("All");
            selectBox.addItem("What");
            selectBox.addItem("Who");
            selectBox.addItem("Where");
            selectBox.addItem("When");

            JButton newButton = new JButton(" + ");
            newButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    onNewDetail();
                }
            });
            filterPanel.add(newButton, BorderLayout.WEST);

            filterPanel.add(selectBox, BorderLayout.CENTER);

            add(filterPanel, BorderLayout.NORTH);

        }
    }

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

        add(headerPanel, BorderLayout.NORTH);

        JSplitPane detailsPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);



        DetailSelectPanel selectPanel = new DetailSelectPanel();

        detailsPanel.setLeftComponent(selectPanel);

        //right: DetailPanel
        dPanel = new DetailPanel(network);
        detailsPanel.setRightComponent(dPanel);

        detailsPanel.setDividerLocation(0.25);

        add(detailsPanel, BorderLayout.CENTER);
    }

    protected ListModel updateDetailsModel() {

        dlm.removeAllElements();
        ;
        for (String ns : agent.getNodes()) {
            Node n = network.getNode(ns);
            dlm.addElement(n);
        }
        return dlm;
    }

    public void valueChanged(ListSelectionEvent e) {
        Object o = dList.getSelectedValue();
        if (o instanceof Detail) {
            setDetail((Detail) o);
        }
    }

    public void setDetail(Detail d) {
        dPanel.setDetail(d);
    }

    protected void onNewDetail() {
        Detail d = network.newDetail(agent, "Unnamed");
        updateDetailsModel();
    }

}
