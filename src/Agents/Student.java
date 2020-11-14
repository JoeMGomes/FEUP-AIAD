package Agents;

import Behaviours.AssignInitiator;
import Behaviours.UtilitySubInitiator;
import Messages.AssignMessage;
import Messages.ParityMessage;
import Utils.Parity;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.*;


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

            for(int i = 0; i < classes.length; i++){
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
        utilitySubInitiator = new UtilitySubInitiator(this,  classesUtility.size());
        addBehaviour(utilitySubInitiator);
    }

    public void storeUtility(AID a, Float util){
        if(a != null && util != null)
            classesUtility.put(a,util);
    }

    public void chooseClass(){

        try {
            Thread.sleep(new Random().nextInt(1500- 500) + 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AID bestClass = getBestClass();

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(bestClass);
        try {
            request.setContentObject(new AssignMessage(parity, classesUtility.get(bestClass) ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        addBehaviour(new AssignInitiator(this, request,1));
    }

    private AID getBestClass(){
        AID aidClass = Collections.max(classesUtility.entrySet(), Comparator.comparing(Map.Entry::getValue)).getKey();
        System.out.println("Agent: " + this.getLocalName() +" best Class: " + aidClass.getLocalName() + " utility -> " + classesUtility.get(aidClass));
        return aidClass;
    }

    public void cancelSubscription(){

        for (HashMap.Entry<AID, Float> entry : getClassesUtility().entrySet()) {
            utilitySubInitiator.cancel(entry.getKey(), true);
        }
    }

    public HashMap<AID, Float> getClassesUtility() {
        return classesUtility;
    }

    public Parity getParity() {
        return parity;
    }


    protected void takeDown() {
        System.out.println("Student " + getLocalName() + " (" +  parity +") sent to class " + getBestClass().getLocalName());
    }

}
