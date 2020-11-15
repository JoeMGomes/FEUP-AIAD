package MainCases;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Case2 {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.GUI, "true");

        ContainerController cc = rt.createMainContainer(p);
        AgentController ac;

        try {

            Object[] evenStudent = new Object[1];
            evenStudent[0] = "even";

            Object[] oddStudent = new Object[1];
            oddStudent[0] = "odd";

            Object[] uc1Args = new Object[1];
            uc1Args[0] = "30 10 1";
            ac = cc.createNewAgent("uc1", "Agents.CUClass", uc1Args);
            ac.start();

            Object[] uc2Args = new Object[1];
            uc2Args[0] = "30 10 9";
            ac = cc.createNewAgent("uc2", "Agents.CUClass", uc2Args);
            ac.start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            ac = cc.createNewAgent("1", "Agents.Student", oddStudent);
            ac.start();

            ac = cc.createNewAgent("2", "Agents.Student", evenStudent);
            ac.start();

            ac = cc.createNewAgent("3", "Agents.Student", evenStudent);
            ac.start();

            ac = cc.createNewAgent("4", "Agents.Student", evenStudent);
            ac.start();

            ac = cc.createNewAgent("5", "Agents.Student", evenStudent);
            ac.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
