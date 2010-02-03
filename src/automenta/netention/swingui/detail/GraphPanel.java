package automenta.netention.swingui.detail;

import automenta.netention.swingui.ObjectListCellRenderer;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import javax.swing.JPanel;
import org.apache.commons.collections15.Transformer;

public class GraphPanel extends JPanel {

    private final SpringLayout layout;

    public GraphPanel(Graph subgraph) {
        super(new BorderLayout());
        layout = new SpringLayout(subgraph);
        layout.setSize(new Dimension(900, 700));
        VisualizationViewer vv = new VisualizationViewer(layout);
        //vv.setPreferredSize(new Dimension(350, 350));
        // Show vertex and edge labels
        vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<Object,Paint>() {
            public Paint transform(Object o) {
                Paint p = Color.getHSBColor( ObjectListCellRenderer.getHue(o), 0.7f, 0.7f);
                return p;
            }
        });
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        // Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(gm);
        add(vv, BorderLayout.CENTER);
    }

    public SpringLayout getGraphLayout() {
        return layout;
    }
}
