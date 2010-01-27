package automenta.netention.swingui.pattern;

import automenta.netention.api.Network;
import automenta.netention.api.Pattern;
import automenta.netention.api.Schema;
import javax.swing.tree.DefaultMutableTreeNode;

public class PatternsNode extends DefaultMutableTreeNode {

    private final Network network;

    public PatternsNode(Network n) {
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
