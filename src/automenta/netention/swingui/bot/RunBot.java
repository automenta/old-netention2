/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swingui.bot;

import automenta.netention.net.PlexusGraph;
import automenta.netention.swingui.util.SwingWindow;

/**
 *
 * @author seh
 */
public class RunBot {

    public static void main(String[] args) {
        new SwingWindow(new BotPanel(new PlexusGraph()), 900, 700, true);
    }
}
