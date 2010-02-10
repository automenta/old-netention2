/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.graph;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.Map;
import javolution.util.FastMap;

/**
 *
 * @author seh
 */
public class FastDirectedGraph<V,E> extends DirectedSparseGraph<V,E> {


    public FastDirectedGraph() {
        super();
        vertices = new FastMap<V, Pair<Map<V,E>>>();
        ((FastMap)vertices).setShared(true);
        edges  = new FastMap<E, Pair<V>>();
        ((FastMap)edges).setShared(true);
    }

    public void clear() {
        vertices.clear();
        edges.clear();
    }


    @Override public boolean addVertex(V vertex) {
        if(vertex == null) {
            throw new IllegalArgumentException("vertex may not be null");
        }

        if (!containsVertex(vertex)) {
            vertices.put(vertex, new Pair<Map<V,E>>(new FastMap<V,E>(), new FastMap<V,E>()));
            return true;
        } else {
            return true;
        }
    }

}
