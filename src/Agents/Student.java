package Agents;

import Behaviours.CyclicSpitMessage;
import Utils.Parity;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Student extends Agent {
    /**
     * Parity of the Student's number
     */
    private Parity parity;

    /**
     * Hashmap of the known classes Utility
     * Key= CUClass AID
     * Value= Utility
     */
    private HashMap<AID, Float> classesUtility;

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





}
