/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.io;

import automenta.netention.Memory;
import automenta.netention.node.Message;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author seh
 */
public class RSSFeed {

    public final static String FeedType = "application/rss+xml";
    private URI uri;
    private String title = new String();
    private String summary = new String();
    private String description;

    List<Message> messages = new LinkedList();
    
    public RSSFeed(URI uri, Memory m) {
        super();

        this.uri = uri;

        updateFeed();

        for (Message x : getMessages()) {
            m.addVertex(x);
        }
    }

    public URI getURI() {
        return uri;
    }

    /**
     * @see https://rome.dev.java.net/
     * @see http://wiki.java.net/bin/view/Javawsxml/Rome04TutorialFeedReader
     */
    public void updateFeed() {
        try {
            URL feedUrl = getURI().toURL();

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            this.title = feed.getTitle();
            this.description = feed.getDescription();

            messages.clear();
            
            for (Object o : feed.getEntries()) {
                SyndEntry s = (SyndEntry) o;

                final String title = s.getTitle();
                final String uri = s.getUri();
                final String desc = s.getDescription().getValue();


                messages.add(new Message(title, desc, s.getUri()));

//                add(new Found() {
//
//                    @Override public String getDescription() {
//                        return desc;
//                    }
//
//                    @Override
//                    public String getName() {
//                        return title;
//                    }
//
//                    @Override
//                    public Object getObject() {
//                        return uuri;
//                    }
//
//                    @Override
//                    public double getStrength() {
//                        return 1.0;
//                    }
//
//                    @Override
//                    public String getTags() {
//                        return "item";
//                    }
//
//                    @Override
//                    public UURI getUURI() {
//                        return uuri;
//                    }
//                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public List<Message> getMessages() {
        return messages;
    }
    
    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }


}
