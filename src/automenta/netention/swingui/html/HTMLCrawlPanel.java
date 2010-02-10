/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.html;

import automenta.netention.Action;
import automenta.netention.Self;
import automenta.netention.graph.FastDirectedGraph;
import automenta.netention.io.Async;
import automenta.netention.io.HTML;
import automenta.netention.node.Link;
import automenta.netention.swingui.GraphSummaryPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author seh
 */
abstract public class HTMLCrawlPanel extends JPanel {

    FastDirectedGraph graph;
    private final JPanel chartPanel;
    private final Self self;
    private final JTextArea urlPanel;
    private final JLabel statusLabel;
    private Set<String> fetchedURLS = new HashSet();

    public HTMLCrawlPanel(Self self) {
        this(self, "");
    }

    public HTMLCrawlPanel(Self self, String initialURLS) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.self = self;

        urlPanel = new JTextArea(64, 4);
        urlPanel.setText(initialURLS);
        add(new JScrollPane(urlPanel));


        this.chartPanel = new JPanel(new BorderLayout());
        add(chartPanel);

        JPanel actPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton updateButton = new JButton("Update");
        JButton commitButton = new JButton("Commit");
        JButton recurseButton = new JButton("Recurse");


        updateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                update();
            }
        });

        commitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                commit();
            }
        });

        recurseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                recurse();
            }
        });

        actPanel.add(updateButton);
        actPanel.add(recurseButton);
        actPanel.add(commitButton);

        add(actPanel);

        statusLabel = new JLabel("");
        add(statusLabel);
    }

    protected void update() {
        chartPanel.removeAll();

        graph = new FastDirectedGraph();

        String[] urls = urlPanel.getText().split("\n");
        fetchedURLS.clear();
        for (String ux : urls) {
            final String u = ux.trim();
            if (u.length() > 0) {
                fetchedURLS.add(u);
            }
        }

        self.run(new Action("Loading " + urls) {
            public Object call() throws Exception {
                for (String u : fetchedURLS) {
                    try {
                        new HTML(self, graph, u);
                    } catch (Exception e) {
                    }
                }
                return fetchedURLS;
            }
        }, new Async() {

            public void onFinished(Object result) {
                refresh();
            }

            public void onError(Exception ex) {
            }
        });

        refresh();
    }

    protected synchronized void refresh() {
        String t = graph.getVertexCount() + "v|" + graph.getEdgeCount() + "e";
        statusLabel.setText(t);

        chartPanel.removeAll();
        chartPanel.add(new GraphSummaryPanel().newGraphSummaryPanel(graph));

        updateUI();
    }

    protected void commit() {
        self.getMemory().add(graph);
        finished();
        graph.clear();
    }

    abstract protected void finished();

    protected void recurse() {
        for (Object v : graph.getVertices()) {
            if (v instanceof Link) {
                Link l = (Link) v;
                fetchedURLS.add(l.url);
            }
        }

        String n = "";
        for (String s : fetchedURLS) {
            n += s + "\n";
        }

//        new SwingWindow(new HTMLCrawlPanel(self, n) {
//
//            @Override protected void finished() {
//            }
//        }, 600, 600, false);
        urlPanel.setText(n);
    }
}
