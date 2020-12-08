package Behaviours;

import Agents.DataRecorderAgent;
import Utils.CUClassInfo;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.SimpleBehaviour;

import static jade.lang.acl.MessageTemplate.MatchPerformative;

public class ResultsListener extends CyclicBehaviour {

    private MessageTemplate template =MessageTemplate.MatchPerformative(ACLMessage.INFORM);


    @Override
    public void action() {
        ACLMessage inform = myAgent.receive(template);
        if(inform != null) {
            CUClassInfo info = null;
            try {
                info = (CUClassInfo) inform.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            if(info != null)
                ((DataRecorderAgent)myAgent).addFinalInfo(inform.getSender(),info);
            else
                System.err.println("SOMETHING WENT WRONG IN RESULT LISTENER");
        }
        else{
            block();
        }

    }
}
