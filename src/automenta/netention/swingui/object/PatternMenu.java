package automenta.netention.swingui.object;

import automenta.netention.node.Detail;
import automenta.netention.Pattern;
import automenta.netention.Schema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

abstract public class PatternMenu extends JMenu {

    private final List<Pattern> ep;
    private final Schema schema;

    public PatternMenu(Schema s, Detail d) {
        super("It\'s a...");
        this.schema = s;
        this.ep = s.getPatterns(d);
        for (Pattern p : s.getRootPatterns()) {
            add(newSubMenu(p));
        }
    }

    public JMenuItem newSubMenu(final Pattern p) {
        Collection<Pattern> children = schema.getChildren(p);
        if (children.size() > 0) {
            JMenu s = new JMenu(p.getName());

            JMenuItem mp = new JMenuItem(p.getName());
            mp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onPatternAdded(p);
                }
            });

            if (ep.contains(p)) {
                mp.setEnabled(false);
            }
            s.add(mp);
            s.addSeparator();
            for (Pattern c : children) {
                s.add(newSubMenu(c));
            }
            return s;
        } else {
            JMenuItem mp = new JMenuItem(p.getName());
            mp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onPatternAdded(p);
                }
            });

            if (ep.contains(p)) {
                mp.setEnabled(false);
            }
            return mp;
        }
    }

    abstract protected void onPatternAdded(Pattern p);
}
