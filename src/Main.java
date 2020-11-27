import jade.core.Profile;
import jade.core.ProfileImpl;
import sajas.core.Runtime;


public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.GUI, "true");

        rt.createMainContainer(p);

    }
}
