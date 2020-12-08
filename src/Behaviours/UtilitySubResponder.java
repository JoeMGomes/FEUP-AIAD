package Behaviours;

import Agents.CUClass;
import Messages.UtilityMessage;
import Utils.Parity;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.wrapper.ControllerException;
import sajas.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import sajas.domain.DFService;
import sajas.proto.SubscriptionResponder;

import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

import sajas.core.Runtime;

import static sajas.domain.AMSService.amsAID;
import static sajas.domain.AMSService.send;

public class UtilitySubResponder extends SubscriptionResponder {

    public UtilitySubResponder(Agent a) {
        super(a, MessageTemplate.and(

                MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE),

                        MessageTemplate.MatchPerformative(ACLMessage.CANCEL)),

                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE)));

    }

    protected ACLMessage handleSubscription(ACLMessage subscription_msg) {
//        System.out.println("\n\nSUBS STUDENT=" + subscription_msg.getSender().getLocalName()+ "\n\n");
        //Always Agrees
        createSubscription(subscription_msg);

        System.out.println("Agent "+ myAgent.getLocalName() +": Agrees " + subscription_msg.getSender().getLocalName() + " subscription");
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
            response.setContentObject(new UtilityMessage(((CUClass) myAgent).getUtilityTotal(p)));

        } catch (IOException | UnreadableException e) {
            e.printStackTrace();
        }
    }

    protected ACLMessage handleCancel(ACLMessage cancel)
            throws FailureException {
        super.handleCancel(cancel);

        if(getSubscriptions().size() == 0){
            try {
                DFAgentDescription dfd = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Data Recorder");
                dfd.addServices(sd);


                DFAgentDescription[] dataRecorder = DFService.search(myAgent, dfd);

                if(dataRecorder != null){
                    ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                    inform.setContentObject(((CUClass)myAgent).getInfo());
                    inform.addReceiver(dataRecorder[0].getName());

                    myAgent.send(inform);
                }

                else{
                    System.err.println("SOMETHING WENT WRONG");
                }
            } catch (FIPAException | IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }
}

