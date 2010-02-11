/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.object;

import automenta.netention.Self;
import automenta.netention.swingui.*;
import automenta.netention.edge.Next;
import automenta.netention.swingui.util.SwingWindow;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter.EdgeType;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author seh
 */
public class PageRankPanel extends JPanel {

    private final DirectedGraph graph;
    double alpha = 0.15;
    int subgraphRadius = 3;
    int maxIterations = 4000;
    int maxShown = 100;
    private final Object focus;
    private JPanel resultPanel;
    private Set<Class> typeClasses = new HashSet();
    private JPanel typesPanel;
    private Map<Class, Boolean> typeVisible = new HashMap();
    private final Self self;

    public PageRankPanel(Self self, DirectedGraph g, Object focus) {
        super(new BorderLayout());

        this.self = self;
        this.graph = g;
        this.focus = focus;

        refresh();
    }

    double getAlpha() {
        return alpha;
    }

    int getMaxIterations() {
        return maxIterations;
    }

    int getMaxShown() {
        return maxShown;
    }

    public Transformer<Object, Double> getEdgeWeights() {
        return new Transformer<Object, Double>() {

            public Double transform(Object o) {
//                if (o instanceof Next) {
//                    return 0.1;
//                }
                return 1.0;
            }
        };
    }

    protected void refresh() {
        removeAll();

        JPanel controlPanel = new JPanel(new BorderLayout());

        typesPanel = new JPanel(new FlowLayout());
        controlPanel.add(typesPanel, BorderLayout.CENTER);

        JButton rankButton = new JButton("Update");
        rankButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        controlPanel.add(rankButton, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.NORTH);

        resultPanel = new JPanel();
        add(new JScrollPane(resultPanel), BorderLayout.CENTER);

        updateRank();


        updateUI();
    }

    private void updateRank() {

        Graph subGraphX = new KNeighborhoodFilter(focus, subgraphRadius, EdgeType.IN_OUT).transform(graph);

        typeClasses.clear();
        for (Object v : subGraphX.getVertices()) {
            typeClasses.add(v.getClass());
            if (typeVisible.get(v.getClass())==null) {
                typeVisible.put(v.getClass(), false);
            }
        }


        PageRank pr = new PageRank(subGraphX, getAlpha());
        pr.setEdgeWeights(getEdgeWeights());
        pr.setMaxIterations(getMaxIterations());
        pr.acceptDisconnectedGraph(true);
        pr.initialize();
        pr.step();

        Graph subGraph = new VertexPredicateFilter(new Predicate() {
            public boolean evaluate(Object o) {
                Class c = o.getClass();
                if (typeVisible.get(c)!=null) {
                    return typeVisible.get(c);
                }
                return false;

            }
        }).transform(subGraphX);

        final Map<Object, Double> vertScores = new HashMap();
        for (Object v : subGraph.getVertices()) {
            Object o = pr.getVertexScore(v);
            if (o != null) {
                double s = (Double) o;
                vertScores.put(v, s);
            }
        }

        typesPanel.removeAll();
        for (final Class c : typeClasses) {
            final JToggleButton b = new JToggleButton(c.getSimpleName());
            if (typeVisible.get(c)!=null) {
                b.setSelected(typeVisible.get(c));
            }
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    typeVisible.put(c, b.isSelected());
                }
            });
            typesPanel.add(b);
        }
        typesPanel.updateUI();

        List<Object> vl = new LinkedList(vertScores.keySet());

        Collections.sort(vl, new Comparator() {

            public int compare(Object o1, Object o2) {
                double s1 = vertScores.get(o1);
                double s2 = vertScores.get(o2);
                if (s1 == s2) {
                    return 0;
                } else if (s1 > s2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        resultPanel.removeAll();
        resultPanel.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1.0;
        gc.fill = gc.HORIZONTAL;
        
        for (int i = 0; i < Math.min(maxShown, vl.size()); i++) {
            final Object x = vl.get(i);
            JPanel j = new JPanel(new BorderLayout());

            double score = vertScores.get(x);
            //TODO normalize score
            
            String s = ((int)(score*10000.0)) + " " + x.toString();
            String t = x.getClass().getSimpleName();

            JButton ls = new JButton(s);
            ls.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new SwingWindow(new ObjPanel(self, x), 400, 400);
                }
            });
            j.add(ls, BorderLayout.CENTER);
            ls.setFont(EQListPanel.FontH2);

            j.add(new JLabel(t), BorderLayout.SOUTH);

            j.setBorder(new EmptyBorder(4,4,4,4));
            
            resultPanel.add(j, gc);

            gc.gridy++;
        }
        resultPanel.updateUI();
    }
}
