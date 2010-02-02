/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.node;

import automenta.netention.Node;
import automenta.netention.linker.NodeToNode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author seh
 */
public class Agent extends Node {
	public static final String CREATED = "created";

	private List<String> nodes = new LinkedList();


	/** subject (node ID, etc...) -> list of messages about it */
	private Map<String, Set<NodeToNode>> messages = new HashMap<String, Set<NodeToNode>>();


	public Agent(String id, String name) {
		super(id, name);
	}


	public List<String> getNodes() {
		return nodes;
	}

	public void addNode(String nodeID) {
		synchronized (getNodes()) {
			getNodes().add(nodeID);
		}
	}

	public void removeNode(String nodeID) {
		synchronized (getNodes()) {
			getNodes().remove(nodeID);
		}
	}

	@Override public String toString() {
		return getName();
	}

	public void addMessage(String subjectID, NodeToNode m) {
		synchronized (getMessages()) {
			Set<NodeToNode> subject = getMessages().get(subjectID);
			if (subject == null) {
				subject = new HashSet();
				getMessages().put(subjectID, subject);
			}
			synchronized (subject) {
				subject.add(m);
			}
		}
	}

	//abstract public Node newDetail(String name, String... patterns);


	public Map<String, Set<NodeToNode>> getMessages() {
		return messages;
	}


    
}
