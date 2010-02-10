/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.io;

import automenta.netention.node.Message;

/**
 *
 * @author seh
 */
public interface Sends {

    public boolean canSend(Message m);
    public void send(Message m, Async a);

}
