package MainCases;

import Agents.CUClass;
import Agents.Student;
import Utils.Parity;
import jade.core.Profile;
import jade.core.ProfileImpl;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.AgentController;
import sajas.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import uchicago.src.sim.engine.SimInit;

public class Case1 extends Repast3Launcher {
    public static void main(String[] args) {
        SimInit init = new SimInit();
        init.setNumRuns(1);   // works only in batch mode
        init.loadModel(new Case1(), null, true);
    }

    @Override
    protected void launchJADE() {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();

        ContainerController cc = rt.createMainContainer(p);
        AgentController ac;

        try {
            // Object[] evenStudent = new Object[1];
            // evenStudent[0] = "even";

            //Object[] oddStudent = new Object[1];
            //oddStudent[0] = "odd";

            CUClass cuClass1 = new CUClass(30, 10, 5);
            ac = cc.acceptNewAgent("uc1", cuClass1);
            ac.start();

            CUClass cuClass2 = new CUClass(30, 10, 5);
            ac = cc.acceptNewAgent("uc2", cuClass2);
            ac.start();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Student std1 = new Student(Parity.EVEN);
            ac = cc.acceptNewAgent("Student1", std1);
            ac.start();

            Student std2 = new Student(Parity.EVEN);
            ac = cc.acceptNewAgent("Student2", std2);
            ac.start();

            Student std3 = new Student(Parity.EVEN);
            ac = cc.acceptNewAgent("Student3", std3);
            ac.start();

            Student std4 = new Student(Parity.EVEN);
            ac = cc.acceptNewAgent("Student4", std4);
            ac.start();

            Student std5 = new Student(Parity.EVEN);
            ac = cc.acceptNewAgent("Student5", std5);
            ac.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getInitParam() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "Case 1";
    }
}
