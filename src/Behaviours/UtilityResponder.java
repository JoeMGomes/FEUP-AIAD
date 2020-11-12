package Behaviours;

import Agents.CUClass;
import Messages.UtilityRequest;
import Messages.UtilityResponse;
import Utils.Parity;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

import java.io.IOException;

public class UtilityResponder extends AchieveREResponder {
    public UtilityResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        System.out.println("Agent "+ myAgent.getLocalName() +": REQUEST received from "+request.getSender().getName()+". Action is "+request.getContent());
        try {
            if (request.getContentObject().getClass() == UtilityRequest.class) {
                // We agree to perform the action. Note that in the FIPA-Request
                // protocol the AGREE message is optional. Return null if you
                // don't want to send it.
                System.out.println("Agent "+ myAgent.getLocalName() +": Agree");
                ACLMessage agree = request.createReply();
                agree.setPerformative(ACLMessage.AGREE);
                return agree;
            }
            else {
                // We refuse to perform the action
                System.out.println("Agent "+ myAgent.getLocalName() +": Refuse");
                throw new RefuseException("check-failed");
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
            System.out.println("Agent "+ myAgent.getLocalName() +": Refuse");
            throw new RefuseException("check-failed");
        }
    }

    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {

        ACLMessage inform = request.createReply();

        try {
            Parity studentParity = ((UtilityRequest)request.getContentObject()).parity;

            System.out.println("Agent "+ myAgent.getLocalName() +": Action successfully performed");
            inform.setPerformative( ACLMessage.INFORM );

            inform.setContentObject(new UtilityResponse(((CUClass)myAgent).getUtilityTotal(studentParity)));
        } catch (IOException | UnreadableException e) {

        e.printStackTrace();
            System.out.println("Agent "+ myAgent.getLocalName() +": Failure");
            throw new FailureException("failure");
        }

        return inform;

    }
}
