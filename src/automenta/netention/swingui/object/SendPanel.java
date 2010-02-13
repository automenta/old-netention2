package automenta.netention.swingui.object;

import automenta.netention.io.Async;
import automenta.netention.swingui.*;
import automenta.netention.io.Sends;
import automenta.netention.node.Contactable;
import automenta.netention.node.Message;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SendPanel extends JPanel {

    private MessageBuilderPanel msgPanel;
    private JButton sendButton;
    private JTextArea toField;
    private JTextField subjField;
    private final Collection<Sends> sends;
    //private final SMTP emailOut;

    public SendPanel(Collection<Sends> sends, Collection<Message> messageParts) {
        super(new BorderLayout());

        this.sends = sends;

        AudiencePanel audience = new AudiencePanel();
        add(audience, BorderLayout.NORTH);
        msgPanel = new MessageBuilderPanel(messageParts);
        add(msgPanel, BorderLayout.CENTER);
        ActionPanel sendPanel = new ActionPanel();
        add(sendPanel, BorderLayout.SOUTH);
    }

    void addRecipient(Contactable c) {
        String addr = c.url;
        toField.setText(addr + "\n" + toField.getText());
    }

    void addMessage(Message m) {
        msgPanel.addMessage(m);
    }

    public class AudiencePanel extends JPanel {

        public AudiencePanel() {
            super(new GridBagLayout());
            JLabel toLabel = new JLabel("To:");
            JLabel subjLabel = new JLabel("Subj:");
            toField = new JTextArea(4, 32);
            subjField = new JTextField(32);
            GridBagConstraints gc = new GridBagConstraints();
            gc.gridx = 0;
            gc.gridy = 0;
            add(toLabel, gc);
            gc.gridx = 0;
            gc.gridy = 1;
            add(subjLabel, gc);
            gc.gridx = 1;
            gc.gridy = 0;
            gc.weightx = 1.0;
            add(new JScrollPane(toField), gc);
            gc.gridx = 1;
            gc.gridy = 1;
            gc.weightx = 1.0;
            add(subjField, gc);
        }
    }

    public class ActionPanel extends JPanel {

        public ActionPanel() {
            super(new FlowLayout(FlowLayout.RIGHT));

            final JButton sendButton = new JButton("Send");
            sendButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    send(sends, sendButton);
                }
            });
            add(sendButton);

        }
    }
    String separator = "\n";

    public String wrapTitle(String title) {
        return "<h2>" + title + "</h2>";
    }

    protected void send(Collection<Sends> outs, final JButton sendButton) {
        List<Message> ml = msgPanel.getMessages();

        StringBuffer content = new StringBuffer();

        content.append(wrapTitle(msgPanel.getIntroText()) + "<br/><br/>");

        for (Message m : ml) {
            if (m.title != null) {
                content.append(wrapTitle(m.title));
            }
            if (m.text != null) {
                content.append(m.text);
            }
            content.append(separator);
        }

        String subject = subjField.getText();
        sendButton.setText("Sending...");
        sendButton.setEnabled(false);

        String[] toAddresses = toField.getText().split("\n");
        for (String toAddress : toAddresses) {
            toAddress = toAddress.trim();
            if (toAddress.length() == 0) {
                continue;
            }

            final Message m = new Message(subject, content.toString(), null);
            m.setTo(toAddress);

            for (Sends out : outs) {
                if (out.canSend(m)) {

                    out.send(m, new Async() {

                        public void onFinished(Object result) {
                            System.out.println(m.toString() + " sent.");
                            sendButton.setText("Sent...");
                            sendButton.setEnabled(true);
                        }

                        public void onError(Exception e) {
                            System.err.println(e);
                            sendButton.setText("Error Sending!");
                            sendButton.setEnabled(true);
                        }
                    });
                    System.out.println("sending " + m + " to " + out + " via " + m.getTo() + "...");
                }
                else {
                    //System.out.println("unable to send " + m + " to " + out + " via " + m.getTo());
                }

                //emailOut.emailName, emailOut.senderEmail, emailOut.passwd, emailOut.emailHost, toAddress, subject, message.toString()
            }
        }
    }
}
