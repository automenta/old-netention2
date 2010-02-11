/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.io;

import automenta.netention.Memory;
import automenta.netention.node.Channel;
import automenta.netention.node.AgentRef;
import automenta.netention.node.Message;
import automenta.netention.edge.CreatedBy;
import automenta.netention.edge.Retweets;
import automenta.netention.edge.MentionedBy;
import automenta.netention.edge.Creates;
import automenta.netention.edge.Mentions;
import automenta.netention.edge.RetweetedBy;
import automenta.netention.nlp.en.POSTagger;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import twitter4j.Status;
import twitter4j.User;

/**
 *
 * @author seh
 */
public class Twitter extends twitter4j.Twitter implements Sends {
    private final POSTagger tagger;

    public Twitter(POSTagger tagger) {
        super();

        this.tagger = tagger;
    }

    @Override
    public String toString() {
        return "Twitter";
    }


    protected void updateMentions(final Memory pg, Status s, final Message m) {
        final List<String> users = new LinkedList();
        List<String> tags = new LinkedList();

        String t = s.getText();
        StringTokenizer st = new StringTokenizer(t, " ");
        while (st.hasMoreTokens()) {
            String x = st.nextToken();
            if (x.startsWith("@")) {
                users.add(x);
            } else if (x.startsWith("#")) {
                tags.add(x);
            }
        }


        for (String x : tags) {
            //Channel c = new Channel(x);
            Channel h = new Channel(x);
            pg.addVertex(h);
            pg.addEdge(new Mentions(), m, h);
            pg.addEdge(new MentionedBy(), h, m);
        }

        if (users.size() > 0) {
            for (String us : users) {
                AgentRef a = new AgentRef(us, null);
                pg.addVertex(a);
                pg.addEdge(new Mentions(), m, a);
                pg.addEdge(new MentionedBy(), a, m);
            }

//            new Thread(new Runnable() {
//
//                @Override public void run() {
//                    for (String us : users) {
//                        try {
//                            User u = showUser(us);
//                            Agent a = new Agent(u.getName(), u.getProfileImageURL());
//                            if (u != null) {
//                                pg.graph.addVertex(a);
//                                pg.graph.addEdge(new Mentions(), m, a);
//                            }
//                        } catch (TwitterException ex) {
//                            Logger.getLogger(Twitter.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            }).start();
        }

        try {
            tagger.tag(m, s.getText(), pg);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void addStatus(Memory pg, Status s) {
        Message m = new Message(s.getText(), null);

        pg.addVertex(m);

        AgentRef ac = new AgentRef(s.getUser().getName(), s.getUser().getProfileImageURL());
        pg.addEdge(new Creates(), ac, m);
        pg.addEdge(new CreatedBy(), m, ac);

        if (s.isRetweet()) {
            //Concept src = addAuthor(pg, s.getRetweetDetails().getRetweetingUser());
            User u = s.getRetweetDetails().getRetweetingUser();
            AgentRef a = new AgentRef(u.getName(), u.getProfileImageURL());

            pg.addEdge(new Retweets(), m, a);
            pg.addEdge(new RetweetedBy(), a, m);
            
            pg.addEdge(new Creates(), a, m);
            pg.addEdge(new CreatedBy(), m, a);

        }

        updateMentions(pg, s, m);
    }

    public void getPublicTimeline(Memory pg) throws Exception {
        for (Status s : getPublicTimeline()) {
            addStatus(pg, s);
        }
    }

    public boolean canSend(Message m) {
        //TODO more aggressive twitter uid pattern matching here
        return m.getTo().startsWith("@");
    }

    public void send(Message m, Async a) {
        a.onError(new Exception("TODO send twitter"));
    }
}
