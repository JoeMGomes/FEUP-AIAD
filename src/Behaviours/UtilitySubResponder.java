package Behaviours;

import Agents.CUClass;
import Messages.UtilityResponse;
import Utils.Parity;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.SubscriptionResponder;

import java.io.IOException;
import java.util.Vector;

public class UtilitySubResponder extends SubscriptionResponder {

    public UtilitySubResponder(Agent a) {
        super(a, MessageTemplate.and(

                MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE),

                        MessageTemplate.MatchPerformative(ACLMessage.CANCEL)),

                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE)));

    }

    protected ACLMessage handleSubscription(ACLMessage subscription_msg) {

        //Always Agrees
        createSubscription(subscription_msg);

        System.out.println("Agent "+ myAgent.getLocalName() +": Agree");
        ACLMessage agree = subscription_msg.createReply();
        agree.setPerformative(ACLMessage.AGREE);
        setUtilityContent(subscription_msg, agree);
        return agree;
    }

    public void notify(ACLMessage inform) {

        // go through every subscription
        Vector subs = getSubscriptions();
        for (int i = 0; i < subs.size(); i++) {

            inform = ((Subscription) subs.elementAt(i)).getMessage().createReply();
            inform.setPerformative((ACLMessage.INFORM));
            ACLMessage request = ((Subscription) subs.elementAt(i)).getMessage();
            setUtilityContent(request, inform);

            ((SubscriptionResponder.Subscription) subs.elementAt(i)).notify(inform);
        }
    }

    public void setUtilityContent(ACLMessage request, ACLMessage response){
        Parity p = null;

        try {
            p = (Parity) request.getContentObject();
            response.setContentObject(new UtilityResponse(((CUClass) myAgent).getUtilityTotal(p)));

        } catch (IOException | UnreadableException e) {
            e.printStackTrace();
        }
    }
}

