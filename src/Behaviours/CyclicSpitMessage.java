package Behaviours;

import Agents.CUClass;
import Utils.Parity;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class CyclicSpitMessage extends CyclicBehaviour {

    //Just prints every message it receives in a semi-formated way
    public CyclicSpitMessage(Agent a){
        super(a);
    }

    public void action(){
        ACLMessage msg = myAgent.receive();
        if(msg != null){
            if (msg.getPerformative() == ACLMessage.PROPOSE) {
                try {
                    System.out.println(myAgent.getLocalName() + " received: \n\t" + msg.getContentObject());
                    Parity p = (Parity) msg.getContentObject();
                    ((CUClass) myAgent).addStudent(p);
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }

            }
        } else {
            block();
        }
    }
}
