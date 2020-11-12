package Agents;

import Behaviours.StudentHandler;
import Behaviours.FIPA_UtilityRequest;
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
        ACLMessage requestMsg = new ACLMessage(ACLMessage.REQUEST);

        requestMsg.setContentObject(new UtilityRequest(parity));
        for (HashMap.Entry<AID, Float> entry : classesUtility.entrySet()) {
            AID key = entry.getKey();

            requestMsg.addReceiver(key);
        }
        requestMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        requestMsg.setReplyByDate(new Date(System.currentTimeMillis() + 5000));

        addBehaviour(new FIPA_UtilityRequest(this, requestMsg, classesUtility.size()));
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

    private void getParity(){

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

        getParity();
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //addBehaviour(new StudentHandler(this));
    }

}
