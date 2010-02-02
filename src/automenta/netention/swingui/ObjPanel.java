package automenta.netention.swingui;

import automenta.netention.swingui.pattern.PatternPanel;
import automenta.netention.node.Detail;
import automenta.netention.Self;
import automenta.netention.Pattern;
import automenta.netention.swingui.detail.DetailPanel;
import automenta.netention.swingui.detail.GraphPanel;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter.EdgeType;
import edu.uci.ics.jung.graph.Graph;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class ObjPanel extends JPanel {

    private final Self self;

    public ObjPanel(Self self) {
        super(new BorderLayout());
        this.self = self;
    }

//    public void setDetail(Detail d) {
//        removeAll();
//        updateUI();
//    }
//    public void setAgent(Agent a) {
//        removeAll();
//        add(new AgentPanel(network, a), BorderLayout.CENTER);
//        updateUI();
//    }
    public void setPattern(Pattern p) {
        removeAll();
        add(new PatternPanel(self, p), BorderLayout.CENTER);
        updateUI();
    }

    public void setDetail(Detail d) {
        removeAll();
        add(new DetailPanel(self, d), BorderLayout.CENTER);
        updateUI();
    }

    public void setGraphPanel(Object o) {
        removeAll();

        if (!self.getMemory().graph.containsVertex(o)) {
        }
        else {

            int subgraphHops = 3;
            Graph subgraph = new KNeighborhoodFilter(o, subgraphHops, EdgeType.IN_OUT).transform(self.getMemory().graph);
            //graphPanel.getGraphLayout().setGraph(subgraph);

            add(new GraphPanel(subgraph), BorderLayout.CENTER);
        }
        updateUI();
    }

    void setObject(Object o) {
        if (o instanceof Detail) {
            setDetail((Detail) o);
        } else if (o instanceof Pattern) {
            setPattern((Pattern) o);
        } else {
            setGraphPanel(o);
        }


    }
}

