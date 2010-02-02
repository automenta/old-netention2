package automenta.netention.test;

import java.util.Collection;

import junit.framework.TestCase;
import automenta.netention.example.ExampleSelf;
import automenta.netention.serverdepr.Agent;
import automenta.netention.serverdepr.Message;

public class TestLinker extends TestCase {

	public void testLinker() throws Exception {
		ExampleSelf net = new ExampleSelf();
		
//		EsperLinker weaver = new EsperLinker(net) {
//			@Override protected void emitLink(DetailLink weaveLink) {
//				System.out.println("  MATCH: " + weaveLink);	
//			}
//			@Override protected void onNewEvent(NodeEvent event) {
//				System.out.println("  EVENT: " + event);					
//			}
//			@Override protected void onNewRule(EPStatement rule) {
//				System.out.println("  PATTERN: " + rule.getText());									
//			}
//			
//		};
//		
//		for (Agent a : net.getAgents().values()) {
//			for (String nodeID : a.getNodes()) {
//				Node n = net.getNode(nodeID);
//
//				try {
//					weaver.updateNode((Detail)n);
//				} catch (Exception e) {
//					System.out.println(e);
//				}
//				
//			}
//		}
//		

		Thread.sleep(250);

		int messageCount = 0;
		for (Agent a : net.getAgents().values()) {
			for (Collection<Message> lm : a.getMessages().values()) {
				messageCount+=lm.size();
			}
		}
		assertTrue(messageCount > 0);
		
		
	}
	
	
}
