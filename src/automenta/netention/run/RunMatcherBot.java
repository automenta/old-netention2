/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.run;

import automenta.netention.Self;
import automenta.netention.example.ExampleSelf;
import automenta.netention.io.RSSFeed;
import automenta.netention.io.Twitter;
import automenta.netention.nlp.en.POSTagger;
import automenta.netention.swingui.botpanel.MatchBotPanel;
import automenta.netention.swingui.util.SwingWindow;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author seh
 */
public class RunMatcherBot {

    public static void main(String[] args) {
        Self self = new Self("xyz", "XYZ");

        try {
            RSSFeed ewPages = new RSSFeed(new URI("http://evariware.com/?feedpages"), self.getMemory());
            
            //Agents

            POSTagger pos = new POSTagger();
            self.addVertex(new Twitter(pos));
            self.addVertex(pos);

            self.addVertex(RunEMailPanel.vrAssistanceSMTP);
        } catch (Exception ex) {
            Logger.getLogger(RunEQPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        new SwingWindow(new MatchBotPanel(self), 900, 800, true);

    }
}
