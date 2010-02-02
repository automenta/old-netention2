/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.node;

import java.net.URL;

/**
 *
 * @author seh
 */
public class AgentRef {

    public final String id;
    public final URL imageURL;

    public AgentRef(String id) {
        this(id, null);
    }

    public AgentRef(String id, URL imageURL) {
        super();
        this.id = id;
        this.imageURL = imageURL;
    }

    @Override public boolean equals(Object obj) {
        if (obj instanceof AgentRef) {
            AgentRef other = (AgentRef)obj;
            return other.id.equals( id );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }





}
