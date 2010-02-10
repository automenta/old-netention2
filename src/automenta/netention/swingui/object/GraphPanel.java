package automenta.netention.swingui.object;

import automenta.netention.node.Message;
import automenta.netention.swingui.ObjectListCellRenderer;
import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter.EdgeType;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

    public GraphPanel(Graph graph, Object focus) {
        super(new BorderLayout());

        this.graph = graph;
        this.focus = focus;

        controlPanel = new JPanel(new FlowLayout());

        JButton growButton = new JButton("grow");
        growButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateHops(1);
            }
        });
        JButton shrinkButton = new JButton("shrink");
        shrinkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateHops(-1);
            }
        });

        controlPanel.add(growButton);
        controlPanel.add(shrinkButton);

        Set<Class> presentVertexTypes = new HashSet();
        presentVertexTypes.add(Message.class);
        
        JPanel typesPanel = new JPanel();
        typesPanel.setLayout(new BoxLayout(typesPanel, BoxLayout.LINE_AXIS));
        for (Class c : presentVertexTypes) {
            JToggleButton b = new JToggleButton(c.getSimpleName());
            b.setEnabled(true);
            typesPanel.add(b);
        }
        add(typesPanel);

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
            public boolean evaluate(Object arg0) {
                return true;
            }
        };
    }

    public void refresh() {
        removeAll();

        Graph subgraph = new KNeighborhoodFilter(focus, subgraphHops, EdgeType.IN_OUT).transform(graph);
        //Graph subgraph2 = new EdgePredicateFilter(newEdgePredicate()).transform(subgraph);
        //Graph subgraph3 = new VertexPredicateFilter(newVertexPredicate()).transform(subgraph2);

        layout = new SpringLayout(subgraph);
        
        layout.setSize(new Dimension(400, 400));
        VisualizationViewer vv = new VisualizationViewer(layout);
        //vv.setPreferredSize(new Dimension(350, 350));
        // Show vertex and edge labels
        vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<Object, Paint>() {

            public Paint transform(Object o) {
                Paint p = Color.getHSBColor(ObjectListCellRenderer.getHue(o), 0.7f, 0.7f);
                return p;
            }
        });
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        // Create a graph mouse and add it to the visualarization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        //gm.setMode(ModalGraphMouse.Mode.EDITING);
        vv.setGraphMouse(gm);

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(vv), BorderLayout.CENTER);        
        
        updateUI();
    }

    public SpringLayout getGraphLayout() {
        return layout;
    }
}
