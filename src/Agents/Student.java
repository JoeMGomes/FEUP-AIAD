package Agents;

import Behaviours.UtilitySubInitiator;
import Utils.Parity;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

    private UtilitySubInitiator utilitySubInitiator;

    protected void setup(){
        classesUtility = new HashMap<AID, Float>();
        getClasses();

        getParityArgs();
        start();
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

    private void getParityArgs(){

        Object[] args = getArguments();
        String s;

        if (args != null) {
            if(args.length == 0){
                System.err.println("Invalid Parity. Setting to Even");
                parity = Parity.EVEN;
                return;
            }

            s = (String) args[0];
            System.out.println("Parity: " + s);

            if( s.equalsIgnoreCase("Odd")){
                parity = Parity.ODD;
            } else if ( s.equalsIgnoreCase("Even")) {
                parity = Parity.EVEN;
            } else {
                System.err.println("Invalid Parity. Setting to Even");
                parity = Parity.EVEN;
            }
        }
    }

    private void start(){
        System.out.println("Starting");
        utilitySubInitiator = new UtilitySubInitiator(this,  classesUtility.size());
        addBehaviour(utilitySubInitiator);
        System.out.println("Finished starting");
    }

    public void storeUtility(AID a, Float util){
        if(a != null && util != null)
            classesUtility.put(a,util);

        System.out.println("Added " + this.getLocalName() + ": " + a + ", " + util);
    }

    public void chooseClass(){
        AID bestClass = getBestClass();

        //Another protocol???????????????????
        ACLMessage proposal = new ACLMessage(ACLMessage.PROPOSE);
        proposal.addReceiver(bestClass);
        try {
            proposal.setContentObject(parity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(proposal);

//        cancelSubscription(bestClass);
    }

    private AID getBestClass(){
        AID aidClass = Collections.max(classesUtility.entrySet(), Comparator.comparing(Map.Entry::getValue)).getKey();
        System.out.println("Best Class: " + aidClass);
        return aidClass;
    }

    private void cancelSubscription(AID aidClass){
        utilitySubInitiator.cancel(aidClass, true);
    }

    public HashMap<AID, Float> getClassesUtility() {
        return classesUtility;
    }

    public Parity getParity() {
        return parity;
    }
}
