/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.run;

import automenta.netention.Self;
import automenta.netention.example.ExampleSelf;
import automenta.netention.io.Twitter;
import automenta.netention.nlp.en.POSTagger;
import automenta.netention.node.Agent;
import automenta.netention.node.Detail;
import automenta.netention.swingui.object.GraphPanel;
import automenta.netention.swingui.util.SwingWindow;
import automenta.netention.value.integer.IntegerIs;
import automenta.netention.value.real.RealIs;
import automenta.netention.value.string.StringIs;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 *
 * @author seh
 */
public class RunGraphPanel {

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {      }
    }

    public static void main(String[] args) {
        Self self = new ExampleSelf();
        Agent s = self.getAgent();

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

        try {
            POSTagger pos = new POSTagger();
            self.addVertex(new Twitter(pos));
            self.addVertex(pos);

            self.addVertex(RunEMailPanel.vrAssistanceSMTP);
        } catch (Exception ex) {
            Logger.getLogger(RunEQPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        new SwingWindow(new GraphPanel(self, self.getMemory(), null), 900, 800, true);
    }
}
