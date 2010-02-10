/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.io;

import automenta.netention.Self;
import automenta.netention.edge.Contains;
import automenta.netention.nlp.en.POSTagger;
import automenta.netention.node.Image;
import automenta.netention.node.Link;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Remark;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.NodeList;

/**
 *
 * @author seh
 */
public class HTMLTree {

    private final DirectedSparseGraph graph;
    private final POSTagger tagger;
    private final Self self;
    private URL u;
    
    public HTMLTree(Self self, String url) {
        this(self, self.getMemory().graph, url);
    }

    public HTMLTree(Self self, DirectedSparseGraph targetGraph, String url) {
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
            Logger.getLogger(HTMLTree.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addNode(String url, Object parent, Node n) {
        Object node = n;

        if (n instanceof Text) {
            Text t = (Text) n;
            String text = t.getText();
            text = text.trim();
            if (text.equals("")) {
                node = null;
            }

            node = text;

            if (tagger != null) {
                try {
                    tagger.tag(node, text, graph);
                } catch (Exception e) {
                }
            }

        } else if (n instanceof Tag) {
            Tag t = (Tag) n;

            String tagName = t.getTagName().toLowerCase();
            if (isExcluding(tagName)) {
                node = null;
            } else if (tagName.equals("a")) {
                String text = "", href = "";
                try {
                    text = href = t.getAttribute("href");
                    text = t.getFirstChild().getText();
                } catch (NullPointerException e) {
                }
                if (href == null) {
                    href = "link";
                }
                else {
                    if (!href.startsWith("http://")) {
                        href = u.toExternalForm() + "/" + href;
                    }
                }
                if (text == null) {
                    text = href;
                }
                node = new Link(text, href);
            } else if (tagName.equals("img")) {
                String src = t.getAttribute("src");
                if (src != null) {
                    String caption = t.getAttribute("alt");
                    node = new Image(src, caption);
                }
            } else {
                //TODO use HTMLTag class
                node = "htmltag:" + t.getTagName();
                //TODO recurse tag attributes
            }
        } else if (n instanceof Remark) {
            Remark r = (Remark) n;
            //TODO use HTMLComment class
            //node = "htmlcomment:" + r.getText();
            node = null;
            //...
        }

        if (node != null) {

            graph.addVertex(node);
            if (parent != null) {
                graph.addEdge(new Contains(), parent, node);
            } else {
                graph.addEdge(new Contains(), u, node);
            }

        }

        if (n.getChildren() != null) {
            for (Node c : n.getChildren().toNodeArray()) {
                addNode(url, node, c);
            }
        }
    }

    public boolean isExcluding(String tagName) {
        if (tagName.equals("div")) {
            return true;
        }
        if (tagName.equals("span")) {
            return true;
        }
        if (tagName.equals("br")) {
            return true;
        }
        return false;
    }
}
