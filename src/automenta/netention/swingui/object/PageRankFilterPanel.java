/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.object;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.Graph;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author seh
 */
public class PageRankFilterPanel extends JPanel {

    boolean commiting = false;
    double minScore, maxScore;
    double threshold;
    double alpha = 0.2;
    private final Graph graph;
    int maxIterations = 1000;
    private final JTextArea status;
    private final JSlider percentToRemove;
    private final JTextField iterationsField;
    private PageRank pr;

    public PageRankFilterPanel(Graph g) {
        super(new BorderLayout());

        this.graph = g;

        status = new JTextArea();
        add(status, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new BorderLayout());
        {
            percentToRemove = new JSlider(0, 100);
            percentToRemove.setValue(0);
            controlPanel.add(percentToRemove, BorderLayout.CENTER);

            iterationsField = new JTextField(6);
            iterationsField.setText(Integer.toString(maxIterations));
            controlPanel.add(iterationsField, BorderLayout.WEST);
        }

        add(controlPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refresh(false);
            }
        });

        JButton commitButton = new JButton("Commit");
        commitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refresh(true);
            }
        });

        buttonPanel.add(updateButton);
        buttonPanel.add(commitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        refresh(false);

    }

    protected void refresh(boolean commiting) {
        maxIterations = Integer.decode(iterationsField.getText());

        pr = new PageRank(graph, alpha);
        //pr.setEdgeWeights(getEdgeWeights());
        pr.setMaxIterations(maxIterations);
        pr.acceptDisconnectedGraph(true);
        pr.initialize();
        pr.evaluate();


        int i = 0;
        for (Object v : graph.getVertices()) {
            double s = getScore(v);
            if (i == 0) {
                minScore = maxScore = s;
            } else {
                if (s < minScore) {
                    minScore = s;
                }
                if (s > maxScore) {
                    maxScore = s;
                }
            }
            i++;
        }

        threshold = (minScore) + 0.25 * (maxScore - minScore);

        //update label
        String st = "Min=" + minScore + ", Max=" + maxScore;
        status.setText(st);


        List sortedVertices = new ArrayList(graph.getVertices());
        Collections.sort(sortedVertices, new Comparator() {
            public int compare(Object a, Object b) {
                double as = getScore(a);
                double bs = getScore(b);
                if (as == bs)
                    return 0;
                else if (as < bs)
                    return 1;
                else
                    return -1;
            }
        });

        if (commiting) {
            int numVertices = graph.getVertexCount();
            int numToRemove = (int)((((float)percentToRemove.getValue())/100.0) * numVertices);
            List toRemove = new LinkedList();

            for (Object v : sortedVertices) {
                if (numToRemove == 0) {
                    toRemove.add(v);
                }
                else
                    numToRemove--;
            }
            System.out.println("Removing: " + toRemove);

            for (Object o : toRemove) {
                graph.removeVertex(o);
            }

        }



    }

    double getScore(Object o) {
        Double score = (Double) pr.getVertexScore(o);
        double s = 0;
        if (score != null) {
            s = score;
        }
        return s;
    }
}
