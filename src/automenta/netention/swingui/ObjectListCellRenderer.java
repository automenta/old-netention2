/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui;

import automenta.netention.node.Detail;
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
public class ObjectListCellRenderer implements ListCellRenderer {

    private final EQListPanel selfPanel;

    public ObjectListCellRenderer(EQListPanel selfPanel) {
        this.selfPanel = selfPanel;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel j = new JPanel(new BorderLayout());


        JLabel nameLabel, subLabel = null;
        String typeString = "";
        
        if (value instanceof Detail) {
            Detail d = (Detail) value;
            nameLabel = new JLabel(d.getName());

            typeString = d.getPatterns().toString();
            int maxTypeStringLength = 16;
            if (typeString.length() > maxTypeStringLength) {
                typeString = typeString.substring(0, maxTypeStringLength);
            }
        } else {
            nameLabel = new JLabel(value.toString());
        }
        
        if (typeString.length() == 0) {
            typeString = value.getClass().getSimpleName();
        }

        subLabel = new JLabel(typeString);

        j.add(nameLabel, BorderLayout.CENTER);
        if (subLabel != null) {
            j.add(subLabel, BorderLayout.SOUTH);
        }

        Color bgColor;

        double a = selfPanel.score.get(value);

        if (isSelected) {
            bgColor = Color.DARK_GRAY;
            nameLabel.setForeground(Color.WHITE);
            if (subLabel != null) {
                subLabel.setForeground(Color.WHITE);
            }
        } else {
            float af = (float)a;
            float bf = 0.55f + (af * 0.4f) + ((index % 2 == 1) ? 0.05f : 0f);

            float h = getHue(value);
            bgColor = Color.getHSBColor(h, 0.4f + bf*0.2f, 0.8f+bf*0.2f);

            //bgColor = new Color(bf, bf, 0.0f);

        }

        float nameSize = (float) (((double)EQListPanel.FontH1.getSize()) * a);
        nameSize = Math.max(nameSize, EQListPanel.FontH3.getSize());

        nameLabel.setFont(EQListPanel.FontH2.deriveFont(nameSize));
        subLabel.setFont(EQListPanel.FontH2.deriveFont(nameSize/2.0f));

        int borderSize = 4;
        if (cellHasFocus) {
            j.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderSize));
        } else {
            j.setBorder(BorderFactory.createLineBorder(bgColor, borderSize));
        }

        j.setBackground(bgColor);

        return j;
    }

    public static float getHue(Object value) {
        return ((float)value.getClass().hashCode()) / ((float)Integer.MAX_VALUE);
    }
}
