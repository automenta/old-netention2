/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swingui.object;

import automenta.netention.Self;
import automenta.netention.io.Sends;
import automenta.netention.node.Contactable;
import automenta.netention.node.Message;
import automenta.netention.swingui.ObjectListCellRenderer;
import automenta.netention.swingui.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author seh
 */
public class ContactRankPanel extends JPanel {
    private final Self self;
    private final JPanel listPanel;


    public ContactRankPanel(Self s) {
        super(new BorderLayout());
        this.self = s;

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
        add(new JScrollPane(listPanel), BorderLayout.CENTER);
    }

    public void addObject(final Object concept, double score, final Collection<Contactable> contactables, final Collection<Message> messages) {
        JPanel j = new JPanel(new BorderLayout());
        
        String str = ObjectListCellRenderer.maxLength(concept.toString() + " " + score, 32);

        JButton b = new JButton(str);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contact(concept, contactables, messages);
            }
        });
        j.add(b, BorderLayout.NORTH);

        JTextArea jt = new JTextArea(contactables.toString());
        jt.setEditable(false);
        jt.setWrapStyleWord(true);
        jt.setLineWrap(true);
        j.add(jt, BorderLayout.CENTER);

        listPanel.add(j);
        
        updateUI();
    }

    protected void contact(Object concept, Collection<Contactable> contactables, Collection<Message> messages) {
        SendPanel sp = new SendPanel(self.getAll(Sends.class), self.getAll(Message.class));

        for (Contactable c : contactables)
            sp.addRecipient(c);
        for (Message m : messages)
            sp.addMessage(m);
        
        new SwingWindow(sp, 500, 500, false);
    }

}
