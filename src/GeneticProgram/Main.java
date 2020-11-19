package GeneticProgram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    /*
    To:
    ----------------------------
    -> F1 score for fitness.
    -> Impose unique restraint on initial population.
    -> Application rate instead of mutation.
    */
    
    private static void RunExperiment(
            String root,
            String classname,
            String outputFile,
            int numClasses,
            int populationSize,
            int convergenceThreshold,
            int tournamentSize,
            int maxGeneration,
            double applicationRate,
            int initialMaxDepth,
            int maxDepth
    ) {
        double results[][] = new double[4][30];
        long trainTime[] = new long[30];
        Evolution evolution;
        Individual classifier;
        long start, end;
        Data.SetNumberClasses(numClasses);
        for (int i = 0; i < 30; i++) {
            GeneticOperators.initialMaxDepth    = initialMaxDepth;
            GeneticOperators.maxDepth           = maxDepth;
            //TRAIN
            System.out.println("RUN #" + (i + 1));
            System.out.println(root + "train_" + i + ".csv");
            Data.ReadData(root + "train_" + i + ".csv", classname);
            Data.SetRandomSeed(i);
            GeneticOperators.InitializeAttributes();
            evolution                           = new Evolution(
                                                    populationSize,
                                                    convergenceThreshold,
                                                    tournamentSize,
                                                    maxGeneration,
                                                    applicationRate);
            start                               = System.currentTimeMillis();
            classifier                          = evolution.Start();
            end                                 = System.currentTimeMillis();
            trainTime[i]                        = end - start;
            
            //TEST
            Data.ReadData(root + "test_" + i + ".csv", classname);
            System.out.println("\nClasses     :   " + Data.GetNumberClasses());
            GeneticOperators.InitializeAttributes();
            System.out.println("Fitness     :   " + IndividualFactory.Fitness(classifier));
            System.out.println("F1          :   " + IndividualFactory.F1(classifier));
            System.out.println("Precision   :   " + IndividualFactory.Precision(classifier));
            System.out.println("Recall      :   " + IndividualFactory.Recall(classifier));
            System.out.println("Accuracy    :   " + IndividualFactory.Accuracy(classifier));
            results[0][i] = IndividualFactory.F1(classifier);
            results[1][i] = IndividualFactory.Precision(classifier);
            results[2][i] = IndividualFactory.Recall(classifier);
            results[3][i] = IndividualFactory.Accuracy(classifier);
        }
        try {
            System.out.println("SAVED " + outputFile);
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write("F1,Precision,Recall,Accuracy,Time\n");
            for (int i = 0; i < 30; i++) {
                fileWriter.write(
                        results[0][i] + ","
                        + results[1][i] + ","
                        + results[2][i] + ","
                        + results[3][i] + ","
                        + trainTime[i] + "\n"
                );
            }
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error saving file  :   " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        /*Covid Clinical Data*/
        /*
        RunExperiment(
                "/media/rudo/Storage/Research/work-related/Code/Python/covid_clinical_data/",
                "covid19_test_results",
                "results_covid_clinical_data.csv",
                2,
                4000,
                20,
                4,
                50,
                0.02,
                5, 
                15
        );
        */
        
        /*
        RunExperiment(
                        "/media/rudo/Storage/Research/work-related/Code/Python/finding_&_clinical_notes/", 
                        "finding",
                        "results_finding_&_clinical_notes.csv",
                        4,
                        4000,
                        20,
                        4,
                        100,
                        0.02,
                        5,
                        15
        ); 
        */
        
        
        RunExperiment(
                "/media/rudo/Storage/Research/work-related/Code/Python/covid_clinical_data/",
                "covid19_test_results",
                "results_covid_clinical_data.csv",
                2,
                100,
                20,
                4,
                200,
                0.5,
                5, 
                15
        );
        
        
        
        
        RunExperiment(
                        "/media/rudo/Storage/Research/work-related/Code/Python/finding_&_clinical_notes/", 
                        "finding",
                        "results_finding_&_clinical_notes.csv",
                        4,
                        100,
                        20,
                        4,
                        200,
                        0.50,
                        5,
                        15
        );  
        
    }

}
