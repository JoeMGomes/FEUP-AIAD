package Behaviours;

import Agents.Student;
import sajas.core.Agent;
import sajas.core.behaviours.WakerBehaviour;

public class StudentSubWaker extends WakerBehaviour {

    public StudentSubWaker(Agent student, long period) {
        super(student, period);
    }

    public void onWake() {
        ((Student)myAgent).getClasses();
        UtilitySubInitiator utilitySubInitiator = new UtilitySubInitiator(myAgent, ((Student) myAgent).getClassesUtility().size());
        ((Student) myAgent).setUtilitySubInitiator(utilitySubInitiator);
        myAgent.addBehaviour(utilitySubInitiator);
    }
}