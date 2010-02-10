/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.io;

/**
 *
 * @author seh
 */
public interface Async  {

    abstract public void onFinished(Object result);
    abstract public void onError(Exception e);

}
