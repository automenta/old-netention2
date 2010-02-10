package automenta.netention.swingui;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraphSummaryPanel {

    public JPanel newGraphSummaryPanel(DirectedSparseGraph graph) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Class, Integer> typeCounts = new HashMap();
        for (Object v : graph.getVertices()) {
            Class t = v.getClass();
            Integer c = typeCounts.get(t);
            int ci;
            if (c == null) {
                ci = 1;
            } else {
                ci = c + 1;
            }
            typeCounts.put(t, ci);
        }
        for (Class t : typeCounts.keySet()) {
            dataset.addValue(typeCounts.get(t), t.getSimpleName(), "Count");
        }
        JFreeChart fc = ChartFactory.createBarChart("Type Profile", "Types", "Count", dataset, PlotOrientation.HORIZONTAL, true, true, true);
        return new ChartPanel(fc);
    }
}
