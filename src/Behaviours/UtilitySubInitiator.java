package Behaviours;

import Agents.Student;
import Messages.UtilityResponse;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SubscriptionInitiator;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class UtilitySubInitiator extends SubscriptionInitiator {

    private int nResponders;

    public UtilitySubInitiator(Agent a, int n_classes) {
        super(a, new ACLMessage(ACLMessage.SUBSCRIBE));
        System.out.println("Subscribing");
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
        System.out.println("Agent " + agree.getSender().getName() + " agreed.");
        try {
            if( agree.getContentObject().getClass() == UtilityResponse.class){
                ((Student)myAgent).storeUtility(agree.getSender(), ((UtilityResponse)agree.getContentObject()).utility);
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
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

    protected void handleAllResponses(Vector responses) {
        if (responses.size() < nResponders) {
            // Some responder didn't reply within the specified timeout
            System.out.println("Timeout expired: missing "+(nResponders - responses.size())+" responses");
        } else {
            System.out.println("ALL SUBSCIRPTIS DONE");

            ((Student)myAgent).chooseClass();
        }
    }

}
