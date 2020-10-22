package Agents;

import Behaviours.CyclicSpitMessage;
import jade.core.Agent;

//CUClass - Curricular Unit Class
public class CUClass extends Agent {

    /** TODO com varias UC's:
     * Cada turma só pode ter uma CUClass para cada scheduleID
     */
    //Numero global que identifica o dia e hora da aula
    private int scheduleID;

    protected  void setup(){
        //Retirar esta mensagem daqui, mover para um Behaviour se for necessária
        System.out.println("Hello I am CurricularUnit");


        addBehaviour(new CyclicSpitMessage(this));
    }
}
