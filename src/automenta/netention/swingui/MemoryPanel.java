/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui;

import automenta.netention.Action;
import automenta.netention.Self;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author seh
 */
public class MemoryPanel extends JPanel implements Runnable {

    private final Self self;
    private final DirectedSparseGraph graph;
    boolean running = true;
    long updatePeriodMS = 500;

    MemoryPanel(Self self) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.self = self;
        this.graph = self.getMemory().graph;

        new Thread(this).start();

    }

    protected void refresh() {
        removeAll();

        {
            add(new GraphSummaryPanel().newGraphSummaryPanel(graph));
        }

        {
            //list of processes
            DefaultListModel m = new DefaultListModel();
            JList processList = new JList(m);
            for (Action a : self.getRunningActions()) {
                m.addElement(a);
            }
            add(processList);
        }

        updateUI();
    }

    public void run() {
        while (running) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    refresh();
                }
            });

            try {
                Thread.sleep(updatePeriodMS);
            } catch (InterruptedException ex) {
            }
        }
    }
}
