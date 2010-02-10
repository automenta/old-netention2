/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swingui;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author seh
 */
public class PageRankPanel extends JPanel {
    private final DirectedGraph graph;
    double  alpha = 0.15;
    int maxIterations = 1000;
    int maxShown = 100;
    DefaultListModel results = new DefaultListModel();

    public PageRankPanel(DirectedGraph g) {
        super(new BorderLayout());

        this.graph = g;
        


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


    public Transformer<Object,Double> getEdgeWeights() {
        return new Transformer<Object,Double>() {
            public Double transform(Object o) {
                return 1.0;
            }
        };
    }
    protected void refresh() {
        removeAll();

        JPanel controlPanel = new JPanel();

        JButton rankButton = new JButton("Rank");
        rankButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        controlPanel.add(rankButton);

        add(controlPanel, BorderLayout.NORTH);

        JList resultPanel = new JList(results);
        add(resultPanel, BorderLayout.SOUTH);

        PageRank pr = new PageRank(graph, getAlpha());
        pr.setEdgeWeights(getEdgeWeights());
        pr.setMaxIterations(getMaxIterations());

        final Map<Object, Double> vertScores = new HashMap();
        for (Object v : graph.getVertices()) {
            Object o = pr.getVertexScore(v);
            if (o!=null) {
                double s = (Double)o;
                vertScores.put(v, s);
            }
        }
        System.out.println("page rank: " + vertScores);
        
        List<Object> vl = new LinkedList(vertScores.keySet());

        Collections.sort(vl, new Comparator() {
            public int compare(Object o1, Object o2) {
                double s1 = vertScores.get(o1);
                double s2 = vertScores.get(o2);
                if (s1 == s2)
                    return 0;
                else if (s1 > s2)
                    return 1;
                else
                    return -1;
            }
        });


        results.removeAllElements();
        for (int i = 0; i < Math.min(maxShown, vl.size()); i++) {
            results.addElement(vl.get(i));
        }

        updateUI();
    }


}
