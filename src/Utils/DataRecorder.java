package Utils;

import Agents.CUClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import jade.core.AID;

public class DataRecorder {

    private HashMap<AID, CUClassInfo> initialValues = new HashMap<>();
    private HashMap<AID, CUClassInfo> finalValues = new HashMap<>();

    public DataRecorder(List<CUClass> classes) {

        for( CUClass c : classes ){
            initialValues.put(c.getAID(),c.getInfo());
        }
    }

    public void addFinalInfoAndProcess(List<CUClass> classes){
        for( CUClass c : classes ){
            finalValues.put(c.getAID(),c.getInfo());
        }
        System.out.println("Initial ---------------------");

        for (AID agentID : initialValues.keySet()) {
            System.out.println(( agentID.getLocalName() + " " + initialValues.get(agentID).toString()));
        }

        System.out.println("Final ---------------------");

        for (AID agentID : finalValues.keySet()) {
            System.out.println(( agentID.getLocalName() + " " + finalValues.get(agentID).toString()));
        }

        System.out.println("ENDING ---------------------");

        /* Process data and store it
         *
         */
        processData();

        for (AID agentID : finalValues.keySet()) {
            System.out.println(( agentID.getLocalName() + " " + finalValues.get(agentID).toString()));
        }
        System.out.println("ENDED ---------------------");

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
