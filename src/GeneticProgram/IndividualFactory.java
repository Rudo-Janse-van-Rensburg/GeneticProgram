package GeneticProgram;

public class IndividualFactory {
    
    
    public IndividualFactory(){
    }  
    
    public static Individual PopOneOut(Data dataObj){
        return new Individual(GeneticOperators.Grow(0,GeneticOperators.initialMaxDepth,dataObj));
    }

    public static Individual Clone(Individual i){
        return new Individual(i);
    }
    
    public static double Fitness(Individual individual, Data dataObj){
        double fitness          = 0;
        double accuracy         = 0;
        double precision        = 0;
        double recall           = 0;
        double f1               = 0;
        int numberOfClasses     = dataObj.GetNumberClasses();
        double truePositives[]  = new double[numberOfClasses];
        double falsePositives[] = new double[numberOfClasses];
        double falseNegatives[] = new double[numberOfClasses];
        double targetClass;
        double predictedClass;
        for (int i = 0; i < dataObj.GetDataSize(); i++) {
            targetClass         = dataObj.GetData()[i][dataObj.GetPosition(dataObj.GetClass())];
            predictedClass      = Resolve(dataObj.GetData()[i],individual.GetRoot(),dataObj);
            if(predictedClass == targetClass){
                truePositives[(int) Math.floor(targetClass)]        += 1;
            }else{
                falsePositives[(int) Math.floor(predictedClass)]    += 1;
                falseNegatives[(int) Math.floor(targetClass)]       += 1;
            }
        }
        for (int i = 0; i < numberOfClasses; i++) {
            if(truePositives[i] > 0 || falsePositives[i] > 0){
                precision   += truePositives[i]/(truePositives[i]+falsePositives[i]);
            }
            if(truePositives[i] > 0 || falseNegatives[i] > 0){
                 recall      += truePositives[i]/(truePositives[i]+falseNegatives[i]);
            }
        }
        precision   /= numberOfClasses;
        recall      /= numberOfClasses;
        f1          = 2 * (precision * recall)/(precision + recall);
        return f1; 
    } 
    
    public static double F1(Individual individual,Data dataObj){
        double precision    = Precision(individual,dataObj);
        double recall       = Recall(individual,dataObj);
        return 2 * (precision * recall)/(precision + recall);
    }
    
    public static double Precision(Individual individual,Data dataObj){
        double precision        = 0;
        int numberOfClasses     = dataObj.GetNumberClasses();
        double truePositives;
        double falsePositives;
        for (int i = 0; i < numberOfClasses; i++) {
            truePositives           = 0;
            falsePositives          = 0;
            double expectedClass    = i;
            for (int j = 0; j < dataObj.GetDataSize(); j++) {
                double determinedClass  = Resolve(dataObj.GetData()[j],individual.GetRoot(),dataObj);
                if(determinedClass == expectedClass){
                    if(dataObj.GetData()[j][dataObj.GetPosition(dataObj.GetClass())] == expectedClass){
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
    
    public static double Recall(Individual individual,Data dataObj){
        double recall           = 0;
        int numberOfClasses     = dataObj.GetNumberClasses();
        double truePositives;
        double falseNegatives;
        for (int i = 0; i < numberOfClasses; i++) {
            truePositives           = 0;
            falseNegatives          = 0;
            double  expectedClass   = i;
            for (int j = 0; j < dataObj.GetDataSize(); j++) {
                double determinedClass  = Resolve(dataObj.GetData()[j],individual.GetRoot(),dataObj);
                if(determinedClass == expectedClass){
                    if(dataObj.GetData()[j][dataObj.GetPosition(dataObj.GetClass())] == expectedClass){
                        truePositives += 1;
                    }
                }else{
                    if(dataObj.GetData()[j][dataObj.GetPosition(dataObj.GetClass())] == expectedClass){
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
    
    public static double Accuracy(Individual individual,Data dataObj){
        int numberCorrectPredictions    = 0;
        int numberPredictions           = dataObj.GetDataSize();
        for (int i = 0; i < numberPredictions; i++) {
            if(Resolve(dataObj.GetData()[i],individual.GetRoot(),dataObj)  ==  dataObj.GetData()[i][dataObj.GetPosition(dataObj.GetClass())]){
                ++numberCorrectPredictions;
            }
        }
        return (1.0*numberCorrectPredictions)/(1.0*numberPredictions);
    }
    
    private static double Resolve(double[] dataArr, If i, Data dataObj){
        if(i.GetType() == 'C')
            return i.GetClass();
        else if(Met(dataArr,i.GetCondition(), dataObj))
            return Resolve(dataArr,i.GetTrue(), dataObj);
        else 
            return Resolve(dataArr,i.GetFalse(), dataObj);
    }
    
    private static boolean Met(double[] dataArr, Condition c, Data dataObj){
        boolean metCondition = false;
        if(c.GetType() == 'L'){
            metCondition = Resolve_Logical(dataArr, c, dataObj);
        }else if(c.GetType() == 'R'){
            metCondition = Resolve_Relational(dataArr, c, dataObj);
        }
        return metCondition; 
    }
    
    private static boolean Resolve_Logical(double[] dataArr, Condition c, Data dataObj){
        boolean result  = false;
        switch(c.GetOperator()){
            case '|':
                result = (Met(dataArr,c.GetLeft(),dataObj) || Met(dataArr,c.GetRight(),dataObj));
                break;
            case '&':
                result = (Met(dataArr,c.GetLeft(),dataObj) && Met(dataArr,c.GetRight(),dataObj));
                break;
            case '!':
                result = (!Met(dataArr,c.GetRight(),dataObj));
                break;
        }
        return result;
    }
    
    private static boolean Resolve_Relational(double[] dataArr, Condition c, Data dataObj){
        boolean result  = false;
        switch(c.GetOperator()){
            case '>':
                result = (Resolve_Arithmetic(dataArr,c.GetLeft(), dataObj) > Resolve_Arithmetic(dataArr,c.GetRight(),dataObj));
                break;
            case '<':
                result = (Resolve_Arithmetic(dataArr,c.GetLeft(), dataObj) < Resolve_Arithmetic(dataArr,c.GetRight(), dataObj));
                break;
            case 'g':
                result = (Resolve_Arithmetic(dataArr,c.GetLeft(), dataObj) >= Resolve_Arithmetic(dataArr,c.GetRight(), dataObj));
                break;
            case 'l':
                result = (Resolve_Arithmetic(dataArr,c.GetLeft(), dataObj) <= Resolve_Arithmetic(dataArr,c.GetRight(), dataObj));
                break;
            case '=':
                result = (Resolve_Arithmetic(dataArr,c.GetLeft(), dataObj) == Resolve_Arithmetic(dataArr,c.GetRight(), dataObj));
                break;
        }
        return result;
    }
    
    private static double Resolve_Arithmetic(double[] dataArr, Condition c, Data dataObj){
        double result = 0;
        if(c.GetType() == 'a'){
            result  = dataArr[dataObj.GetPosition(c.GetOperator())];
        }else{
            switch(c.GetOperator()){
                case '+':
                    result  = Resolve_Arithmetic(dataArr, c.GetLeft(), dataObj) + Resolve_Arithmetic(dataArr, c.GetRight(),dataObj);
                    break;
                case '-':
                    result  = Resolve_Arithmetic(dataArr, c.GetLeft(), dataObj) - Resolve_Arithmetic(dataArr, c.GetRight(), dataObj);
                    break;
                case '*':
                    result  = Resolve_Arithmetic(dataArr, c.GetLeft(), dataObj) * Resolve_Arithmetic(dataArr, c.GetRight(), dataObj);
                    break;
                case '/':
                    result  = Resolve_Arithmetic(dataArr, c.GetLeft(), dataObj) / Resolve_Arithmetic(dataArr, c.GetRight(), dataObj);
                    break;
            }
        }
        return result;
    } 

}
