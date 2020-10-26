package Agents;

import Behaviours.CyclicSpitMessage;
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
     * Total utility calculated from class statistics.
     * Formula if Student is even: utilityCapacity * parityRatio.
     * Formula if Student is odd:  utilityCapacity / parityRatio.
     */
    private Float utilityTotal;
    /**
     * Utility related to capacity
     */
    private float utilityCapacity;

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


    protected void setup()
    {
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( "Curricular Units" );
        sd.setName( getLocalName() );
        register( sd );

        addBehaviour(new CyclicSpitMessage(this));
    }

    void register( ServiceDescription sd)
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd );
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
    }

    protected void takeDown()
    {
        try { DFService.deregister(this); }
        catch (Exception e) {}
    }

}
