/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swingui;

import automenta.netention.api.Agent;
import automenta.netention.api.Detail;
import automenta.netention.api.Network;
import automenta.netention.example.ExampleNetwork;
import automenta.netention.swingui.detail.DetailPanel;
import javax.swing.UIManager;

/**
 *
 * @author seh
 */
public class DemoDetailPanel {
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
    }

    public static void main(String[] args) {
        Network network = new ExampleNetwork();
        Agent s = network.addAgent(new Agent("xyz", "XyZ"));
        Detail d = network.newDetail(s, "Bicycle", "Bicycle");

        DetailPanel dp = new DetailPanel(network, d);

        new SwingWindow(dp, 1000, 700, true);
    }

}
