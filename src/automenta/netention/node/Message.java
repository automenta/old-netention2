package automenta.netention.node;

import java.net.URL;

public final class Message {

    public final String title;
    public final String text;
    public final String source;
    private String to;

    public Message(String text) {
        this(text, text, null);
    }
    
    public Message(String title, String text, String source) {
        super();
        this.title = title;
        this.text = text;
        this.source = source;
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
