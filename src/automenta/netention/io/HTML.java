/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.io;

import automenta.netention.Self;
import automenta.netention.edge.Mentions;
import automenta.netention.nlp.en.POSTagger;
import automenta.netention.node.Link;
import automenta.netention.node.Message;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;

/**
 *
 * @author seh
 */
public class HTML {

    private final DirectedSparseGraph graph;
    private final POSTagger tagger;
    private final Self self;
    private URL u;
    private List<Node> nodes = new LinkedList();
    private List<Link> links = new LinkedList();

    public HTML(Self self, String url) {
        this(self, self.getMemory().graph, url);
    }

    public HTML(Self self, DirectedSparseGraph targetGraph, String url) {
        super();
        this.self = self;
        this.graph = targetGraph;

        this.tagger = self.getThe(POSTagger.class);
        addURL(url);
    }

    public void addURL(String url) {
        Parser p;
        try {
            p = new Parser(url);
            p.setFeedback(null);

            u = new URL(url);
            graph.addVertex(u);

            NodeList nodes = p.parse(null);
            for (Node n : nodes.toNodeArray()) {
                addNode(url, null, n);
            }
        } catch (Exception ex) {
            Logger.getLogger(HTML.class.getName()).log(Level.SEVERE, null, ex);
        }

        String text = "";

        for (Node n : nodes) {
            if (n instanceof TextNode) {
                TextNode t = (TextNode) n;
                text += t.getText();
            }

            if (n instanceof TagNode) {
                TagNode tn = (TagNode) n;
                String tag = tn.getTagName().toLowerCase();
                if (tag.equals("a")) {
                    //extract link
                    String linkUrl = tn.getAttribute("href");
                    if (linkUrl != null) {
                        String linkText = tn.getAttribute("title");
                        if ((!linkUrl.startsWith("http:")) && (!linkUrl.startsWith("https:")) && (!linkUrl.startsWith("mailto:"))) {
                            linkUrl = url + linkUrl;
                        }
                        if (linkText == null) {
                            if (tn.getFirstChild() != null) {
                                linkText = tn.getFirstChild().getText();
                            }
                        }
                        if (linkText == null) {
                            linkText = linkUrl;
                        }

                        Link l = new Link(linkText, linkUrl);
                        links.add(l);
                    }
                }
            }
        }

        Message page = new Message(url, text, url, null);
        graph.addVertex(page);


        for (Link l : links) {
            graph.addVertex(l);
            graph.addEdge(new Mentions(), page, l);
        }

        if (tagger != null) {
            try {
                tagger.tag(page, text, graph);
            } catch (Exception e) {
            }
        }
        else {
            System.out.println("TAGGER is null");
            System.exit(1);
        }

    }

    public void addNode(String url, Object parent, Node n) {
        Object node = n;

        if (n instanceof TagNode) {
            if (isExcluding(((TagNode) n).getTagName().toLowerCase())) {
                return;
            }
        }

        nodes.add(n);

        if (n.getChildren() != null) {
            for (Node c : n.getChildren().toNodeArray()) {
                addNode(url, node, c);
            }
        }
    }

    /** excluding tags, at the top-level and all their children */
    public boolean isExcluding(String tagName) {
        if (tagName.equals("style")) {
            return true;
        }
        if (tagName.equals("script")) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Self s = new Self("xyz", "XYZ");
        try {
            s.addVertex(new POSTagger());
            new HTML(s, "http://baltimore.craigslist.org/cpg/1593964151.html");
        } catch (Exception ex) {
            Logger.getLogger(HTML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
