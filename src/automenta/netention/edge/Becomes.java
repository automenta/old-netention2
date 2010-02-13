/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.edge;

/** indicates that something has transformed (become) something else */
public final class Becomes {
    private final String name;

    public Becomes(String t) {
        this.name = t;
    }

    @Override public String toString() {
        return name;
    }


}
