/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swingui.agent;

import automenta.netention.api.Detail;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author seh
 */
class DetailsListCellRenderer implements ListCellRenderer {

    final static Color bA = new Color(1.0f, 1.0f, 1.0f);
    final static Color bB = new Color(0.95f, 0.95f, 0.95f);

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel j = new JPanel(new BorderLayout());


        JLabel nameLabel;
        if (value instanceof Detail) {
            Detail d = (Detail)value;
            nameLabel = new JLabel(d.getName());
        }
        else {
            nameLabel = new JLabel(value.toString());
        }
        j.add(nameLabel, BorderLayout.CENTER);

        Color bgColor;
        
        if (isSelected) {
            bgColor = Color.DARK_GRAY;
            nameLabel.setForeground(Color.WHITE);
        }
        else {
            if (index % 2 == 1) {
                bgColor = bB;
            }
            else {
                bgColor = bA;
            }
        }

        int borderSize = 4;
        if (cellHasFocus) {
            j.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderSize));
        }
        else {
            j.setBorder(BorderFactory.createLineBorder(bgColor, borderSize));
        }

        j.setBackground(bgColor);
        
        return j;
    }


}
