package Utils;

import java.io.Serializable;

//Mimics CUClass information for storage purposes
public class CUClassInfo implements Serializable {
    /**
     * Number of even numbered students
     */
    public float evenStudents;
    /**
     * Current number of students in the CUClass
     */
    public int occupiedSeats;
    /**
     * Maximum capacity of the CUClass allocated room
     */
    public int capacity;

    public CUClassInfo(float e,int o, int c){
        evenStudents = e;
        occupiedSeats = o;
        capacity = c;
    }

    @Override
    public String toString() {
        return "CUClassInfo{" +
                "e=" + evenStudents +
                ", o=" + occupiedSeats +
                ", c=" + capacity +
                '}';
    }
}
