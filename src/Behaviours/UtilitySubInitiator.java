package Behaviours;

import Agents.Student;
import Messages.UtilityMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SubscriptionInitiator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class UtilitySubInitiator extends SubscriptionInitiator {

    private int nResponders;

    public UtilitySubInitiator(Agent a, int n_classes) {
        super(a, new ACLMessage(ACLMessage.SUBSCRIBE));
        nResponders = n_classes;
    }

    @Override
    protected Vector<ACLMessage> prepareSubscriptions(ACLMessage subscription) {

        subscription.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);

        for (HashMap.Entry<AID, Float> entry : ((Student) myAgent).getClassesUtility().entrySet()) {
            AID key = entry.getKey();

            subscription.addReceiver(key);
        }
        try {
            subscription.setContentObject(((Student) myAgent).getParity());   // the subscription content
        } catch (IOException e) {
            e.printStackTrace();
        }
        Vector<ACLMessage> v = new Vector<ACLMessage>();
        v.addElement(subscription);
        return v;
    }

    protected void handleAgree(ACLMessage agree){
        System.out.println( "Agent " + myAgent.getLocalName() +  " : "  + agree.getSender().getLocalName() + " agreed utility");
        try {
            if( agree.getContentObject().getClass() == UtilityMessage.class){
                ((Student)myAgent).storeUtility(agree.getSender(), ((UtilityMessage)agree.getContentObject()).utility);
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    protected void handleInform(ACLMessage inform) {
        System.out.println( "Agent " + myAgent.getLocalName() +  " : "  + inform.getSender().getLocalName()+" sent utility");
        try {
            if( inform.getContentObject().getClass() == UtilityMessage.class){
                ((Student)myAgent).storeUtility(inform.getSender(), ((UtilityMessage)inform.getContentObject()).utility);
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    protected void handleAllResponses(Vector responses) {
        if (responses.size() < nResponders) {
            // Some responder didn't reply within the specified timeout
            System.out.println("Timeout expired: missing "+(nResponders - responses.size())+" responses");
        } else {
            System.out.println("Agent " + myAgent.getLocalName() +  " : "  + " confirmed all subscriptions");

            try {
                Thread.sleep(new Random().nextInt(2500- 500) + 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ((Student)myAgent).chooseClass();
        }
    }

}
