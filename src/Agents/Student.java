package Agents;

import Behaviours.UtilityInitiator;
import Behaviours.UtilitySubInitiator;
import Messages.UtilityRequest;
import Utils.Parity;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;


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

    private void start() throws IOException {


        addBehaviour(new UtilitySubInitiator(this,  classesUtility.size()));
    }

    public void storeUtility(AID a, Float util){
        if(a != null && util != null)
            classesUtility.put(a,util);

        System.out.println("Added: " + a + ", " + util);
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
    protected void setup(){
        classesUtility = new HashMap<AID, Float>();
        getClasses();

        getParityArgs();
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //addBehaviour(new StudentHandler(this));
    }



    public HashMap<AID, Float> getClassesUtility() {
        return classesUtility;
    }

    public Parity getParity() {
        return parity;
    }
}
