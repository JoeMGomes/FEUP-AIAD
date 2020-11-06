package Messages;

import Utils.Parity;

import java.io.Serializable;

public class UtilityRequest implements Serializable {

    public Parity parity;

    public UtilityRequest(Parity p){
        parity = p;
    }

}
