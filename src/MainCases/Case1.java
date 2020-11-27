package MainCases;

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
        //p.setParameter(Profile.GUI, "true");

        ContainerController cc = rt.createMainContainer(p);
        AgentController ac;

        try {

            Object[] evenStudent = new Object[1];
            evenStudent[0] = "even";

            Object[] oddStudent = new Object[1];
            oddStudent[0] = "odd";

            Object[] uc1Args = new Object[1];
            uc1Args[0] = "30 10 5";
            ac = cc.createNewAgent("uc1", "Agents.CUClass", uc1Args);
            ac.start();

            Object[] uc2Args = new Object[1];
            uc2Args[0] = "30 10 5";
            ac = cc.createNewAgent("uc2", "Agents.CUClass", uc2Args);
            ac.start();


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            ac = cc.createNewAgent("Student1", "Agents.Student", evenStudent);
            ac.start();

            ac = cc.createNewAgent("Student2", "Agents.Student", evenStudent);
            ac.start();

            ac = cc.createNewAgent("Student3", "Agents.Student", evenStudent);
            ac.start();

            ac = cc.createNewAgent("Student4", "Agents.Student", evenStudent);
            ac.start();

            ac = cc.createNewAgent("Student5", "Agents.Student", evenStudent);
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
