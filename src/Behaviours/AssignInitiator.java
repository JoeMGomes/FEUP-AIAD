package Behaviours;

import Agents.Student;
import Messages.UtilityMessage;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

public class AssignInitiator extends AchieveREInitiator {

    private int nResponders;

    public AssignInitiator(Agent a, ACLMessage msg, int n_classes) {
        super(a, msg);
        nResponders = n_classes;
    }

    protected void handleAgree(ACLMessage agree){
        System.out.println("Agent " + myAgent.getLocalName() +  " : "  +agree.getSender().getLocalName() + " agreed to add");
    }

    protected void handleInform(ACLMessage inform) {
        System.out.println("Agent " + myAgent.getLocalName() +  " : " +inform.getSender().getLocalName()+" added this student");

        //Cancel utility subscription
        ((Student)myAgent).cancelSubscription();
    }
    protected void handleRefuse(ACLMessage refuse) {
        System.out.println("Agent " + myAgent.getLocalName() +  " : "  + refuse.getSender().getLocalName()+" refused to perform the requested action");
        nResponders--;
    }
    protected void handleFailure(ACLMessage failure) {
        if (failure.getSender().equals(myAgent.getAMS())) {
            // FAILURE notification from the JADE runtime: the receiver
            // does not exist
            System.out.println("Responder does not exist");
        }
        else {
            System.out.println("Agent "+failure.getSender().getLocalName()+" failed to perform the requested action");
        }
    }
    protected void handleAllResultNotifications(Vector notifications) {
        if (notifications.size() < nResponders) {
            // Some responder didn't reply within the specified timeout
            System.out.println("Timeout expired: missing "+(nResponders - notifications.size())+" responses");
        } else {

        }
    }
}
