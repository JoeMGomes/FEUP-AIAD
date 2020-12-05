import Agents.CUClass;
import Agents.Student;
import Utils.Parity;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;

import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.AgentController;
import sajas.wrapper.ContainerController;

import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Repast3Launcher {
    private static final boolean BATCH_MODE = true;

    int numberOfStudents = 5;
    int numberOfClasses = 2;
    int numberOfEvenStudents = 0;
    int numberOfOccupiedSeats = 0;
    int numberOfTotalSeats = 0;

    private List<Student> students;
    private List<CUClass> classes;

    private OpenSequenceGraph graph = null;

    public static void main(String[] args) {
        boolean runMode = BATCH_MODE;

        // create a simulation
        SimInit init = new SimInit();
        init.setNumRuns(2);   // works only in batch mode

        // load model into simulation
        init.loadModel(new Main(), null, runMode);

    }

    public boolean allAllocated(){
        for(Student s : students){
            if(!s.isAllocated())
                return  false;
        }
        return true;
    }

    @Override
    public void launchJADE() {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();

        ContainerController mainContainer = rt.createMainContainer(p);
        launchAgents(mainContainer);
    }

    private void launchAgents(ContainerController mainContainer){
        students = new ArrayList<Student>();
        classes = new ArrayList<CUClass>();

        try {
            for (int i = 0; i < numberOfClasses; i++) {
                CUClass cuClassAgent = new CUClass(30, 10, 5);
                AgentController cuClass = mainContainer.acceptNewAgent("uc" + i, cuClassAgent);
                cuClass.start();
                classes.add(cuClassAgent);
            }

            for (int i = 0; i < numberOfStudents; i++) {
                Student studentAgent = new Student(Parity.EVEN);
                AgentController student = mainContainer.acceptNewAgent("Student" + i, studentAgent);
                student.start();
                students.add(studentAgent);
            }

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void begin() {
        super.begin();
        //buildAndScheduleDisplay();
    }

    public void buildAndScheduleDisplay() {
        buildGraph();
        getSchedule().scheduleActionAtInterval(1, graph, "step", Schedule.LAST);
    }

    public void buildGraph(){
        if (graph != null)
            graph.dispose();
        graph = new OpenSequenceGraph("Number of Students and Classes", this);
        graph.setAxisTitles("time", "quantity");
        graph.addSequence("Students", () -> { return this.students.size();}, Color.blue);
        graph.addSequence("Classes", () -> { return this.classes.size();}, Color.red);

        graph.display();
    }

    @Override
    public String getName() {
        return "Schedule Assignment Simulation";
    }

    @Override
    public String[] getInitParam() {
        return new String[] { "numberOfStudents", "numberOfClasses", "numberOfEvenStudents", "numberOfOccupiedSeats",
                "numberOfTotalSeats" };
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public int getNumberOfClasses() {
        return numberOfClasses;
    }

    public void setNumberOfClasses(int numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }

    public int getNumberOfEvenStudents() {
        return numberOfEvenStudents;
    }

    public void setNumberOfEvenStudents(int numberOfEvenStudents) {
        this.numberOfEvenStudents = numberOfEvenStudents;
    }

    public int getNumberOfOccupiedSeats() {
        return numberOfOccupiedSeats;
    }

    public void setNumberOfOccupiedSeats(int numberOfOccupiedSeats) {
        this.numberOfOccupiedSeats = numberOfOccupiedSeats;
    }

    public int getNumberOfTotalSeats() {
        return numberOfTotalSeats;
    }

    public void setNumberOfTotalSeats(int numberOfTotalSeats) {
        this.numberOfTotalSeats = numberOfTotalSeats;
    }
}
