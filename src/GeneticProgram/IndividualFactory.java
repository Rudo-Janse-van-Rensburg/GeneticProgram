package GeneticProgram;

public class IndividualFactory {
    
    public IndividualFactory(){}  
    
    public static Individual PopOneOut(){
        return new Individual(GeneticOperators.Grow(0,GeneticOperators.initialMaxDepth));
    }

    public static Individual Clone(Individual i){
        return new Individual(i);
    }
    
    public static double Fitness(Individual individual ){
        double fitness          = 0;
        double accuracy         = 0;
        double precision        = 0;
        double recall           = 0;
        double f1               = 0;
        int numberOfClasses     = Data.GetNumberClasses();
        double truePositives[]  = new double[numberOfClasses];
        double falsePositives[] = new double[numberOfClasses];
        double falseNegatives[] = new double[numberOfClasses];
        double targetClass;
        double predictedClass;
        for (int i = 0; i < Data.GetDataSize(); i++) {
            targetClass         = Data.GetData()[i][Data.GetPosition(Data.GetClass())];
            predictedClass      = Resolve(Data.GetData()[i],individual.GetRoot());
            if(predictedClass == targetClass){
                truePositives[(int) Math.floor(targetClass)]        += 1;
            }else{
                falsePositives[(int) Math.floor(predictedClass)]    += 1;
                falseNegatives[(int) Math.floor(targetClass)]       += 1;
            }
        }
        double totalTruePositives   = 0;
        for (int i = 0; i < numberOfClasses; i++) {
            totalTruePositives += truePositives[i];
            if(truePositives[i] > 0 || falsePositives[i] > 0){
                precision   += truePositives[i]/(truePositives[i]+falsePositives[i]);
            }
            if(truePositives[i] > 0 || falseNegatives[i] > 0){
                 recall      += truePositives[i]/(truePositives[i]+falseNegatives[i]);
            }
        }
        //accuracy    = totalTruePositives/Data.GetDataSize();
        precision   /= numberOfClasses;
        recall      /= numberOfClasses;
        f1          = 2 * (precision * recall)/(precision + recall);
        
        
        return f1;
        /*
        return (precision + recall + f1 + accuracy)/ 4;
        
        
        double correct = 0;
        for (int i = 0; i < Data.GetDataSize(); i++) {
            if(Resolve(Data.GetData()[i],individual.GetRoot())  ==  Data.GetData()[i][Data.GetPosition(Data.GetClass())]){
                ++correct;
            }
        }
        return correct/Data.GetDataSize();
        */
    } 
    
    public static double F1(Individual individual){
        double precision    = Precision(individual);
        double recall       = Recall(individual);
        return 2 * (precision * recall)/(precision + recall);
    }
    
    public static double Precision(Individual individual){
        double precision        = 0;
        int numberOfClasses     =   Data.GetNumberClasses();
        double truePositives;
        double falsePositives;
        for (int i = 0; i < numberOfClasses; i++) {
            truePositives           = 0;
            falsePositives          = 0;
            double expectedClass    = i;
            for (int j = 0; j < Data.GetDataSize(); j++) {
                double determinedClass  = Resolve(Data.GetData()[j],individual.GetRoot());
                if(determinedClass == expectedClass){
                    if(Data.GetData()[j][Data.GetPosition(Data.GetClass())] == expectedClass){
                        truePositives += 1;
                    }else{
                        falsePositives += 1;
                    }
                } 
            }
            if(truePositives > 0 || falsePositives > 0){
                precision   += truePositives/(truePositives+falsePositives);
            }
        }
        precision /= numberOfClasses;
        return precision;
    }
    
    public static double Recall(Individual individual){
        double recall           = 0;
        int numberOfClasses     = Data.GetNumberClasses();
        double truePositives;
        double falseNegatives;
        for (int i = 0; i < numberOfClasses; i++) {
            truePositives           = 0;
            falseNegatives          = 0;
            double  expectedClass   = i;
            for (int j = 0; j < Data.GetDataSize(); j++) {
                double determinedClass  = Resolve(Data.GetData()[j],individual.GetRoot());
                if(determinedClass == expectedClass){
                    if(Data.GetData()[j][Data.GetPosition(Data.GetClass())] == expectedClass){
                        truePositives += 1;
                    }
                }else{
                    if(Data.GetData()[j][Data.GetPosition(Data.GetClass())] == expectedClass){
                        falseNegatives += 1;
                    }
                }
            }
            if(truePositives > 0 || falseNegatives > 0){
                recall  += truePositives/(truePositives+falseNegatives);
            }
            
        }
        recall /= numberOfClasses;
        return recall;
    }
    
    public static double Accuracy(Individual individual){
        int numberCorrectPredictions    = 0;
        int numberPredictions           = Data.GetDataSize();
        for (int i = 0; i < numberPredictions; i++) {
            if(Resolve(Data.GetData()[i],individual.GetRoot())  ==  Data.GetData()[i][Data.GetPosition(Data.GetClass())]){
                ++numberCorrectPredictions;
            }
        }
        return (1.0*numberCorrectPredictions)/(1.0*numberPredictions);
    }
    
    private static double Resolve(double[] data, If i){
        if(i.GetType().equals("Class"))
            return i.GetClass();
        else if(Met(data,i.GetCondition()))
            return Resolve(data,i.GetTrue());
        else 
            return Resolve(data,i.GetFalse());
    }
    
    private static boolean Met(double[] data, Condition c){
        boolean metCondition = false;
        if(c.GetType().equals("Logical")){
            metCondition = Resolve_Logical(data, c);
        }else if(c.GetType().equals("Relational")){
            metCondition = Resolve_Relational(data, c);
        }
        return metCondition; 
    }
    
    private static boolean Resolve_Logical(double[] data, Condition c){
        boolean result  = false;
        switch(c.GetOperator()){
            case "OR":
                result = (Met(data,c.GetLeft()) || Met(data,c.GetRight()));
                break;
            case "AND":
                result = (Met(data,c.GetLeft()) && Met(data,c.GetRight()));
                break;
            case "NOT":
                result = (!Met(data,c.GetRight()));
                break;
        }
        return result;
    }
    
    private static boolean Resolve_Relational(double[] data, Condition c){
        boolean result  = false;
        switch(c.GetOperator()){
            case ">":
                result = (Resolve_Arithmetic(data,c.GetLeft()) > Resolve_Arithmetic(data,c.GetRight()));
                break;
            case "<":
                result = (Resolve_Arithmetic(data,c.GetLeft()) < Resolve_Arithmetic(data,c.GetRight()));
                break;
            case ">=":
                result = (Resolve_Arithmetic(data,c.GetLeft()) >= Resolve_Arithmetic(data,c.GetRight()));
                break;
            case "<=":
                result = (Resolve_Arithmetic(data,c.GetLeft()) <= Resolve_Arithmetic(data,c.GetRight()));
                break;
            case "==":
                result = (Resolve_Arithmetic(data,c.GetLeft()) == Resolve_Arithmetic(data,c.GetRight()));
                break;
        }
        return result;
    }
    
    private static double Resolve_Arithmetic(double[] data, Condition c){
        double result = 0;
        if(c.GetType().equals("Attribute")){
            result  = data[Data.GetPosition(c.GetOperator())];
        }else{
            switch(c.GetOperator()){
                case "+":
                    result  = Resolve_Arithmetic(data, c.GetLeft()) + Resolve_Arithmetic(data, c.GetRight());
                    break;
                case "-":
                    result  = Resolve_Arithmetic(data, c.GetLeft()) - Resolve_Arithmetic(data, c.GetRight());
                    break;
                case "*":
                    result  = Resolve_Arithmetic(data, c.GetLeft()) * Resolve_Arithmetic(data, c.GetRight());
                    break;
                case "/":
                    result  = Resolve_Arithmetic(data, c.GetLeft()) / Resolve_Arithmetic(data, c.GetRight());
                    break;
            }
        }
        return result;
    } 

}
