/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui;

import automenta.netention.node.Agent;
import automenta.netention.node.Detail;
import automenta.netention.Self;
import automenta.netention.focus.Focus;
import automenta.netention.io.Twitter;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author seh
 */
public class SelfPanel extends JPanel implements ListSelectionListener {

    private final Twitter t = new Twitter();
    private final Focus focus = new Focus();
    private final Self self;
    private final Agent agent;
    private JList dList;
    private final ObjPanel oPanel;
    DefaultListModel olm = new DefaultListModel();
    public static final Font FontH1 = new Font("Arial", Font.BOLD, 24);
    public static final Font FontH2 = new Font("Arial", Font.PLAIN, 20);
    public static final Font FontH3 = new Font("Arial", Font.PLAIN, 16);
    HashMap<Object, Double> score;

    public class NewMenu extends JPopupMenu {

        public NewMenu() {
            super();

            JMenuItem newDetail = new JMenuItem("Detail...");
            newDetail.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Detail d = self.newDetail(agent, "Unnamed");
                    refreshObjectList();
                }
            });

            JMenuItem ambientMessages = new JMenuItem("Ambient Messages...");
            ambientMessages.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        t.getPublicTimeline(self.getMemory());
                        refreshObjectList();
                    } catch (Exception ex) {
                        Logger.getLogger(SelfPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            add(newDetail);
            addSeparator();
            add(ambientMessages);

        }
    }

    public class DetailSelectPanel extends JPanel {

        public DetailSelectPanel() {
            super(new BorderLayout());

            //left: list of details
            dList = new JList(refreshObjectList());
            dList.setCellRenderer(new ObjectListCellRenderer(SelfPanel.this));
            dList.addListSelectionListener(SelfPanel.this);

            add(new JScrollPane(dList), BorderLayout.CENTER);

            JPanel indexPanel = new JPanel(new BorderLayout());

            FocusPanel focusPanel = new FocusPanel(focus) {
                @Override public void onFocusChanged() {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            refreshObjectList();
                        }
                    });
                }
            };

            final JButton newButton = new JButton(" + ");
            newButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new NewMenu().show(newButton, newButton.getWidth() / 2, newButton.getHeight() / 2);
                }
            });
            indexPanel.add(newButton, BorderLayout.WEST);

            indexPanel.add(focusPanel, BorderLayout.CENTER);

            add(indexPanel, BorderLayout.NORTH);

        }
    }

    public SelfPanel(final Self self, final Agent a) {
        super(new BorderLayout());

        this.self = self;
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
        oPanel = new ObjPanel(self);
        detailsPanel.setRightComponent(oPanel);

        detailsPanel.setDividerLocation(0.25);

        add(detailsPanel, BorderLayout.CENTER);
    }

    protected ListModel refreshObjectList() {
        olm.removeAllElements();

        double minScore = 1.0;
        double maxScore = 0.0;
        score = new HashMap(self.getNodes().size());
        for (Object o : self.getNodes()) {
            double f = focus.score(o);
            score.put(o, f);
            if (f < minScore) minScore = f;
            if (f > maxScore) maxScore = f;
        }
        
//        //normalize to 0..1.0
//        for (Object o : self.getNodes()) {
//            double f = score.get(o);
//            f = (f - minScore) * (maxScore - minScore);
//            score.put(o, f);
//        }

        List<Object> shown = new ArrayList(score.keySet());
        Collections.sort(shown, new Comparator() {
            public int compare(Object o1, Object o2) {
                double a = score.get(o1);
                double b = score.get(o2);
                if (a == b) return 0;
                if (a > b) return -1;
                else return 1;
            }
        });
        
        for (Object o : shown) {
            if (score.get(o) > 0)
                olm.addElement(o);
        }

        return olm;
    }

    public void valueChanged(ListSelectionEvent e) {
        Object o = dList.getSelectedValue();
        showObject(o);
    }

    public void showObject(final Object o) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                oPanel.setObject(o);
            }
        });
    }
}
