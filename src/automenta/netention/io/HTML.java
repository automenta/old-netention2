/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.io;

import automenta.netention.Self;
import automenta.netention.edge.ReffedBy;
import automenta.netention.edge.Refs;
import automenta.netention.nlp.en.POSTagger;
import automenta.netention.node.Contactable;
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
    private List<Object> links = new LinkedList();

    public HTML(Self self, String url) {
        this(self, self.getMemory(), url);
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

            NodeList nodes = p.parse(null);
            for (Node n : nodes.toNodeArray()) {
                addNode(url, null, n);
            }
        } catch (Exception ex) {
            Logger.getLogger(HTML.class.getName()).log(Level.SEVERE, null, ex);
        }

        String text = "";
        String pageTitle = url;

        for (Node n : nodes) {
            if (n instanceof TextNode) {
                TextNode t = (TextNode) n;
                text += t.getText();
            }

            if (n instanceof TagNode) {
                TagNode tn = (TagNode) n;
                String tag = tn.getTagName().toLowerCase();

                if (tag.equals("title")) {
                    try {
                        pageTitle = tn.getFirstChild().getText();
                        //System.out.println(url + " has title " + pageTitle);
                    } catch (NullPointerException e) {
                    }
                } else if (tag.equals("a")) {
                    //extract link
                    String linkUrl = tn.getAttribute("href");
                    if (linkUrl != null) {
                        String linkText = tn.getAttribute("title");


                        if ((!linkUrl.startsWith("http:")) && (!linkUrl.startsWith("https:")) && (!linkUrl.startsWith("mailto:"))) {
                            if (linkUrl.startsWith("/")) {
                                linkUrl = getRootURL(url) + linkUrl;
                            } else {
                                linkUrl = url + linkUrl;
                            }
                        }

                        //System.out.println("encountered link: " + linkUrl);

                        //handle twitter urls
                        if (linkUrl.startsWith("http://twitter.com/")) {
                            String twitterUser = linkUrl.replace("http://twitter.com/", "");
                            if (twitterUser.length() > 0) {
                                //remove extra slash material
                                if (twitterUser.contains("/")) {
                                    int slashIndex = twitterUser.indexOf('/');
                                    twitterUser = twitterUser.substring(0, slashIndex);
                                }
                                if (isValidTwitterer(twitterUser)) {
                                    twitterUser = "@" + twitterUser;
                                    Contactable c = new Contactable(twitterUser, twitterUser);
                                    //System.out.println("  encountered twitterer: " + twitterUser);
                                    links.add(c);
                                }
                            }
                        }

                        Link l;
                        {

                            if (linkText == null) {
                                if (tn.getFirstChild() != null) {
                                    linkText = tn.getFirstChild().getText();
                                }
                            }
                            if (linkText == null) {
                                linkText = linkUrl;
                            }

                            if (linkUrl.startsWith("mailto:")) {
                                l = new Contactable(linkText, linkUrl);
                            } else {
                                l = new Link(linkText, linkUrl);
                            }
                        }

                        if (l != null) {
                            links.add(l);
                        }
                    }
                }
            }
        }

        Message page = new Message(pageTitle, text, url);

        synchronized (graph) {
            graph.addVertex(page);

            graph.addVertex(u);

            for (Object l : links) {
                graph.addVertex(l);
                graph.addEdge(new Refs(), page, l);
                graph.addEdge(new ReffedBy(), l, page);
            }
            if (tagger != null) {
                try {
                    tagger.tag(page, text, graph);
                } catch (Exception e) {
                }
            } else {
                System.out.println("TAGGER is null");
                System.exit(1);
            }

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
            new HTML(s, "http://twitter.com/sseehh");
        } catch (Exception ex) {
            Logger.getLogger(HTML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getRootURL(String url) {
        int lastSlash = url.lastIndexOf("/");
        if (lastSlash == -1) {
            return url;
        } else {
            return url.substring(0, lastSlash);
        }

    }

    public boolean isValidTwitterer(String twitterUser) {
        if ((!twitterUser.contains("?")) && (!twitterUser.contains("&")) && (!twitterUser.contains("#")) && (!twitterUser.contains("="))) {
            if (twitterUser.equals("about")) {
                return false;
            }
            if (twitterUser.equals("login")) {
                return false;
            }
            if (twitterUser.equals("signup")) {
                return false;
            }
            if (twitterUser.equals("statuses")) {
                return false;
            }
            if (twitterUser.equals("favorites")) {
                return false;
            }
            if (twitterUser.equals("help")) {
                return false;
            }
            if (twitterUser.equals("goodies")) {
                return false;
            }
            if (twitterUser.equals("jobs")) {
                return false;
            }
            if (twitterUser.equals("tos")) {
                return false;
            }
            if (twitterUser.equals("privacy")) {
                return false;
            }
            if (twitterUser.equals("twitter")) {
                return false;
            }
            if (twitterUser.equals("twitterapi")) {
                return false;
            }
            if (twitterUser.equals("twittermobile")) {
                return false;
            }
            if (twitterUser.equals("spam")) {
                return false;
            }
            if (twitterUser.equals("feedback")) {
                return false;
            }
            if (twitterUser.equals("support")) {
                return false;
            }
            if (twitterUser.equals("safety")) {
                return false;
            }
            if (twitterUser.equals("account")) {
                return false;
            }
            return true;
        }
        return false;
    }
}
