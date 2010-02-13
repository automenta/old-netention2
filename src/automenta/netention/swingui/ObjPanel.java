package automenta.netention.swingui;

import automenta.netention.swingui.object.PageRankPanel;
import automenta.netention.swingui.pattern.PatternPanel;
import automenta.netention.node.Detail;
import automenta.netention.Self;
import automenta.netention.Pattern;
import automenta.netention.io.Sends;
import automenta.netention.node.Message;
import automenta.netention.swingui.object.DetailPanel;
import automenta.netention.swingui.object.SendPanel;
import automenta.netention.swingui.object.GraphPanel;
import automenta.netention.swingui.object.SummaryPanel;
import automenta.netention.swingui.object.NeighborhoodPanel;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

public class ObjPanel extends JPanel {

    private final Self self;
    private final JTabbedPane tabs;
    int borderThickness = 8;
    private final DirectedSparseGraph graph;

    public ObjPanel(Self self) {
        super(new BorderLayout());
        this.self = self;
        this.graph = self.getMemory();

        tabs = new JTabbedPane();
        add(tabs, BorderLayout.CENTER);

    }

    public ObjPanel(Self self, Object o) {
        this(self);
        setObject(o);
    }

    public void setObject(Object o) {
        float h = ObjectListCellRenderer.getHue(o);
        Color c = Color.getHSBColor(h, 0.5f + 0.5f, 0.5f + 0.5f);

        setBorder(new LineBorder(c, borderThickness));

        if (o instanceof Detail) {
            tabs.add("Edit Details", new DetailPanel(self, (Detail) o));
        }
        if (o instanceof Pattern) {
            tabs.add("Pattern", new PatternPanel(self, (Pattern) o));
        }

        tabs.add("Summary", new SummaryPanel(self, o));

        
        if (graph.containsVertex(o)) {
            tabs.add("Graph", new GraphPanel(self, graph, o));
            tabs.add("Paths", new NeighborhoodPanel(self, o));
        }

        tabs.add("Radar", new PageRankPanel(self, self.getMemory(), o));

        tabs.add("Send", new SendPanel(self.getAll(Sends.class), getMessages(o)));

    }

    public List<Message> getMessages(Object o) {
        LinkedList<Message> lm = new LinkedList();

        lm.add(new Message(o.toString(), "", null));
        lm.add(new Message("Type", "it's a " + o.getClass().getSimpleName(), null));

        if (graph.containsVertex(o)) {
            lm.add(new Message("Inputs", "inputs from " + graph.getPredecessors(o), null));
            lm.add(new Message("Outputs", "outputs to " + graph.getSuccessors(o), null));
        }

        return lm;
    }
}

