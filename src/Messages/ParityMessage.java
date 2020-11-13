package Messages;

import Utils.Parity;

import java.io.Serializable;

public class ParityMessage implements Serializable {

    public Parity parity;

    public ParityMessage(Parity p){
        parity = p;
    }

}
