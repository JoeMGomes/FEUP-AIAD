package Ontologies.Concepts;

import jade.content.Concept;

public class StudentConcept implements Concept {

    private Boolean isOdd;

    public Boolean getOdd() {
        return isOdd;
    }

    public void setOdd(Boolean odd) {
        isOdd = odd;
    }
}
