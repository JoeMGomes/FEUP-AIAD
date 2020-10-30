package Ontologies;

import Ontologies.AgentActions.CalculateUtility;
import Ontologies.Concepts.StudentConcept;
import Ontologies.Concepts.UtilityConcept;
import jade.content.onto.*;
import jade.content.schema.*;
import Utils.ScheduleVocabulary;


public class ScheduleOntology extends Ontology implements ScheduleVocabulary {

   // The name identifying this ontology
   public static final String ONTOLOGY_NAME = "Schedule-Ontology";

   // The singleton instance of this ontology
   private static Ontology instance = new ScheduleOntology();

   // Method to access the singleton ontology object
   public static Ontology getInstance() { return instance; }


   // Private constructor
   private ScheduleOntology() {

      super(ONTOLOGY_NAME, BasicOntology.getInstance());

      try {

         // ------- Add Concepts

         // Student Info
         ConceptSchema cs = new ConceptSchema(STUDENT_INFO);
         add(cs, StudentConcept.class);
         cs.add(PARITY, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN), ObjectSchema.MANDATORY);


         // Problem
         //add(cs = new ConceptSchema(PROBLEM), Problem.class);
         //cs.add(PROBLEM_NUM, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
         //cs.add(PROBLEM_MSG, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);

         // Utility
         cs = new ConceptSchema(UTILITY);
         add(cs, UtilityConcept.class);
         cs.add(UTILITY_VALUE, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);

         // ------- Add AgentActions

         // Calculate Utility
         AgentActionSchema as = new AgentActionSchema(CALCULATE_UTILITY);
         add(as, CalculateUtility.class);
         as.add(PARITY, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN), ObjectSchema.MANDATORY);
         as.add(UTILITY_VALUE, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);

         // Add student to class
         //add(as = new AgentActionSchema(MAKE_OPERATION), MakeOperation.class);
         //as.add(MAKE_OPERATION_TYPE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
         //as.add(MAKE_OPERATION_AMOUNT, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
         //as.add(MAKE_OPERATION_ACCOUNTID, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);

         // Information
         //add(as = new AgentActionSchema(INFORMATION), Information.class);
         //as.add(INFORMATION_TYPE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
         //as.add(INFORMATION_ACCOUNTID, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
      }
      catch (OntologyException oe) {
         oe.printStackTrace();
      }
   }
}