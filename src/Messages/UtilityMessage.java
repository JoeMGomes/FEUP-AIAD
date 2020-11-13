package Messages;

import java.io.Serializable;

public class UtilityResponse implements Serializable {

    public float utility;

    public UtilityResponse(float u){
        utility = u;
    }
}
