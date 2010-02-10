package automenta.netention.swingui.object;

import automenta.netention.io.Async;
import automenta.netention.swingui.*;
import automenta.netention.io.Sends;
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
import javax.swing.JTextField;

public class SendPanel extends JPanel {

    private MessageBuilderPanel msgPanel;
    private JButton sendButton;
    private JTextField toField;
    private JTextField subjField;
    private final Collection<Sends> sends;
    //private final SMTP emailOut;

    public SendPanel(Collection<Sends> sends, List<Message> messageParts) {
        super(new BorderLayout());

        this.sends = sends;

        AudiencePanel audience = new AudiencePanel();
        add(audience, BorderLayout.NORTH);
        msgPanel = new MessageBuilderPanel(messageParts);
        add(msgPanel, BorderLayout.CENTER);
        ActionPanel sendPanel = new ActionPanel();
        add(sendPanel, BorderLayout.SOUTH);
    }

    public class AudiencePanel extends JPanel {

        public AudiencePanel() {
            super(new GridBagLayout());
            JLabel toLabel = new JLabel("To:");
            JLabel subjLabel = new JLabel("Subj:");
            toField = new JTextField(32);
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
            add(toField, gc);
            gc.gridx = 1;
            gc.gridy = 1;
            gc.weightx = 1.0;
            add(subjField, gc);
        }
    }

    public class ActionPanel extends JPanel {

        public ActionPanel() {
            super(new FlowLayout(FlowLayout.RIGHT));

            for (final Sends s : sends) {
                final JButton sendButton = new JButton("Send to " + s.toString());
                sendButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        send(s, sendButton);
                    }
                });
                add(sendButton);
            }
            
        }
    }
    String separator = "\n";

    public String wrapTitle(String title) {
        return "<h2>" + title + "</h2>";
    }

    protected void send(Sends out, final JButton sendButton) {
        List<Message> ml = msgPanel.getMessages();
        StringBuffer content = new StringBuffer();
        for (Message m : ml) {
            if (m.title != null) {
                content.append(wrapTitle(m.title));
            }
            if (m.text != null) {
                content.append(m.text);
            }
            content.append(separator);
        }
        String toAddress = toField.getText();
        String subject = subjField.getText();

        sendButton.setText("Sending...");
        sendButton.setEnabled(false);

        final Message m = new Message(subject, content.toString(), null, null);
        m.setTo(toAddress);

        if (out.canSend(m)) {

            out.send(m, new Async() {

                public void onFinished(Object result) {
                    System.out.println(m.toString() + " sent.");
                    sendButton.setText("Sent.");
                    sendButton.setEnabled(true);
                }

                public void onError(Exception e) {
                    System.err.println(e);
                    sendButton.setText("Error Sending!");
                    sendButton.setEnabled(true);
                }
            });
        }

        //emailOut.emailName, emailOut.senderEmail, emailOut.passwd, emailOut.emailHost, toAddress, subject, message.toString()

    }
}
