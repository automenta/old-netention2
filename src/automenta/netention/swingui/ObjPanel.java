package automenta.netention.swingui;

import automenta.netention.swingui.pattern.PatternPanel;
import automenta.netention.swingui.agent.AgentPanel;
import automenta.netention.api.Agent;
import automenta.netention.api.Detail;
import automenta.netention.api.Network;
import automenta.netention.api.Pattern;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class ObjPanel extends JPanel {

    private final Network network;

    public ObjPanel(Network network) {
        super(new BorderLayout());
        this.network = network;
    }

//    public void setDetail(Detail d) {
//        removeAll();
//        updateUI();
//    }

    public void setAgent(Agent a) {
        removeAll();
        add(new AgentPanel(network, a), BorderLayout.CENTER);
        updateUI();
    }

    public void setPattern(Pattern p) {
        removeAll();
        add(new PatternPanel(network, p), BorderLayout.CENTER);
        updateUI();
    }
}

