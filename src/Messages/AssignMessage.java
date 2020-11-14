package Messages;

import Utils.Parity;

import java.io.Serializable;

public class AssignMessage implements Serializable {

    public Parity parity;
    public Float utility;
    public AssignMessage(Parity p, Float u){
        parity = p;
        utility = u;
    }

}
