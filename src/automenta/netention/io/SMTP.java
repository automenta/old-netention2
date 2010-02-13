package automenta.netention.io;

import automenta.netention.node.Message;

public class SMTP implements Sends {

    public final String emailHost;
    public final String emailName;
    public final String senderEmail;
    public final String passwd;
    private final boolean ssl;

    public SMTP(String emailHost, String emailName, String senderEmail, String passwd, boolean ssl) {
        super();
        this.emailHost = emailHost;
        this.emailName = emailName;
        this.senderEmail = senderEmail;
        this.passwd = passwd;
        this.ssl = ssl;
    }

    public boolean canSend(Message m) {
        String addr = m.getTo();
        if (addr == null)
            return false;
        
        //TODO use more specific pattern matching
        return addr.contains("@");
    }

    public void send(Message m, Async async) {
        new SMTPSend(m, this, async);
    }

    @Override
    public String toString() {
        return "E-Mail";
    }

    public boolean isSSL() {
        return ssl;
    }


    
}
