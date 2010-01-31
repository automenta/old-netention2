/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swingui;

import automenta.netention.swingui.util.SwingWindow;
import automenta.netention.api.Agent;
import automenta.netention.api.Detail;
import automenta.netention.api.Network;
import automenta.netention.api.value.integer.IntegerIs;
import automenta.netention.api.value.real.RealIs;
import automenta.netention.api.value.string.StringIs;
import automenta.netention.example.ExampleNetwork;
import automenta.netention.swingui.agent.AgentPanel;
import javax.swing.UIManager;

/**
 *
 * @author seh
 */
public class DemoAgentPanel {
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
    }

    public static void main(String[] args) {
        Network network = new ExampleNetwork();
        Agent s = network.addAgent(new Agent("xyz", "Sam Smith"));

        Detail d = network.newDetail(s, "Mountain Bike", "Bicycle");
        d.add("gearCount", new IntegerIs(12));
        d.add("wheelDiameter", new RealIs(22));
        d.add("bicycleType", new StringIs("mountain"));

        Detail g = network.newDetail(s, "Guitar", "Guitar");
        

        network.newDetail(s, "Apartment", "Dwelling");
        network.newDetail(s, "Next Apartment", "Dwelling");

        Detail t = network.newDetail(s, "Twitter", "TwitterAccount");
        t.add("login", new StringIs("twitterid"));
        t.add("password", new StringIs("********"));


        AgentPanel dp = new AgentPanel(network, s);
        dp.setDetail(d);;

        new SwingWindow(dp, 1000, 700, true);
    }


}
