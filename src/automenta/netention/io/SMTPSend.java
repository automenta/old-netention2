/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.io;

import automenta.netention.node.Message;
import java.lang.Exception;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author seh
 */
class SMTPSend {

    //public SMTPSend(final String fromName, final String fromEmail, final String password, final String host, final String to, final String subject, final String message, final Async async) {
    public SMTPSend(final Message m, final SMTP s, final Async async) {

        new Thread(new Runnable() {

            public void run() {
                try {
                    HtmlEmail smtp = new HtmlEmail();
                    smtp.setHostName(s.emailHost);
                    smtp.setSSL(s.isSSL()); //set to false for hotmail, true for gmail
                    smtp.setTLS(true);
                    smtp.setAuthentication(s.senderEmail, s.passwd);

                    String toAddress = m.getTo();
                    if (toAddress.startsWith("mailto:")) {
                        toAddress = toAddress.replace("mailto:", "");
                    }
                    if (toAddress.contains("?")) {
                        toAddress = toAddress.substring(0, toAddress.indexOf("?"));
                    }

                    smtp.addTo(toAddress, toAddress);
                    smtp.setFrom(s.senderEmail, s.emailName);
                    smtp.setSubject(m.title);
                    smtp.setHtmlMsg(m.text);
                    smtp.send();
                    
                    async.onFinished(smtp);
                }
                catch (Exception e) {
                    async.onError(e);
                }
            }
            
        }).start();
    }

}
