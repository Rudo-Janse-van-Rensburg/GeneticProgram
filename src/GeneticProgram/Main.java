package GeneticProgram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main { 
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
        Data dataObj;
        
        /* 
        Data.SetNumberClasses(numClasses);
        */
        for (int i = 0; i < 30; i++) {
            GeneticOperators.initialMaxDepth    = initialMaxDepth;
            GeneticOperators.maxDepth           = maxDepth;
            //TRAIN
            System.out.println("RUN #" + (i + 1));
            System.out.println(root + "train_" + i + ".csv");
            dataObj = new Data(i,numClasses, root + "train_" + i + ".csv", classname); 
            evolution                           = new Evolution(
                                                    dataObj,
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
            dataObj = new Data(i,numClasses,root + "test_" + i + ".csv", classname); 
            System.out.println("\nClasses     :   " + dataObj.GetNumberClasses());
            //GeneticOperators.InitializeAttributes(dataObj);
            System.out.println("Fitness     :   " + IndividualFactory.Fitness(classifier,dataObj));
            System.out.println("F1          :   " + IndividualFactory.F1(classifier,dataObj));
            System.out.println("Precision   :   " + IndividualFactory.Precision(classifier,dataObj));
            System.out.println("Recall      :   " + IndividualFactory.Recall(classifier,dataObj));
            System.out.println("Accuracy    :   " + IndividualFactory.Accuracy(classifier,dataObj));
            results[0][i] = IndividualFactory.F1(classifier,dataObj);
            results[1][i] = IndividualFactory.Precision(classifier,dataObj);
            results[2][i] = IndividualFactory.Recall(classifier,dataObj);
            results[3][i] = IndividualFactory.Accuracy(classifier,dataObj);
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
       /*
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
        */
        
        
        
        RunExperiment(
                        "/media/rudo/Storage/Research/work-related/Code/Python/finding_&_clinical_notes/", 
                        "finding",
                        "results_finding_&_clinical_notes.csv",
                        4,
                        100,
                        20,
                        4,
                        200,
                        0.20,
                        5,
                        15
        );  
        
    }

}
