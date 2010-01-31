/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.net;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

/**
 *
 * @author seh
 */
public class PlexusGraph {

    public DirectedSparseGraph graph = new DirectedSparseGraph();

    int pagerankIterations = 100;

    public PageRank<Object,Double> getConcepts() {
//        BetweennessCentrality pr = new BetweennessCentrality(graph, true, false);
//        pr.setRemoveRankScoresOnFinalize(false);
//        pr.setMaximumIterations(100); //set up conditions
//        pr.evaluate();
//        try {
//            pr.printRankings(true, true);
//        } catch (NullPointerException e) {
//        }
//

        final PageRank p = new PageRank(graph, 0.2);
        p.setMaxIterations(pagerankIterations);
        p.acceptDisconnectedGraph(true);
        p.evaluate();

//        SortedMap<Object, Double> m = new TreeMap(new Comparator() {
//            public int compare(Object o1, Object o2) {
//                double a = (Double)p.getVertexScore(o1);
//                double b = (Double)p.getVertexScore(o2);
//                if (a < b) {
//                    return 1;
//                }
//                else if (a == b) {
//                    return 0;
//                }
//                else {
//                    return -1;
//                }
//            }
//        });
//
//        for (Object o : graph.getVertices()) {
//            System.out.println("pr: " + o + " -> " + p.getVertexScore(o));
//
//        }

        return p;
    }
}