/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.botpanel;

import automenta.netention.Self;
import automenta.netention.io.HTML;
import automenta.netention.node.Concept;
import automenta.netention.node.Contactable;
import automenta.netention.node.Message;
import automenta.netention.swingui.EQListPanel;
import automenta.netention.swingui.object.ContactRankPanel;
import automenta.netention.swingui.util.SwingWindow;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter.EdgeType;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javolution.context.ConcurrentContext;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;

/**
 *
 * @author seh
 */
public class MatchBotPanel extends JPanel implements Runnable {

    private final JTextArea logArea;
    private final JPanel runPanel;
    private final BotControlPanel controlPanel;
    private List<String> sources = new LinkedList();
    private Thread thread;
    private boolean running;
    private long sleepPeriodMS = 1000;
    private final Self self;
    private final JLabel statLabel;
    int maxConceptNeighborhoodHops = 2;
    //int maxVertices = 1000;
    //int maxEdges = 3000;
    double alpha = 0.2;
    int maxIterations = 12;
    double fractionToRemove = 0.05;

    public MatchBotPanel(Self self) {
        super(new BorderLayout());

        this.self = self;

        controlPanel = new BotControlPanel(this);
        add(controlPanel, BorderLayout.NORTH);

        logArea = new JTextArea();
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        runPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statLabel = new JLabel("");
        runPanel.add(statLabel);

        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                filterConcepts();
            }
        });
        runPanel.add(filterButton);

        final JToggleButton runButton = new JToggleButton("Run");
        runButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (runButton.isSelected()) {
                    runButton.setText("Stop");
                    start();
                } else {
                    runButton.setText("Run");
                    stop();
                }
            }
        });
        runPanel.add(runButton);
        add(runPanel, BorderLayout.SOUTH);

        add(new EQListPanel(self), BorderLayout.WEST);
    }

    protected void log(String s) {
        logArea.setText(logArea.getText() + "\n" + s);
    }

    public void addSource(String source) {
        sources.add(source);
        log("Added source: " + source);
    }

    protected synchronized void start() {
        if (thread != null) {
            return;
        }

        thread = new Thread(this);
        thread.start();
    }

    protected void stop() {
        running = false;
        thread = null;
    }

    public void run() {
        running = true;
        while (running) {
            if (sources.size() > 0) {
                String s = sources.get(0);
                sources.remove(s);

                log("Loading " + s);
                new HTML(self, self.getMemory(), s);

            }
            if (needsToFilter()) {
                filter(self.getMemory(), 0.2, 2048, 0.9);
            }

            try {
                Thread.sleep(sleepPeriodMS);
            } catch (InterruptedException ex) {
            }

            String l = self.getMemory().getVertexCount() + " " + self.getMemory().getEdgeCount();
            statLabel.setText(l);
        }
    }

    protected void updateConcepts() {

//        List<Concept> concepts = new LinkedList();
//        for (Object o : self.getMemory().getVertices()) {
//            if (o instanceof Concept) {
//                concepts.add((Concept)o);
//            }
//        }

        //rank concepts

        final PageRank pr = new PageRank(self.getMemory(), alpha);
        pr.setMaxIterations(maxIterations);
        pr.acceptDisconnectedGraph(true);
        pr.initialize();
        pr.evaluate();

//        double minScore = 0, maxScore = 0;
//        int i = 0;
//        for (Object v : self.getMemory().getVertices()) {
//            if (!(v instanceof Concept))
//                return v;
//            double s = getScore(pr, v);
//            if (i == 0) {
//                minScore = maxScore = s;
//            } else {
//                if (s < minScore) {
//                    minScore = s;
//                }
//                if (s > maxScore) {
//                    maxScore = s;
//                }
//            }
//            i++;
//        }

        List sortedConcepts = new ArrayList(self.getMemory().getVertices());
        CollectionUtils.filter(sortedConcepts, new Predicate() {

            public boolean evaluate(Object t) {
                return (t instanceof Concept);
            }
        });
        Collections.sort(sortedConcepts, new PageRankScoreComparator(pr));

        final ContactRankPanel rp = new ContactRankPanel(self);
        new SwingWindow(rp, 500, 500, false);

        ConcurrentContext.setConcurrency((Runtime.getRuntime().availableProcessors()) - 1);

        final Map<Concept, Set<Message>> conceptMessages = new HashMap();
        final Map<Concept, Set<Contactable>> conceptContacts = new HashMap();

        ConcurrentContext.enter();
        try {
            for (final Object o : sortedConcepts) {

                ConcurrentContext.execute(new Runnable() {

                    public void run() {
                        Concept c = (Concept) o;

                        Set<Contactable> contactables = new HashSet();
                        Set<Message> messages = new HashSet();
                        boolean gotContacts = false;
                        boolean gotMessages = false;

                        for (int i = 1; i <= maxConceptNeighborhoodHops; i++) {
                            Graph cNeighborhood = new KNeighborhoodFilter(c, i, EdgeType.IN_OUT).transform(self.getMemory());

                            for (Object n : cNeighborhood.getVertices()) {
                                if (!gotContacts) {
                                    if (n instanceof Contactable) {
                                        contactables.add((Contactable) n);
                                    }
                                }
                                if (!gotMessages) {
                                    if (n instanceof Message) {
                                        messages.add((Message) n);
                                    }
                                }
                            }

                            gotMessages = (messages.size() > 0);
                            gotContacts = (contactables.size() > 0);

                            //only go to next neighborhood size if any contactables or messages are not found yet
                            if ((contactables.size() > 0) && (messages.size() > 0)) {
                                break;
                            }

                        }
                        
                        conceptContacts.put(c, contactables);
                        conceptMessages.put(c, messages);

                    }
                });
            }
        } finally {
            ConcurrentContext.exit();
        }
        
        for (final Object o : sortedConcepts) {
            rp.addObject(o, getScore(pr, o), conceptContacts.get((Concept) o), conceptMessages.get((Concept) o));
        }

//        for (Object o : sortedConcepts) {
//            rp.addObject(o, getScore(pr, o));
//        }


    }

    protected void logConcept(Concept c) {
        log(c.toString());
    }

    protected boolean needsToFilter() {
        return false; //(self.getMemory().getVertexCount() > maxVertices) || (self.getMemory().getEdgeCount() > maxEdges);
    }

    public class PageRankScoreComparator implements Comparator {

        private final PageRank pr;

        public PageRankScoreComparator(PageRank pr) {
            this.pr = pr;
        }

        public int compare(Object a, Object b) {
            double as = getScore(pr, a);
            double bs = getScore(pr, b);
            if (as == bs) {
                return 0;
            } else if (as < bs) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    protected void filterConcepts() {
        filter(self.getMemory(), alpha, maxIterations, fractionToRemove);
    }

    protected void filter(DirectedGraph graph, double alpha, int maxIterations, double fractionToRemove) {
        final PageRank pr = new PageRank(graph, alpha);
        pr.setMaxIterations(maxIterations);
        pr.acceptDisconnectedGraph(true);
        pr.initialize();
        pr.evaluate();

        double minScore = 0, maxScore = 0;
        int i = 0;
        for (Object v : graph.getVertices()) {
            double s = getScore(pr, v);
            if (i == 0) {
                minScore = maxScore = s;
            } else {
                if (s < minScore) {
                    minScore = s;
                }
                if (s > maxScore) {
                    maxScore = s;
                }
            }
            i++;
        }

        List sortedVertices = new ArrayList(graph.getVertices());
        Collections.sort(sortedVertices, new PageRankScoreComparator(pr));

        int numVertices = 0; //graph.getVertexCount();
        for (Object v : graph.getVertices()) {
            if (isRemovable(v)) {
                numVertices++;
            }
        }


        int numToRemove = (int) ((fractionToRemove) * numVertices);
        List toRemove = new LinkedList();

        for (Object v : sortedVertices) {
            if (!isRemovable(v)) {
                continue;
            }

            if (numToRemove > 0) {
                toRemove.add(v);
                numToRemove--;
            } else {
            }
        }

        for (Object o : toRemove) {
            graph.removeVertex(o);
        }

        log("Filter removed " + toRemove.size() + " concepts");
    }

    /** only remove concepts */
    public boolean isRemovable(Object o) {
        if (!(o instanceof Concept)) {
            return false;
        }

//        if (o instanceof POSTagger)
//            return false;
//        if (o instanceof SMTP)
//            return false;
//        if (o instanceof Twitter)
//            return false;

        return true;
    }

    public Self getSelf() {
        return self;
    }

    static double getScore(PageRank pr, Object o) {
        Double score = (Double) pr.getVertexScore(o);
        double s = 0;
        if (score != null) {
            s = score;
        }
        return s;
    }
}
