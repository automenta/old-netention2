package automenta.netention.api.linker.hueristic;

import java.util.HashSet;
import java.util.Set;

import automenta.netention.api.Detail;
import automenta.netention.api.linker.DetailLink;
import automenta.netention.api.linker.Linker;

abstract public class HueristicLinker implements Linker {

	private Set<Detail> nodes = new HashSet();

	public HueristicLinker() {
		super();
	}
	
	@Override public void removeNode(Detail n) {
		synchronized (nodes) {
			nodes.remove(n);
		}
	}

	@Override public synchronized void updateNode(Detail n) throws Exception {
		synchronized (nodes) {
			if (!containsNode(n)) {
				addNode(n);
			}
			
			for (Detail d : nodes) {
				if (d == n)
					continue;
		
				DetailLink link = compare(n, d);
				if (link!=null) {
					if (link.getStrength() > getStrengthThreshold())
					emitLink(link);
				}
			}
		}
		
	}

	private double getStrengthThreshold() {		return 0.0;		}

	public abstract void emitLink(DetailLink link);

	abstract public DetailLink compare(Detail a, Detail b);

	protected void addNode(Detail n) {
		synchronized (nodes) {
			nodes.add(n);
		}
	}

	protected boolean containsNode(Detail n) {
		boolean b;
		synchronized (nodes) {	
			b = nodes.contains(n);
		}
		return b;
	}

	@Override
	public DetailLink getLink(Detail a, Detail b) {
		return compare(a, b);
	}
	

}
