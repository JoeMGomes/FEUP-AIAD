package Behaviours;

import Agents.CUClass;
import Messages.AssignMessage;
import Utils.Parity;
import sajas.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import sajas.proto.AchieveREResponder;

public class AssignResponder extends AchieveREResponder {
    public AssignResponder(Agent a) {
        super(a, MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)));
    }

    /**
     * If received Utility does not match actual utility or the received
     * object is not of type AssignMessage a refuse exception is thrown.
     * Else sends agree message.
     */
    protected ACLMessage handleRequest(ACLMessage request) throws  RefuseException {
        try {
            System.out.println("Agent " + myAgent.getLocalName() + ": ASSIGN received from " + request.getSender().getLocalName());

            Object message = request.getContentObject();
            if ( message.getClass() == AssignMessage.class) {

                // Agree
                if(((AssignMessage)message).utility.equals(((CUClass)myAgent).getUtilityTotal( ((AssignMessage)message).parity))) {
                    ACLMessage agree = request.createReply();
                    agree.setPerformative(ACLMessage.AGREE);
                    return agree;
                } else {
                    throw new RefuseException("invalid-utility");
                }
            } else {
                // We refuse to perform the action
                throw new RefuseException("check-failed");
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
            System.out.println("Agent " + myAgent.getLocalName() + ": Refused assign");
            throw new RefuseException("check-failed");
        }
    }


    /**
     * Adds the student to the class and sends an inform message
     */
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        ACLMessage inform = request.createReply();

        try {
            Parity studentParity = ((AssignMessage) request.getContentObject()).parity;

            //Add Student to class
            ((CUClass) myAgent).addStudent(studentParity);

            System.out.println("Agent " + myAgent.getLocalName() + ": Action successfully performed");
            inform.setPerformative(ACLMessage.INFORM);

        } catch (UnreadableException e) {

            e.printStackTrace();
            System.out.println("Agent " + myAgent.getLocalName() + ": Failure");
            throw new FailureException("failure");
        }

        return inform;
    }
}
