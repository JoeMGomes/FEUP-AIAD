package Behaviours;

import Agents.Student;
import Messages.UtilityRequest;
import Messages.UtilityResponse;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

public class StudentHandler extends CyclicBehaviour {

    public StudentHandler(Agent a){
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null){
            try {
                if( msg.getContentObject().getClass() == UtilityResponse.class){
                    ((Student)myAgent).storeUtility(msg.getSender(), ((UtilityResponse)msg.getContentObject()).utility);
                }
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

        } else {
            block();
        }
    }
}
