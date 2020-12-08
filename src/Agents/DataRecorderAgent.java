package Agents;

import Behaviours.ResultsListener;
import Utils.CUClassInfo;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.wrapper.ControllerException;
import sajas.core.Agent;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

import jade.core.AID;
import sajas.domain.DFService;

public class DataRecorderAgent extends Agent {

    private HashMap<AID, CUClassInfo> initialValues = new HashMap<>();
    private HashMap<AID, CUClassInfo> finalValues = new HashMap<>();

    public DataRecorderAgent(List<CUClass> classes) {

        for( CUClass c : classes ){
            initialValues.put(c.getAID(),c.getInfo());
        }

    }

    public void addFinalInfo(AID id, CUClassInfo c){
        finalValues.put(id,c);

        if(initialValues.size() == finalValues.size()){

            /* Proccess data and store it
            *
             */

            System.out.println("ENDING ---------------------");

            for (AID agentID : finalValues.keySet()) {
                System.out.println(( agentID.getLocalName() + " " + finalValues.get(agentID).toString()));
            }
            System.out.println("ENDED ---------------------");

            //Terminate program
            try {
                getContainerController().getPlatformController().kill();
            } catch (ControllerException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void setup() {

        // Register service in DF Agent
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Data Recorder");
        sd.setName(getLocalName());
        register(sd);

        addBehaviour(new ResultsListener());
    }

    void register(ServiceDescription sd) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }


}
