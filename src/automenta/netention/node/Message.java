package automenta.netention.node;

import java.net.URL;

public class Message {

    public final String title;
    public final String text;
    public final URL image;
    public final String source;
    private String to;

    public Message(String text) {
        this(text, null);
    }

    public Message(String text, URL image) {
        this(null, text, null, image);
    }
    
    public Message(String title, String text, String source, URL image) {
        super();
        this.title = title;
        this.text = text;
        this.source = source;
        this.image = image;
    }

    @Override
    public String toString() {
        if (title!=null)
            return title;
        return text;
    }

    public void setTo(String to) {
        this.to = to;
    }
    
    public String getTo() {
        return to;
    }



}
