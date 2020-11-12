package Behaviours;

import Agents.Student;
import Messages.UtilityResponse;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

public class FIPA_UtilityRequest extends AchieveREInitiator {

    private int nResponders;

    public FIPA_UtilityRequest(Agent a, ACLMessage msg, int n_classes) {
        super(a, msg);
        nResponders = n_classes;
    }

    protected void handleAgree(ACLMessage agree){
        System.out.println("Agent " +agree.getSender().getName() + " agreed.");
    }

    protected void handleInform(ACLMessage inform) {
        System.out.println("Agent "+inform.getSender().getName()+" successfully performed the requested action");
        try {
            if( inform.getContentObject().getClass() == UtilityResponse.class){
                ((Student)myAgent).storeUtility(inform.getSender(), ((UtilityResponse)inform.getContentObject()).utility);
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
    protected void handleRefuse(ACLMessage refuse) {
        System.out.println("Agent "+refuse.getSender().getName()+" refused to perform the requested action");
        nResponders--;
    }
    protected void handleFailure(ACLMessage failure) {
        if (failure.getSender().equals(myAgent.getAMS())) {
            // FAILURE notification from the JADE runtime: the receiver
            // does not exist
            System.out.println("Responder does not exist");
        }
        else {
            System.out.println("Agent "+failure.getSender().getName()+" failed to perform the requested action");
        }
    }
    protected void handleAllResultNotifications(Vector notifications) {
        if (notifications.size() < nResponders) {
            // Some responder didn't reply within the specified timeout
            System.out.println("Timeout expired: missing "+(nResponders - notifications.size())+" responses");
        }
    }
}
