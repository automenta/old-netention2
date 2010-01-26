package automenta.netention.swingui;

import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;

public class JHyperLink extends JButton {

    public JHyperLink(String toString, String tooltip) {
        super(toString);
        setOpaque(false);
        setBorderPainted(false);
        setFont(getFont().deriveFont(Font.BOLD));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText(tooltip);
    }
}
