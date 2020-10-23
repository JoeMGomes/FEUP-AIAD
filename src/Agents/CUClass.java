package Agents;

import Behaviours.CyclicSpitMessage;
import jade.core.Agent;

import java.lang.reflect.Array;
import java.util.ArrayList;

//CUClass - Curricular Unit Class
public class CUClass extends Agent {

    //TODO com varias UC's:Cada turma só pode ter uma CUClass para cada scheduleID
    /**
     * Global scheduleID that identifies the class schedule
      */
    private int scheduleID;
    /**
     * Total utility calculated from class statistics
     * Formula if Student is even: utilityCapacity * parityRatio
     * Formula if Student is odd:  utilityCapacity / parityRatio
     */
    private float utilityTotal;
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


    protected  void setup(){
        //Retirar esta mensagem daqui, mover para um Behaviour se for necessária
        System.out.println("Hello I am CurricularUnit");


        addBehaviour(new CyclicSpitMessage(this));
    }
}
