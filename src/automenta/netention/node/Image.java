/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.node;

/**
 *
 * @author seh
 */
public class Image {
    public final String url;
    private final String caption;

    public Image(String url, String caption) {
        this.url = url;
        this.caption = caption;
    }

    @Override
    public String toString() {
        if (caption!=null)
            return caption;
        return url;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Image) {
            Image i = (Image)obj;
            //TODO compare caption?
            return i.url.equals(url);
        }
        return false;
    }






}
