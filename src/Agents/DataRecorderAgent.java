package Agents;

import Behaviours.ResultsListener;
import Utils.CUClassInfo;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.wrapper.ControllerException;
import sajas.core.Agent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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

            System.out.println("ENDING ---------------------");

            /* Proccess data and store it
             *
             */
            processData();

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

    private float parityAverage(HashMap<AID, CUClassInfo> values) {
        float sum = 0;

        for (AID id : values.keySet()) {
            CUClassInfo info = values.get(id);
            sum += (float)info.evenStudents / info.occupiedSeats;
        }

        return (float)sum / values.size();
    }

    private float calculateParityDeviation(HashMap<AID, CUClassInfo> values) {
        float average = parityAverage(values);
        float sum = 0;

        for (AID id : values.keySet()) {
            CUClassInfo info = values.get(id);
            sum += Math.pow( average - ( (float) info.evenStudents / info.occupiedSeats ) , 2);
        }

        return (float)Math.sqrt(sum / values.size());
    }

    private float occupationAverage(HashMap<AID, CUClassInfo> values) {
        float sum = 0;

        for (AID id : values.keySet()) {
            CUClassInfo info = values.get(id);
            sum += (float)info.occupiedSeats / info.capacity;
        }

        return (float)sum / values.size();
    }

    private float calculateOccupationDeviation(HashMap<AID, CUClassInfo> values) {
        float average = occupationAverage(values);
        float sum = 0;

        for (AID id : values.keySet()) {
            CUClassInfo info = values.get(id);
            sum += Math.pow( average - ( (float) info.occupiedSeats / info.capacity ) , 2);
        }

        return (float)Math.sqrt(sum / values.size());
    }

    private void processData() {
        // difference of initial and final parity deviations
        // positive difference means the final deviation is lower, so the overall values are more balanced
        // higher is better
        float parityDiff = calculateParityDeviation(initialValues) - calculateParityDeviation(finalValues);

        // difference of initial and final occupation deviations
        // positive difference means the final deviation is lower, so the overall values are more balanced
        // higher is better
        float occupationDiff = calculateOccupationDeviation(initialValues) - calculateOccupationDeviation(finalValues);

        storeInFile(parityDiff, occupationDiff);
    }

    private void storeInFile(float parity, float occupation) {
        File file = new File("data.csv");
        try {
            if(file.createNewFile()) {
                file.deleteOnExit();
            }
            FileWriter writer = new FileWriter(file, true);
            writer.write(Float.toString(parity)+','+Float.toString(occupation)+"\n");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
