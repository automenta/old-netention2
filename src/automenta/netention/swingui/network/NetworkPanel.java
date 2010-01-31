/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swingui.network;

import automenta.netention.swingui.pattern.PatternsNode;
import automenta.netention.api.Agent;
import automenta.netention.api.Network;
import automenta.netention.api.Pattern;
import automenta.netention.api.Schema;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author seh
 */
public class NetworkPanel extends JPanel {

    private final Network network;

    public abstract class PatternListPanel extends JPanel implements TreeSelectionListener {
        final DefaultTreeModel dtm = new DefaultTreeModel(new DefaultMutableTreeNode(""));

        public PatternListPanel() {
            super(new BorderLayout());

            updateModel();

            JTree jt = new JTree(dtm);
            add(jt, BorderLayout.CENTER);

            jt.addTreeSelectionListener(this);

        }

        public void updateModel() {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Netention");
            dtm.setRoot(root);

            DefaultMutableTreeNode agentRoot = new DefaultMutableTreeNode("Agents");
            root.add(agentRoot);
            for (Agent a : network.getAgents().values()) {
                agentRoot.add(new DefaultMutableTreeNode(a));
            }

            DefaultMutableTreeNode schemaRoot = new PatternsNode(network);
            root.add(schemaRoot);


        }

        public void valueChanged(TreeSelectionEvent e) {
            Object o = ((DefaultMutableTreeNode)e.getNewLeadSelectionPath().getLastPathComponent()).getUserObject();
            if (o instanceof Pattern) {
                Pattern p = (Pattern)o;
                onPatternSelected(p);
            }
            else if (o instanceof Agent) {
                Agent a = (Agent)o;
                onAgentSelected(a);
            }
//            else if (o instanceof Detail) {
//
//            }
        }

        abstract public void onPatternSelected(Pattern p);
        abstract public void onAgentSelected(Agent a);
        //abstract public void onDetailSelected(Detail d);
    }

    public NetworkPanel(Network n) {
        super(new BorderLayout());
        this.network = n;

        final ObjPanel objPanel = new ObjPanel(n);

        JPanel listPanel = new PatternListPanel() {
            @Override public void onPatternSelected(Pattern p) {
                objPanel.setPattern(p);
            }

            @Override
            public void onAgentSelected(Agent a) {
                objPanel.setAgent(a);
            }
//
//            @Override
//            public void onDetailSelected(Detail d) {
//                objPanel.setDetail(d);
//            }
        };

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        sp.setDividerLocation(0.25);
        sp.setLeftComponent(listPanel);
        sp.setRightComponent(new JScrollPane(objPanel));
        add(sp, BorderLayout.CENTER);

    }
}
