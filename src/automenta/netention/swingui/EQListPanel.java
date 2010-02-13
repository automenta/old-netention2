/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui;

import automenta.netention.Memory;
import automenta.netention.node.Agent;
import automenta.netention.Self;
import automenta.netention.focus.Focus;
import automenta.netention.swingui.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
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
public class EQListPanel extends JPanel implements ListSelectionListener {

    private final Focus focus = new Focus();
    private final Self self;
    private final Agent agent;
    private JList dList;
    //private final ObjPanel oPanel;
    DefaultListModel olm = new DefaultListModel();
    public static final Font FontH1 = new Font("Arial", Font.BOLD, 24);
    public static final Font FontH2 = new Font("Arial", Font.PLAIN, 20);
    public static final Font FontH3 = new Font("Arial", Font.PLAIN, 16);
    HashMap<Object, Double> score;
    private final JPanel statusPanel = new JPanel(new FlowLayout());

    private void updateStatus(Memory memory, int numShown) {
        String statusString = numShown + " vertices of " + memory.getVertexCount() + " ( " + memory.getEdgeCount() + " edges)";
        statusPanel.removeAll();
        statusPanel.add(new JLabel(statusString));
        statusPanel.updateUI();
    }

    public class ObjectList extends JList {

        private ObjectList(ListModel model) {
            super(model);
            setCellRenderer(new ObjectListCellRenderer(EQListPanel.this));
        }
    }

    public class DetailSelectPanel extends JSplitPane {

        public DetailSelectPanel() {
            super(JSplitPane.VERTICAL_SPLIT);

            //left: list of details
            dList = new ObjectList(refreshObjectList());
            dList.addListSelectionListener(EQListPanel.this);


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


            indexPanel.add(new JScrollPane(focusPanel), BorderLayout.CENTER);

            dList.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (e.getClickCount() == 2) {
                        Object o = dList.getSelectedValue();
                        showObject(o);
                    }
                }
            });

            setTopComponent(indexPanel);
            setBottomComponent(new JScrollPane(dList));

        }
    }

    public EQListPanel(final Self self) {
        super(new BorderLayout());

        this.self = self;
        this.agent = self.getAgent();

        JMenuBar headerPanel = new JMenuBar();

        JMenu nameMenu = new JMenu(agent.getName());
        nameMenu.add(new JMenuItem("Myself"));
        nameMenu.addSeparator();
        nameMenu.add(new JMenuItem("Log out..."));
        nameMenu.add(new JMenuItem("Exit"));

        headerPanel.add(nameMenu);

//                    final JButton newButton = new JButton(" + ");
//            newButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    new NewMenu().show(newButton, newButton.getWidth() / 2, newButton.getHeight() / 2);
//                }
//            });
        headerPanel.add(new AddMenu(self) {
            @Override protected void refresh() {
                refreshObjectList();
            }
        });

        add(headerPanel, BorderLayout.NORTH);

        //JSplitPane detailsPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        DetailSelectPanel selectPanel = new DetailSelectPanel();

        add(selectPanel, BorderLayout.CENTER);
        //detailsPanel.setLeftComponent(selectPanel);

//        //right: DetailPanel
//        oPanel = new ObjPanel(self);
//        add
//        detailsPanel.setRightComponent(oPanel);
//
//        detailsPanel.setDividerLocation(0.25);

        //add(detailsPanel, BorderLayout.CENTER);

        add(statusPanel, BorderLayout.SOUTH);
    }

    protected ListModel refreshObjectList() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                olm.removeAllElements();

                double minScore = 1.0;
                double maxScore = 0.0;
                score = new HashMap(self.getNodes().size());
                for (Object o : self.getNodes()) {
                    double f = focus.score(o);
                    score.put(o, f);
                    if (f < minScore) {
                        minScore = f;
                    }
                    if (f > maxScore) {
                        maxScore = f;
                    }
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
                        if (a == b) {
                            return 0;
                        }
                        if (a > b) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });

                int numShown = 0;
                for (Object o : shown) {
                    if (score.get(o) > 0) {
                        olm.addElement(o);
                        numShown++;
                    }
                }

                updateStatus(self.getMemory(), numShown);
            }
        });

        return olm;
    }

    public void valueChanged(ListSelectionEvent e) {
        Object o = dList.getSelectedValue();
        //showObject(o);
    }

    public void showObject(final Object o) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ObjPanel oPanel = new ObjPanel(self, o);

                new SwingWindow(oPanel, 400, 400, false);
            }
        });
    }
}
