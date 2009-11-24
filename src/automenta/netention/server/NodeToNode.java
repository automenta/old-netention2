package automenta.netention.server;

/** associates two nodes (and their creator agents) in a forward direction */
public interface NodeToNode extends Link {

	public String getFromNode();
	public String getToNode();

}
