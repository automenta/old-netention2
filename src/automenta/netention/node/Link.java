/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.node;

/**
 *
 * @author seh
 */
public class Link {
    public final String label;
    public final String url;

    public Link(String label, String url) {
        this.label = label;
        this.url = url;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public int hashCode() {
        return label.hashCode() + url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Link) {
            Link l = (Link) obj;
            if (l.label.equals(label))
                if (l.url.equals(url))
                    return true;
            return false;
        }
        return false;
    }



    
}
