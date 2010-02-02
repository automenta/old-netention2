/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.node;

/**
 *
 * @author seh
 */
public class Concept {
    private final String c;

    public Concept(String id) {
        this.c = id.toLowerCase();
    }

    @Override
    public int hashCode() {
        return c.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Concept) {
            return c.equals(((Concept)obj).c);
        }
        return false;
    }

    @Override
    public String toString() {
        return "\"" + c + "\"";
    }



}
