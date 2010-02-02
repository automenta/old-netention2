/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swingui;

import automenta.netention.example.*;
import automenta.netention.swingui.util.SwingWindow;
import automenta.netention.node.Agent;
import automenta.netention.node.Detail;
import automenta.netention.Self;
import automenta.netention.value.integer.IntegerIs;
import automenta.netention.value.real.RealIs;
import automenta.netention.value.string.StringIs;
import automenta.netention.example.ExampleSelf;
import automenta.netention.swingui.SelfPanel;
import javax.swing.UIManager;

/**
 *
 * @author seh
 */
public class RunSelfPanel {
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
    }

    public static void main(String[] args) {
        Self self = new ExampleSelf();
        Agent s = self.addNode(new Agent("xyz", "Sam Smith"));

        Detail d = self.newDetail(s, "Mountain Bike", "Bicycle");
        d.add("gearCount", new IntegerIs(12));
        d.add("wheelDiameter", new RealIs(22));
        d.add("bicycleType", new StringIs("mountain"));

        Detail g = self.newDetail(s, "Guitar", "Guitar");
        

        self.newDetail(s, "Apartment", "Dwelling");
        self.newDetail(s, "Next Apartment", "Dwelling");

        Detail t = self.newDetail(s, "Twitter", "TwitterAccount");
        t.add("login", new StringIs("twitterid"));
        t.add("password", new StringIs("********"));


        SelfPanel dp = new SelfPanel(self, s);
        dp.showObject(d);

        new SwingWindow(dp, 1100, 900, true);
    }


}
