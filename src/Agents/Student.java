package Agents;

import jade.content.AgentAction;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;


public class Student extends Agent {
    /**
     * Parity of the Student's number
     */

    /**
     * Hashmap of the known classes Utility
     * Key= CUClass AID
     * Value= Utility
     */
    private HashMap<AID, Float> classesUtility;
    private Ontology ontology = Ontologies.ScheduleOntology.getInstance();


    private void start(){
        ACLMessage requestMsg = new ACLMessage(ACLMessage.REQUEST);
        requestMsg.setContent("Quero utility");
        for (HashMap.Entry<AID, Float> entry : classesUtility.entrySet()) {
            AID key = entry.getKey();
            //Object value = entry.getValue();

            requestMsg.addReceiver(key);
        }
        send(requestMsg);
    }

    private void getClasses() {
        try{
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("Curricular Units");
            dfd.addServices(sd);

            DFAgentDescription[] classes = DFService.search(this, dfd);

            System.out.println(classes.length + " results");
            for(int i = 0; i < classes.length; i++){
                System.out.println(classes[i].getName());
               classesUtility.put(classes[i].getName(), (float)-1);
            }

        } catch (FIPAException fe) { fe.printStackTrace(); }
    }

    protected  void setup(){
        classesUtility = new HashMap<AID, Float>();
        getClasses();
        start();
    }

    void sendMessage(AID receiver ,int performative, AgentAction action) {
// --------------------------------------------------------

        ACLMessage msg = new ACLMessage(performative);
        msg.setOntology(ontology.getName());
        try {
            getContentManager().fillContent(msg, new Action(receiver, action));
            msg.addReceiver(receiver);
            send(msg);
            System.out.println("Contacting server... Please wait!");
            //addBehaviour(new WaitServerResponse(this));
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }



}
