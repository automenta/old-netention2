package automenta.netention.swingui.detail;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {

    private final SpringLayout layout;

    public GraphPanel(Graph subgraph) {
        super(new BorderLayout());
        layout = new SpringLayout(subgraph);
        layout.setSize(new Dimension(900, 700));
        VisualizationViewer<Integer, String> vv = new VisualizationViewer<Integer, String>(layout);
        //vv.setPreferredSize(new Dimension(350, 350));
        // Show vertex and edge labels
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
