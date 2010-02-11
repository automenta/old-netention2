/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swingui;

import automenta.netention.node.Message;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author seh
 */
public class MessageBuilderPanel extends JPanel {

    DefaultListModel selected = new DefaultListModel();
    private final JTextArea introPanel;
    
    public MessageBuilderPanel(List<Message> parts) {
        super(new BorderLayout());

        JSplitPane a = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        JPanel partList = new JPanel();
        partList.setLayout(new BoxLayout(partList, BoxLayout.PAGE_AXIS));
        for (final Message m : parts) {
            JButton mb = new JButton(m.title);
            mb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addPart(m);
                }
            });
            partList.add(mb);

        }
        a.setLeftComponent(new JScrollPane(partList));

        JPanel messageParts = new JPanel(new BorderLayout());
        JList selectedList = new JList(selected);
        messageParts.add(new JScrollPane(selectedList), BorderLayout.CENTER);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        messageParts.add(clearButton, BorderLayout.SOUTH);

        a.setRightComponent(messageParts);
        a.setDividerLocation(0.5);

        add(a, BorderLayout.CENTER);

        introPanel = new JTextArea(3,40);
        introPanel.setFont(introPanel.getFont().deriveFont(introPanel.getFont().getSize()*1.5f));
        introPanel.setBorder(new EmptyBorder(4,4,4,4));
        add(introPanel, BorderLayout.NORTH);
        
    }

    protected void addPart(Message m) {
        selected.addElement(m);
    }

    protected void clear() {
        selected.removeAllElements();
    }

    public List<Message> getMessages() {
        List<Message> l = new LinkedList();
        for (int i = 0; i < selected.size(); i++) {
            l.add( (Message)selected.getElementAt(i) );
        }
        return l;
    }

    public String getIntroText() {
        return introPanel.getText();
    }
}
