package Behaviours;

import Agents.CUClass;
import Messages.UtilityRequest;
import Messages.UtilityResponse;
import Utils.Parity;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.HashMap;

public class ClassHandler extends CyclicBehaviour {

    public ClassHandler(Agent a){
        super(a);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null){
            try {
                if( msg.getContentObject().getClass() == UtilityRequest.class){

                    Parity studentParity = ((UtilityRequest)msg.getContentObject()).parity;

                    ACLMessage reply = msg.createReply();
                    reply.setPerformative( ACLMessage.INFORM );
                    reply.setContentObject(new UtilityResponse(((CUClass)myAgent).getUtilityTotal(studentParity)));
                    myAgent.send(reply);
                }
            } catch (UnreadableException | IOException e) {
                e.printStackTrace();
            }

        } else {
            block();
        }
    }
}
