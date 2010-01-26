/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swingui;

import automenta.netention.api.Network;
import automenta.netention.example.ExampleNetwork;
import javax.swing.UIManager;

/**
 *
 * @author seh
 */
public class DemoNetworkPanel {
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
    }

    public static void main(String[] args) {
        Network network = new ExampleNetwork();
        new SwingWindow(new NetworkPanel(network), 1000, 700, true);
    }
}
