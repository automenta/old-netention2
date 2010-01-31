/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.bot;

import automenta.netention.net.Concept;
import automenta.netention.net.PlexusGraph;
import automenta.netention.net.Twitter;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter.EdgeType;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * list of agents
 * list of messages
 *
 * @author seh
 */
public class BotPanel extends JPanel {

    private JList pList;
    private PlexusListModel listModel;
    private final PlexusGraph p;
    private final Twitter t;
    private PageRank<Object, Double> pr;
    private Graph subgraph = new DirectedSparseMultigraph();
    private SpringLayout layout;

    int subgraphHops = 3;
    
    public static class PlexusListModel extends DefaultListModel {

        private final PlexusGraph p;

        public PlexusListModel(PlexusGraph p) {
            super();

            this.p = p;

        }
    }

    public class PlexusIndex extends JPanel {

        public PlexusIndex(PlexusGraph p) {
            super(new BorderLayout());

            pList = new JList(listModel);
            pList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    updateSubGraph(pList.getSelectedValue());
                }
            });
            pList.setCellRenderer(new ListCellRenderer() {

                @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    if (pr == null) {
                        return new JLabel(value.toString());
                    }

                    JPanel j = new JPanel(new FlowLayout());

                    double a = ((Double) pr.getVertexScore(value));

                    if (isSelected) {
                        j.setBackground(Color.ORANGE);
                    } else {
                        float af = (float)a;
                        Color c = new Color( 20.0f *af, 10.0f * af, 0.0f);
                        j.setBackground(c);
                    }

                    String scoreString = Integer.toString((int) (a * 10000.0));
                    JLabel jl = new JLabel(value.toString() + " " + scoreString);
                    j.add(jl);

                    jl.setForeground(Color.WHITE);

                    return j;
                }
            });
            add(new JScrollPane(pList), BorderLayout.CENTER);

        }
    }

    public class ActionPanel extends JPanel {

        public ActionPanel(final PlexusGraph pg, final Twitter t) {
            super(new FlowLayout());

            JButton tlButton = new JButton("Public Timeline");
            tlButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        t.getPublicTimeline(pg);
                        refresh();
                    } catch (Exception ex) {
                        System.err.println(ex);
                    }
                }
            });
            add(tlButton);

        }
    }

    public BotPanel(PlexusGraph p) {
        super(new BorderLayout());

        this.p = p;
        this.t = new Twitter();
        this.listModel = new PlexusListModel(p);

        refresh();

    }

    public void refresh() {
        removeAll();

        listModel.removeAllElements();

        pr = p.getConcepts();

        List<Object> obj = new ArrayList(p.graph.getVertices());
        Collections.sort(obj, new Comparator() {

            public int compare(Object o1, Object o2) {
                double a = (Double) pr.getVertexScore(o1);
                double b = (Double) pr.getVertexScore(o2);
                if (a < b) {
                    return -1;
                } else if (a == b) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        for (Object o : obj) {
          listModel.addElement(o);
        }

        add(new PlexusIndex(p), BorderLayout.WEST);
        add(new ActionPanel(p, t), BorderLayout.NORTH);

        JPanel j = new JPanel(new BorderLayout());
        layout = new SpringLayout(subgraph);
        layout.setSize(new Dimension(900, 700));
        VisualizationViewer<Integer, String> vv = new VisualizationViewer<Integer, String>(layout);
        //vv.setPreferredSize(new Dimension(350, 350));

        // Show vertex and edge labels
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        // Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(gm);
        j.add(vv, BorderLayout.CENTER);
        add(new JScrollPane(j), BorderLayout.CENTER);

        updateUI();
    }

    protected void updateSubGraph(Object focus) {
        if (focus == null) {
            return;
        }
        if (!p.graph.containsVertex(focus)) {
            return;
        }
        subgraph = new KNeighborhoodFilter(focus, subgraphHops, EdgeType.IN_OUT).transform(p.graph);
        layout.setGraph(subgraph);
    }
}