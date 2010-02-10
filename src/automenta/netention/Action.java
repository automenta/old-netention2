package automenta.netention;

import java.util.concurrent.Callable;

public abstract class Action implements Callable {
    
    private final String name;

    public Action(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }   
    
}
