package Agents;

import Behaviours.CyclicSpitMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class Student extends Agent {


    protected  void setup(){
        //Retirar esta mensagem daqui, mover para um Behaviour se for necessária
        System.out.println("Hello Im Student");


        addBehaviour(new CyclicSpitMessage(this));

        //Test Behaviour sends hello 3 times
        addBehaviour(new SimpleBehaviour() {
            int n = 0;

            @Override
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent( "Hi I'm " + myAgent.getName() );
                msg.addReceiver( new AID( "UC", AID.ISLOCALNAME) );
                send(msg);
                n++;
            }
            @Override
            public boolean done() {
                return n == 3;
            }
        });
    }


}
