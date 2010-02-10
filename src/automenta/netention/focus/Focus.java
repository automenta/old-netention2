/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.focus;

import automenta.netention.Pattern;
import automenta.netention.node.Agent;
import automenta.netention.node.AgentRef;
import automenta.netention.node.Channel;
import automenta.netention.node.Concept;
import automenta.netention.node.Detail;
import automenta.netention.node.Message;

/**
 *
 * @author seh
 */
public class Focus {
   
    public double selfWorld = 0.5;      //WHO: self=0.0 <--> world = 1.0
    public double hereThere = 0.5;      //WHERE: here=0.0 <--> far = 1.0
    public double pastFuture = 0.5;    //WHEN: past=0.0, now=0.5, future=1.0

    public double detailAmount = 0.25;      //amount of story details
    public double conceptAmount = 0;     //amount of concepts
    public double messageAmount = 0.25;     //amount of messages
    public double agentAmount = 0.25;     //amount of agents (people)
    public double textAmount = 0;     //amount of agents (people)
    public double otherAmount = 0;     //amount of agents (people)

    public double metaAmount = 0.1;        //amount of metadata: patterns, processes, configuration, etc

    public String keywords = "";
    
    /** scoring heuristic - how much 'o' is in the focus described by this. in range 0..1.0 */
    public double score(Object o) {
        double s = 1.0;

        if (keywords.length() > 0)
            if (!matchesKeyword(o))
                return 0;
        
        if (o instanceof Detail) {
            //TODO check if it is created by 'self's ME'
            s *= (1.0 - selfWorld);
        }
        else {
            s *= selfWorld;
        }

        if (o instanceof Detail) {
            s *= detailAmount;
        }
        else if (o instanceof Message) {
            s *= messageAmount;
        }
        else if (o instanceof String) {
            s *= textAmount;
        }
        else if ((o instanceof Concept) || (o instanceof Channel)) {
            s *= conceptAmount;
        }
        else if ((o instanceof Agent) || (o instanceof AgentRef)) {
            s *= agentAmount;
        }
        else if (o instanceof Pattern) {
            s *= metaAmount;
        }
        else {
            s *= otherAmount;
        }


        return s;
    }

    public boolean matchesKeyword(Object o) {
        String s = o.toString().toLowerCase();
        if (s.contains(keywords))
            return true;
        return false;
    }

    public void clearAmounts() {
        messageAmount = conceptAmount = agentAmount = metaAmount = detailAmount = textAmount = otherAmount = 0.0;
    }
}
