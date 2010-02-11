/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.object;

import automenta.netention.Action;
import automenta.netention.Self;
import automenta.netention.io.Async;
import automenta.netention.io.HTML;
import automenta.netention.node.Link;
import automenta.netention.node.Message;
import automenta.netention.swingui.EQListPanel;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter.EdgeType;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author seh
 */
public class SummaryPanel extends JPanel {

    private final DirectedSparseGraph g;
    private Graph subgraph;
    private final Object o;
    private final Self self;

    public SummaryPanel(Self s, Object root) {
        super(new BorderLayout());

        this.self = s;
        this.o = root;
        this.g = s.getMemory();

        refresh();
    }

    protected void refresh() {
        removeAll();



        int subgraphHops = 3;
        subgraph = new KNeighborhoodFilter(o, subgraphHops, EdgeType.IN_OUT).transform(g);

        addActions();

        addSummary();

        addLinks();

        //links
        //  in|out
        //  {edge types}
        //  {vertex types}
        //  max distance

        updateUI();
    }

    public void addActions() {
        JMenuBar menu = new JMenuBar();
        add(menu, BorderLayout.NORTH);

        JMenu actions = new JMenu("Action");
        menu.add(actions);

        if (o instanceof Link) {
            final Link l = (Link)o;
            JMenuItem spiderThis = new JMenuItem("Spider this");
            actions.add(spiderThis);
            spiderThis.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    self.run(new Action("Loading " + l.url) {
                        public Object call() throws Exception {
                            return new HTML(self, l.url);
                        }
                    }, new Async() {
                        public void onFinished(Object result) {
                        }
                        public void onError(Exception e) {
                        }
                    });
                }
            });
        }
    }

    public static String getText(Object o) {
        if (o instanceof Message) {
            Message m = (Message)o;
            String h = m.title + "\n\n" + m.text;
            return h;
        }

        return o.toString();
    }
    public void addSummary() {
        JTextArea j = new JTextArea(getText(o));
        j.setFont(EQListPanel.FontH1);
        j.setEditable(false);
        j.setLineWrap(true);
        j.setWrapStyleWord(true);
        add(new JScrollPane(j), BorderLayout.CENTER);
    }

    public void addLinks() {
        JPanel linksPanel = new JPanel();
        add(linksPanel, BorderLayout.SOUTH);
    }
}
