import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.GUI, "true");

        ContainerController cc = rt.createMainContainer(p);
        AgentController ac;

        try {
            Object[] uc1Args = new Object[1];
            uc1Args[0] = "30 10 2";
            ac = cc.createNewAgent("uc1", "Agents.CUClass", uc1Args);
            ac.start();

            Object[] uc2Args = new Object[1];
            uc2Args[0] = "32 12 9";
            ac = cc.createNewAgent("uc2", "Agents.CUClass", uc2Args);
            ac.start();

            Object[] uc3Args = new Object[1];
            uc3Args[0] = "30 10 5";
            ac = cc.createNewAgent("uc3", "Agents.CUClass", uc3Args);
            ac.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
