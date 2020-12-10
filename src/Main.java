import Agents.CUClass;
import Utils.CUClassInfo;
import Utils.DataRecorder;
import Agents.Student;
import Utils.Parity;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.ControllerException;
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
    private static final String PARAMS_FILE_PATH = "./src/Parameters.pf"; // null to set parameters manually
//    private static final String PARAMS_FILE_PATH = null;

    /**
     * Repast run parameters
     */
    int numberOfOddStudents = 2;
    int numberOfEvenStudents = 3;
    String classesStats = null;

    private List<Student> students;
    private List<CUClass> classes;
    private DataRecorder dataRecorder;
    ContainerController mainContainer;

    private Schedule schedule;
    private OpenSequenceGraph graph = null;

    public static void main(String[] args) {
        boolean runMode = BATCH_MODE;

        // create a simulation
        SimInit init = new SimInit();
        //init.setNumRuns(2);   // works only in batch mode

        // load model into simulation
        init.loadModel(new Main(), PARAMS_FILE_PATH, runMode);

    }

    public boolean allAllocated() {
        for (Student s : students) {
            if (!s.isAllocated())
                return false;
        }
        return true;
    }

    @Override
    public void launchJADE() {
        System.out.println(numberOfEvenStudents);
        System.out.println(numberOfOddStudents);
        System.out.println(classesStats);

        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();

        mainContainer = rt.createMainContainer(p);
        launchAgents();
    }

    private void addEvenStudent(ContainerController mainContainer, int i) throws StaleProxyException {
        Student studentAgent = new Student(Parity.EVEN);
        AgentController student = mainContainer.acceptNewAgent("Student" + i, studentAgent);
        student.start();
        students.add(studentAgent);
    }

    private void addOddStudent(ContainerController mainContainer, int i) throws StaleProxyException {
        Student studentAgent = new Student(Parity.ODD);
        AgentController student = mainContainer.acceptNewAgent("Student" + i, studentAgent);
        student.start();
        students.add(studentAgent);
    }

    private void addClass(int capacity, int occupied, float even, ContainerController mainContainer, int i) throws StaleProxyException {
        CUClass cuClassAgent = new CUClass(capacity, occupied, even);
        AgentController cuClass = mainContainer.acceptNewAgent("uc" + i, cuClassAgent);
        cuClass.start();
        classes.add(cuClassAgent);
    }

    private void launchAgents() {
        students = new ArrayList<>();
        classes = new ArrayList<>();

        try {

            List<CUClassInfo> classParams = parseClassString(classesStats);
            for(int i = 0; i < classParams.size(); i++){
                 CUClassInfo c = classParams.get(i);
                addClass(c.capacity,c.occupiedSeats,c.evenStudents, mainContainer, i);
            }

            dataRecorder = new DataRecorder(classes);

            for (int i = 0; i < numberOfOddStudents; i++) {
                addOddStudent(mainContainer, i + 1);
            }

            for (int i = 0; i < numberOfEvenStudents; i++) {
                addEvenStudent(mainContainer, numberOfOddStudents + i + 1);
            }

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void begin() {
        super.begin();
        schedule = new Schedule();

        scheduleEndCheck();

        if (!BATCH_MODE) {
            buildAndScheduleDisplay();
        }
    }

    public void scheduleEndCheck() {
        getSchedule().scheduleActionAtInterval(100, this, "checkEnd");
    }

    public void checkEnd() {

        if (allAllocated()) {
            dataRecorder.addFinalInfoAndProcess(classes);
            try {
                mainContainer.getPlatformController().kill();
            } catch (ControllerException e) {
                e.printStackTrace();
            }
        }
    }

    public void buildAndScheduleDisplay() {
        buildGraph();
        getSchedule().scheduleActionAtInterval(1, graph, "step", Schedule.LAST);
    }

    public void buildGraph() {
        if (graph != null)
            graph.dispose();
        graph = new OpenSequenceGraph("Number of Students and Classes", this);
        graph.setAxisTitles("time", "quantity");
        graph.addSequence("Students", () -> {
            return this.students.size();
        }, Color.blue);
        graph.addSequence("Classes", () -> {
            return this.classes.size();
        }, Color.red);

        graph.display();
    }

    /**
     *
     * @param info String passed to Repast with all classes params formatted as following
     * Format: "Capacity1.OccupiedSeats1.EvenStudents1:Capacity2.OccupiedSeats2.EvenStudents2
     * Note: Any invalid class will be ignored
     * @return List of CUClassInfo obtained from 'info'
     */
    public List<CUClassInfo> parseClassString(String info) {
        List<CUClassInfo> retList = new ArrayList<>();

        String[] classes = info.split(":");

        for (String s : classes) {
            CUClassInfo c = parseClassParams(s);
            if (c != null)
                retList.add(c);
        }

        return retList;
    }

    /**
     *
     * @param s String formatted as following
     * Format: "Capacity1.OccupiedSeats1.EvenStudents1"
     * @return null if invalid else returns CUClassInfo of 's'
     */
    public CUClassInfo parseClassParams(String s) {

        String[] params = s.split("\\.");
        if (params.length != 3) {
            return null;
        } else {
            int capacity = Integer.parseInt(params[0]);
            int occupied = Integer.parseInt(params[1]);
            int even = Integer.parseInt(params[2]);
            return new CUClassInfo(capacity, occupied, even);
        }
    }


    @Override
    public String getName() {
        return "Schedule Assignment Simulation";
    }

    @Override
    public String[] getInitParam() {
        return new String[]{"numberOfOddStudents", "numberOfEvenStudents", "classesStats"};
    }

    public int getNumberOfOddStudents() {
        return numberOfOddStudents;
    }

    public void setNumberOfOddStudents(int numberOfOddStudents) {
        this.numberOfOddStudents = numberOfOddStudents;
    }

    public int getNumberOfEvenStudents() {
        return numberOfEvenStudents;
    }

    public void setNumberOfEvenStudents(int numberOfEvenStudents) {
        this.numberOfEvenStudents = numberOfEvenStudents;
    }

    public String getClassesStats() {
        return classesStats;
    }

    public void setClassesStats(String classesStats) {
        this.classesStats = classesStats;
    }

}
