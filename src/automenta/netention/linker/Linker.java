package automenta.netention.linker;

import automenta.netention.node.Detail;

/** a weaver is a process that semantically links stories in real-time */
public interface Linker {

	public void updateNode(Detail n) throws Exception;
	public void removeNode(Detail n);
	
	public DetailLink getLink(Detail a, Detail b);

}
