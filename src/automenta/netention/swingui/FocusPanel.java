/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui;

import automenta.netention.focus.Focus;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * edits a Focus
 */
abstract public class FocusPanel extends JPanel implements ChangeListener {

    private final Focus focus;
    private final JSlider whereSlider;
    private final JSlider whoSlider;
    private final JSlider detailAmountSlider;
    private final JSlider agentAmountSlider;
    private final JSlider metaAmountSlider;
    private final JSlider conceptAmountSlider;
    private final JSlider messageAmountSlider;
    private final JSlider whenSlider;
    private boolean suppressStateChange = false;
    private final JSlider otherAmountSlider;
    private final JSlider textAmountSlider;
    private final JTextField keywordField;

    public FocusPanel(Focus f) {
        super(new GridBagLayout());

        this.focus = f;

        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        {
            gc.gridwidth = 2;
            keywordField = new JTextField(16);
            keywordField.addKeyListener(new KeyAdapter() {

                @Override public void keyTyped(KeyEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            panelToFocus();
                        }
                    });
                }
            });

            add(keywordField, gc);
        }
        gc.gridy++;
        gc.gridx = 0;
        gc.gridwidth = 1;


        {
            add(new JLabel("Me"), gc);
            gc.gridx++;

            whoSlider = new JSlider(0, 100);
            whoSlider.addChangeListener(this);
            add(whoSlider, gc);
            gc.gridx++;

            add(new JLabel("Everyone"), gc);
            gc.gridx++;
        }
        gc.gridy++;
        gc.gridx = 0;

        {
            add(new JLabel("Past"), gc);
            gc.gridx++;

            whenSlider = new JSlider(0, 100);
            whenSlider.addChangeListener(this);
            add(whenSlider, gc);
            gc.gridx++;

            add(new JLabel("Future"), gc);
            gc.gridx++;
        }
        gc.gridy++;
        gc.gridx = 0;

        {
            add(new JLabel("Here"), gc);
            gc.gridx++;

            whereSlider = new JSlider(0, 100);
            whereSlider.addChangeListener(this);
            add(whereSlider, gc);
            gc.gridx++;

            add(new JLabel("There"), gc);
            gc.gridx++;
        }
        gc.gridy++;
        gc.gridx = 0;

        {
            JButton detailsButton = new JButton("Details");
            detailsButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    focus.clearAmounts();
                    focus.detailAmount = 1.0;
                    focusToPanel();
                    onFocusChanged();
                }
            });
            add(detailsButton, gc);
            gc.gridx++;

            gc.gridwidth = 2;
            detailAmountSlider = new JSlider(0, 100);
            detailAmountSlider.addChangeListener(this);
            add(detailAmountSlider, gc);
            gc.gridx++;
            gc.gridwidth = 1;


        }
        gc.gridy++;
        gc.gridx = 0;

        {
            JButton button = new JButton("Agent");
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    focus.clearAmounts();
                    focus.agentAmount = 1.0;
                    focusToPanel();
                    onFocusChanged();
                }
            });
            add(button, gc);
            gc.gridx++;

            gc.gridwidth = 2;
            agentAmountSlider = new JSlider(0, 100);
            agentAmountSlider.addChangeListener(this);
            add(agentAmountSlider, gc);
            gc.gridx++;
            gc.gridwidth = 1;
        }
        gc.gridy++;
        gc.gridx = 0;

        {
            JButton button = new JButton("Message");
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    focus.clearAmounts();
                    focus.messageAmount = 1.0;
                    focusToPanel();
                    onFocusChanged();
                }
            });
            add(button, gc);
            gc.gridx++;

            gc.gridwidth = 2;
            messageAmountSlider = new JSlider(0, 100);
            messageAmountSlider.addChangeListener(this);
            add(messageAmountSlider, gc);
            gc.gridx++;
            gc.gridwidth = 1;
        }
        gc.gridy++;
        gc.gridx = 0;

        {
            JButton button = new JButton("Concept");
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    focus.clearAmounts();
                    focus.conceptAmount = 1.0;
                    focusToPanel();
                    onFocusChanged();
                }
            });
            add(button, gc);
            gc.gridx++;

            gc.gridwidth = 2;
            conceptAmountSlider = new JSlider(0, 100);
            conceptAmountSlider.addChangeListener(this);
            add(conceptAmountSlider, gc);
            gc.gridx++;
            gc.gridwidth = 1;
        }
        gc.gridy++;
        gc.gridx = 0;

        {
            JButton button = new JButton("Text");
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    focus.clearAmounts();
                    focus.textAmount = 1.0;
                    focusToPanel();
                    onFocusChanged();
                }
            });
            add(button, gc);
            gc.gridx++;

            gc.gridwidth = 2;
            textAmountSlider = new JSlider(0, 100);
            textAmountSlider.addChangeListener(this);
            add(textAmountSlider, gc);
            gc.gridx++;
            gc.gridwidth = 1;
        }
        gc.gridy++;
        gc.gridx = 0;

        {
            JButton button = new JButton("Meta");
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    focus.clearAmounts();
                    focus.metaAmount = 1.0;
                    focusToPanel();
                    onFocusChanged();
                }
            });
            add(button, gc);
            gc.gridx++;

            gc.gridwidth = 2;
            metaAmountSlider = new JSlider(0, 100);
            metaAmountSlider.addChangeListener(this);
            add(metaAmountSlider, gc);
            gc.gridx++;
            gc.gridwidth = 1;
        }
        gc.gridy++;
        gc.gridx = 0;

        {
            JButton button = new JButton("Other");
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    focus.clearAmounts();
                    focus.otherAmount = 1.0;
                    focusToPanel();
                    onFocusChanged();
                }
            });
            add(button, gc);
            gc.gridx++;

            gc.gridwidth = 2;
            otherAmountSlider = new JSlider(0, 100);
            otherAmountSlider.addChangeListener(this);
            add(otherAmountSlider, gc);
            gc.gridx++;
            gc.gridwidth = 1;
        }
        gc.gridy++;
        gc.gridx = 0;

        focusToPanel();

        //JComboBox selectBox = new JComboBox();

//            selectBox.addItem("All");
//            selectBox.addItem("What");
//            selectBox.addItem("Who");
//            selectBox.addItem("Where");
//            selectBox.addItem("When");


    }

    public void panelToFocus() {
        focus.hereThere = ((double) whereSlider.getValue()) / 100.0;
        focus.selfWorld = ((double) whoSlider.getValue()) / 100.0;
        focus.detailAmount = ((double) detailAmountSlider.getValue()) / 100.0;
        focus.agentAmount = ((double) agentAmountSlider.getValue()) / 100.0;
        focus.metaAmount = ((double) metaAmountSlider.getValue()) / 100.0;
        focus.messageAmount = ((double) messageAmountSlider.getValue()) / 100.0;
        focus.conceptAmount = ((double) conceptAmountSlider.getValue()) / 100.0;
        focus.textAmount = ((double) textAmountSlider.getValue()) / 100.0;
        focus.otherAmount = ((double) otherAmountSlider.getValue()) / 100.0;
        focus.keywords = keywordField.getText().toLowerCase();
        System.out.println("keyword focus = " + focus.keywords);
        onFocusChanged();
    }

    public void focusToPanel() {
        suppressStateChange = true;
        whereSlider.setValue((int) (focus.hereThere * 100.0));
        whoSlider.setValue((int) (focus.selfWorld * 100.0));
        detailAmountSlider.setValue((int) (focus.detailAmount * 100.0));
        agentAmountSlider.setValue((int) (focus.agentAmount * 100.0));
        metaAmountSlider.setValue((int) (focus.metaAmount * 100.0));
        conceptAmountSlider.setValue((int) (focus.conceptAmount * 100.0));
        messageAmountSlider.setValue((int) (focus.messageAmount * 100.0));
        textAmountSlider.setValue((int) (focus.textAmount * 100.0));
        otherAmountSlider.setValue((int) (focus.otherAmount * 100.0));
        suppressStateChange = false;
        updateUI();
    }

    public void stateChanged(ChangeEvent e) {
        if (!suppressStateChange) {
            panelToFocus();
        }
    }

    abstract public void onFocusChanged();
}
