package Agents;

import Behaviours.ClassHandler;
import Behaviours.CyclicSpitMessage;
import Utils.Parity;
import jade.core.Agent;

import java.lang.reflect.Array;
import java.util.ArrayList;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

//CUClass - Curricular Unit Class
public class CUClass extends Agent {

    //TODO com varias UC's:Cada turma só pode ter uma CUClass para cada scheduleID
    /**
     * Global scheduleID that identifies the class schedule
     */
    private int scheduleID;

    /**
     * Number of even numbered students
     */
    private float evenStudents;
    /**
     * Current number of students in the CUClass
     */
    private int occupiedSeats;
    /**
     * Maximum capacity of the CUClass allocated room
     */
    private int capacity;


    protected void setup() {
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Curricular Units");
        sd.setName(getLocalName());
        register(sd);

        setFields();

        //addBehaviour(new CyclicSpitMessage(this));
        addBehaviour(new ClassHandler(this));
    }

    public void setFields() {
        Object[] argsOBJ = getArguments();

        String[] args = ((String)argsOBJ[0]).split(" ");

            if (args.length != 3) {
                System.err.println("Invalid Args. Aborting Class...");
                takeDown();
                return;
            }else{
                capacity = Integer.parseInt(args[0]);
                occupiedSeats = Integer.parseInt(args[1]);
                evenStudents =  Integer.parseInt(args[2]);

                if(capacity < occupiedSeats || occupiedSeats < evenStudents ){
                    System.err.println("Inconsistent data. Aborting Class...");
                    takeDown();
                    return;
                }
            }

    }

    /**
     * Registers service in DFAgent
     *
     * @param sd Servide to register
     */
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


    /**
     * Removes agent from DFAgent registration
     */
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (Exception e) {
        }
    }


    public float getUtilityCapacity() {
        return capacity - occupiedSeats;
    }

    /**
     * Total utility calculated from class statistics.
     * Formula if Student is even: utilityCapacity * parityRatio.
     * Formula if Student is odd:  utilityCapacity / parityRatio.
     */
    public Float getUtilityTotal(Parity p) {
        if (p.equals(Parity.EVEN)) {
            return getUtilityCapacity() / (evenStudents / occupiedSeats);
        } else if (p.equals(Parity.ODD)) {
            return getUtilityCapacity() * (evenStudents / occupiedSeats);
        } else {
            System.err.println("Invalid Parity. Assuming Even");
            return getUtilityCapacity() * (evenStudents / occupiedSeats);
        }
    }

    public void addStudent(Parity p) {
        occupiedSeats++;
        if (p.equals(Parity.EVEN)) {
            evenStudents++;
        }
    }

}
