package automenta.netention.swingui.pattern;

import automenta.netention.Self;
import automenta.netention.Pattern;
import automenta.netention.Schema;
import javax.swing.tree.DefaultMutableTreeNode;

public class PatternsNode extends DefaultMutableTreeNode {

    private final Self network;

    public PatternsNode(Self n) {
        super("Patterns");
        this.network = n;
        Schema s = n.getSchema();
        for (Pattern p : s.getRootPatterns()) {
            add(newPatternNode(p));
        }
    }

    public DefaultMutableTreeNode newPatternNode(Pattern p) {
        DefaultMutableTreeNode x = new DefaultMutableTreeNode(p);
        Schema s = network.getSchema();
        for (Pattern j : s.getChildren(p)) {
            x.add(newPatternNode(j));
        }
        return x;
    }
}
