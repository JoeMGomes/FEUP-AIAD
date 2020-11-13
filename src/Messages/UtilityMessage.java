package Messages;

import java.io.Serializable;

public class UtilityMessage implements Serializable {

    public float utility;

    public UtilityMessage(float u){
        utility = u;
    }
}
