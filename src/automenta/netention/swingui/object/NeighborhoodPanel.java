/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.object;

import automenta.netention.Self;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter.EdgeType;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author seh
 */
public class NeighborhoodPanel extends JPanel {

    //final DefaultTreeModel treeModel;
    DefaultListModel pathListModel = new DefaultListModel();
    private final DirectedSparseGraph g;
    private final Object root;
    int subgraphHops = 3;

    public class GraphTreeNode extends DefaultMutableTreeNode {

        public GraphTreeNode(Object edge, Object node, int depth) {
            super(edge + " " + node);

            if (depth == 0) {
                return;
            }

            Collection outEdges = g.getOutEdges(node);
            for (Object x : outEdges) {
                Object y = g.getOpposite(node, x);
                if (y != null) {
                    add(new GraphTreeNode("< " + x, y, depth - 1));
                }
            }

            Collection inEdges = g.getInEdges(node);
            for (Object x : inEdges) {
                Object y = g.getOpposite(node, x);
                if (y != null) {
                    add(new GraphTreeNode("> " + x, y, depth - 1));
                }
            }
        }
    }

    public NeighborhoodPanel(Self s, Object root) {
        super(new BorderLayout());

        this.root = root;
        this.g = s.getMemory();

        //DefaultMutableTreeNode rootNode = new GraphTreeNode("", root, 4);


        //this.treeModel = new DefaultTreeModel(rootNode);
        //JTree jt = new JTree(treeModel);
        //add(new JScrollPane(jt), BorderLayout.CENTER);

        JList pl = new JList(pathListModel);

        refresh();

        add(new JScrollPane(pl), BorderLayout.CENTER);

    }

    protected void refresh() {
        pathListModel.removeAllElements();

        Graph subgraph = new KNeighborhoodFilter(root, subgraphHops, EdgeType.IN_OUT).transform(g);
        
//        DijkstraShortestPath sp = new DijkstraShortestPath(g, true);
//                sp.setMaxDistance(6);
//                sp.setMaxTargets(6);

        for (Object v : subgraph.getVertices()) {
            if (v != root) {

//                path = sp.getPath(root, v);
//                System.out.println(" path: " + path);

                pathListModel.addElement(v);
            }
        }

    }
}
