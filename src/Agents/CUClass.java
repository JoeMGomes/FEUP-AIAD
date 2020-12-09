package Agents;

import Behaviours.AssignResponder;
import Behaviours.UtilitySubResponder;
import Utils.CUClassInfo;
import Utils.Parity;
import sajas.core.Agent;

import sajas.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

//CUClass - Curricular Unit Class
public class CUClass extends Agent {

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

    /**
     * Reference to subscription behaviour to allow calls to the notify() function
     */
    private UtilitySubResponder subscriptionBehaviour;

    public CUClass(int capacity, int occupiedSeats, int evenStudents) {
        this.capacity = capacity;
        this.occupiedSeats = occupiedSeats;
        this.evenStudents = evenStudents;
    }

    @Override
    protected void setup() {
        // Register service in DF Agent
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Curricular Units");
        sd.setName(getLocalName());
        register(sd);

        // Start utility subscription behaviour
        subscriptionBehaviour = new UtilitySubResponder(this);
        addBehaviour(subscriptionBehaviour);

        // Start student assignment behaviour
        addBehaviour(new AssignResponder(this));
    }

    /**
     * Calculates utility related to capacity.
     * Formula is: capacity - OccupiedSeats
     * @return utility related to capacity only
     */
    public float getUtilityCapacity() {
        return capacity - occupiedSeats;
    }

    /**
     * Total utility calculated from class statistics.
     * Formula if Student is even: utilityCapacity / parityRatio.
     * Formula if Student is odd:  utilityCapacity * parityRatio.
     */
    public Float getUtilityTotal(Parity p) {
        if (p.equals(Parity.EVEN)) {
            return getUtilityCapacity() / (evenStudents / occupiedSeats);
        } else if (p.equals(Parity.ODD)) {
            return getUtilityCapacity() * (evenStudents / occupiedSeats);
        } else {
            System.err.println("Invalid Parity. Assuming Even");
            return getUtilityCapacity() / (evenStudents / occupiedSeats);
        }
    }

    /**
     * Adds student with Parity p to the class and notifies
     * all subscriptions of the change in utility
     * @param p Utility of the student to add
     */
    public void addStudent(Parity p) {
        occupiedSeats++;
        if (p.equals(Parity.EVEN)) {
            evenStudents++;
        }
        subscriptionBehaviour.notify(null);
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

    public CUClassInfo getInfo(){
        return new CUClassInfo(evenStudents,occupiedSeats,capacity);
    }
}
