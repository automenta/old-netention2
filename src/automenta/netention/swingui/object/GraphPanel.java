package automenta.netention.swingui.object;

import automenta.netention.Memory;
import automenta.netention.Self;
import automenta.netention.swingui.AddMenu;
import automenta.netention.swingui.ObjectListCellRenderer;
import automenta.netention.swingui.util.SwingWindow;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter.EdgeType;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

public class GraphPanel extends JPanel {

    private SpringLayout layout;
    private final Graph graph;
    int subgraphHops = 1;
    int maxSubgraphHops = 6;
    private final Object focus;
    private JPanel controlPanel;
    int visibleVert;
    int maxVisibleVert = 64;
    int focusedVert;
    int totalVert;
    private JLabel graphStat;
    private JTextField maxNodesField;
    private final JPanel typesPanel = new JPanel(new FlowLayout());
    private Map<Class, Boolean> typeVisible = new HashMap();

    public class GraphControlMenu extends JPanel {

        private final JPanel filtersPanel;

        public GraphControlMenu(GraphPanel gp) {
            super(new GridBagLayout());


            GridBagConstraints gc = new GridBagConstraints();

            gc.gridx = 0;

            //1. set max number of nodes (JTEXTFIELD)
            maxNodesField = new JTextField(6);
            maxNodesField.setText(Integer.toString(maxVisibleVert));
            maxNodesField.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    maxVisibleVert = Integer.decode(maxNodesField.getText());
                    System.out.println("maxVisibleNodes: " + maxVisibleVert);
                    refresh();
                }
            });
            add(maxNodesField, gc);
            gc.gridx++;

            //2. type filters (pushbuttons)

            filtersPanel = new JPanel();
            add(filtersPanel, gc);
            gc.gridx++;

            //3. graph stats (#v, #e)
            graphStat = new JLabel("X");
            add(graphStat, gc);
            gc.gridx++;

            final JPanel buttonPanel = new JPanel(new FlowLayout());
            add(buttonPanel, gc);
            gc.gridx++;

//            JButton growButton = new JButton("Grow...");
//            growButton.addActionListener(new ActionListener() {
//
//                public void actionPerformed(ActionEvent e) {
//                    //updateHops(1);
//                }
//            });
//            JButton shrinkButton = new JButton("Shrink...");
//            shrinkButton.addActionListener(new ActionListener() {
//
//                public void actionPerformed(ActionEvent e) {
//                    //updateHops(-1);
//                }
//            });
//
//            add(growButton, gc);
//            gc.gridx++;
//
//            add(shrinkButton, gc);
//            gc.gridx++;

            add(typesPanel, gc);
            gc.gridx++;

            refresh();

        }

        protected void refresh() {
            //update filters panel
            filtersPanel.removeAll();

            GraphPanel.this.refresh();

            updateUI();
        }
    }

    public GraphPanel(Self self, final Graph graph, Object focus) {
        super(new BorderLayout());

        this.graph = graph;
        this.focus = focus;

        controlPanel = new JPanel(new BorderLayout());

        JMenuBar jb = new JMenuBar();
        jb.add(new AddMenu(self) {

            @Override protected void refresh() {
            }
        });

        
        JMenu filterMenu = new JMenu();
        JMenuItem pagerankThreshold = new JMenuItem("Pagerank Threshold");
        pagerankThreshold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //compute pagerank and remove all objects below a threshold
                new SwingWindow(new PageRankFilterPanel(graph), 500, 500, false);
            }
        });
        filterMenu.add(pagerankThreshold);
        jb.add(filterMenu);

        controlPanel.add(jb, BorderLayout.CENTER);

        controlPanel.add(new GraphControlMenu(this), BorderLayout.SOUTH);



        refresh();
    }

    protected void updateHops(int dh) {
        subgraphHops += dh;
        subgraphHops = Math.min(subgraphHops, maxSubgraphHops);
        subgraphHops = Math.max(subgraphHops, 0);

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                refresh();
            }
        });

    }

    public Predicate newEdgePredicate() {
        return new Predicate() {

            public boolean evaluate(Object arg0) {
                return false;
            }
        };
    }

    public Predicate newVertexPredicate() {
        return new Predicate() {

            public boolean evaluate(Object o) {
                Boolean b = typeVisible.get(o.getClass());
                if (b == null) {
                    return false;
                }
                return b;
            }
        };
    }

    public static class FirstNVertices implements Predicate<Object> {

        int current;
        private final int n;

        public FirstNVertices(int n) {
            this.n = n;
            this.current = 0;
        }

        public boolean evaluate(Object o) {
            current++;
            if (current <= n) {
                return true;
            } else {
                return false;
            }
        }
//        public int getCurrent() {
//            return current;
//        }
    }

    public void refresh() {
        removeAll();


        totalVert = graph.getVertexCount();
        Graph subgraph2 = (focus != null) ? new KNeighborhoodFilter(focus, subgraphHops, EdgeType.IN_OUT).transform(graph) : graph;
        //Graph subgraph2 = new EdgePredicateFilter(newEdgePredicate()).transform(subgraph);

        Graph subgraph3 = new VertexPredicateFilter(newVertexPredicate()).transform(subgraph2);

        typesPanel.removeAll();
        typesPanel.setLayout(new BoxLayout(typesPanel, BoxLayout.LINE_AXIS));

        Set<Class> presentVertexTypes = Memory.getPresentVertexTypes(subgraph2);

        for (final Class c : presentVertexTypes) {

            final JToggleButton b = new JToggleButton(c.getSimpleName());
            if (typeVisible.get(c) != null) {
                b.setSelected(typeVisible.get(c));
            } else {
                b.setSelected(true);
                setTypeVisible(c, true);
            }
            b.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            setTypeVisible(c, b.isSelected());
                            refresh();
                        }
                    });
                }
            });
            b.setEnabled(true);
            typesPanel.add(b);
        }
        typesPanel.updateUI();


        focusedVert = subgraph3.getVertexCount();
        Graph subgraph = new VertexPredicateFilter(new FirstNVertices(maxVisibleVert)).transform(subgraph3);
        visibleVert = subgraph.getVertexCount();

        layout = new SpringLayout(subgraph);
        layout.setSize(new Dimension(900, 900));

        VisualizationViewer vv = new VisualizationViewer(layout);
        vv.setAutoscrolls(true);


        //vv.setPreferredSize(new Dimension(350, 350));
        // Show vertex and edge labels
        vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<Object, Paint>() {

            public Paint transform(Object o) {
                Paint p = Color.getHSBColor(ObjectListCellRenderer.getHue(o), 0.7f, 0.7f);
                return p;
            }
        });
        vv.getRenderContext().setVertexShapeTransformer(new Transformer() {

            public Object transform(Object arg0) {
                return new Rectangle2D.Double(0, 0, 16, 16);
            }
        });

        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.BentLine());

        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller() {

            int maxStringLength = 24;

            @Override public String transform(Object v) {
                String s = v.toString();
                if (s.length() > maxStringLength) {
                    s = s.substring(0, maxStringLength - 1);
                }
                return s;
            }
        });
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        // Create a graph mouse and add it to the visualarization component

        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        //gm.setMode(ModalGraphMouse.Mode.EDITING);
        vv.setGraphMouse(gm);

        add(controlPanel, BorderLayout.NORTH);
        add(vv, BorderLayout.CENTER);

        updateCountLabel();

        updateUI();
    }

    public void setTypeVisible(Class t, boolean visible) {
        typeVisible.put(t, visible);
    }

    protected void updateCountLabel() {
        String s = visibleVert + " / " + focusedVert + " / " + totalVert;
        if (visibleVert < focusedVert) {
            graphStat.setBackground(Color.ORANGE);
            graphStat.setOpaque(true);
        } else {
            graphStat.setBackground(Color.WHITE);
        }
        graphStat.setText(s);
    }

    public SpringLayout getGraphLayout() {
        return layout;
    }
}
