package Agents;

import Behaviours.AssignInitiator;
import Behaviours.StudentSubWaker;
import Behaviours.UtilitySubInitiator;
import Messages.AssignMessage;
import Utils.Parity;
import jade.core.AID;
import sajas.core.Agent;
import sajas.domain.DFService;
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

    private boolean isAllocated;

    public void setUtilitySubInitiator(UtilitySubInitiator utilitySubInitiator) {
        this.utilitySubInitiator = utilitySubInitiator;
    }

    /**
     * Reference to the subscription behaviour to allow calls to the cancel() function
     */
    private UtilitySubInitiator utilitySubInitiator;

    public Student(Parity parity) {
        this.parity = parity;
        this.isAllocated = false;
    }

    protected void setup(){
        classesUtility = new HashMap<AID, Float>();

        // Add behaviour related to the subscription of each class utility
        addBehaviour(new StudentSubWaker(this,2000 ));
    }

    /**
     *  Finds all classes that published the service "Curricular Units"
     *  in the DFAgent and adds them to the classesUtility Hashmap
     */
    public void getClasses() {
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

    /**
     * Stores utility information for class "a"
     * @param a - AID of the class to be stored
     * @param util - Util of class "a" to be stored
     */
    public void storeUtility(AID a, Float util){
        if(a != null && util != null)
            classesUtility.put(a,util);
    }

    /**
     * Chooses the current class with max utility and initiates
     * an AssignInitiator Behaviour
     */
    public void chooseClass(){
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

    /**
     * @return Class AID for the best utility in classesUtility Hashmap
     */
    private AID getBestClass(){
        AID aidClass = Collections.max(classesUtility.entrySet(), Comparator.comparing(Map.Entry::getValue)).getKey();
        System.out.println("Agent: " + this.getLocalName() +" best Class: " + aidClass.getLocalName() + " utility -> " + classesUtility.get(aidClass));
        return aidClass;
    }

    /**
     * Cancels utility subscription for all classes
     */
    public void cancelSubscription(){

        for (HashMap.Entry<AID, Float> entry : getClassesUtility().entrySet()) {
            utilitySubInitiator.cancel(entry.getKey(), true);
        }
    }

    /**
     * Getter for classesUtility
     * @return classesUtility Hashmap
     */
    public HashMap<AID, Float> getClassesUtility() {
        return classesUtility;
    }

    /**
     * Getter for student parity
     * @return
     */
    public Parity getParity() {
        return parity;
    }

    /**
     * Prints to which class the student was assigned before terminating
     */
    protected void takeDown() {
        System.out.println("Student " + getLocalName() + " (" +  parity +") sent to class " + getBestClass().getLocalName());
    }

    public boolean isAllocated() {
        return isAllocated;
    }

    public void setAllocated(boolean allocated) {
        isAllocated = allocated;
    }
}
