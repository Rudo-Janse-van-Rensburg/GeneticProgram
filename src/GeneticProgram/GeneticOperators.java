package GeneticProgram;
 
import java.util.ArrayList;

public class GeneticOperators {
    
    public static int initialMaxDepth                   = 5;
    
    public static int maxDepth                          = 10;
    /*    
    private final static String[] logical               = new String[]{
        "AND", "OR", "NOT"
    };
    
    private final static String[] relational            = new String[]{
        ">", "<", ">=", "<=", "=="
    };
    
    private final static String[] arithmetic            = new String[]{
        "+", "-", "*", "/"
    };
    */
    private final static char[] logical                 = new char[]{
        '&','|','!'
    };
    
    private final static char[] relational              = new char[]{
        '>','<','g','l','='
    };
        
    private final static char[] arithmetic              = new char[]{
        '+','-','*','/'
    };
    
    private final static char[] conditionTypes          = new char[]{
        'L',
        'R',
        'A',
        'a'
    };
    
    private final static char[] ifTypes                 = new char[]{
        'I',
        'C'
    };
    
    private final static char[][] conditionCompatible   = new char[][]{
        {'L','R'},
        {'R','L'},
        {'A','a'},
        {'a','A'} 
    }; 
    
    private final static char[][] ifCompatible          = new char[][]{
        {'I','C'},
        {'C','I'}
    };
    
    /*
    private static String[] attributes;

    public static void InitializeAttributes(Data dataObj) {
        attributes = new String[dataObj.GetNumberAttributes()-1];
        int pos = 0;
        for (int i = 0; i < dataObj.GetNumberAttributes(); i++) 
            if (!dataObj.GetAttribute(i).equalsIgnoreCase(dataObj.GetClass())) 
                GeneticOperators.attributes[pos++] = dataObj.GetAttribute(i);
    } 
    */
    public static If Grow(int depth, int maxDepth, Data dataObj) {
        if (depth < maxDepth) {
            if (depth == 0 || dataObj.GetPercentage() <= 0.5) {
                Condition c = CreateRootCondition(depth + 1, maxDepth,dataObj);
                If t        = Grow(depth + 1 , maxDepth, dataObj);
                If f        = Grow(depth + 1 , maxDepth, dataObj);
                return new If(c, t, f);
            } else 
                return new Class(dataObj.GetRandomIntExclusive(0, dataObj.GetNumberClasses()));
            
        } else 
            return new Class(dataObj.GetRandomIntExclusive(0, dataObj.GetNumberClasses()));
    } 
    
    public static void Mutate(Individual m, Data dataObj){
        //System.out.print("MUTATING ");
        if(!(m.GetRoot().GetType() == 'C') && dataObj.GetPercentage() < 0.5){
            //System.out.print("CONDITION\n");
            int conditionPlace;
            char type;
            Condition mutationPoint = null;
            do{
                conditionPlace  = dataObj.GetRandomIntExclusive(0, conditionTypes.length);
                type            = conditionTypes[conditionPlace];
                mutationPoint   = GetConditionCrossover(m, new char[]{type},dataObj);
            }while(mutationPoint == null);
            MutateCondition(mutationPoint,conditionCompatible[conditionPlace],dataObj);
        }else{
            //System.out.print("IF\n");
            int ifPlace;
            char type;
            If mutationPoint    = null;
            do{
                ifPlace         = dataObj.GetRandomIntExclusive(0, ifTypes.length);
                type            = ifTypes[ifPlace];
                mutationPoint   = GetIfCrossover(m, new char[]{type}, dataObj);
            }while(mutationPoint == null);
            MutateIf(mutationPoint,ifCompatible[ifPlace],dataObj);
        }
        Trim(m, 0, maxDepth, dataObj);
    }
    
    private static void MutateCondition(Condition c, char [] compatible, Data dataObj){
        int newTypeIndex    = dataObj.GetRandomIntExclusive(0, compatible.length);
        char newType      = compatible[newTypeIndex]; 
        c.SetType(newType);
        char newOperator;
        switch (newType) {
            case 'L':
                newOperator  = logical[dataObj.GetRandomIntExclusive(0,logical.length)];
                c.SetOperator(newOperator);
                if(c.GetOperator() != '!'){
                    c.SetLeft((dataObj.GetPercentage() < 0.5)? CreateLogicalCondition(0,maxDepth,dataObj) : CreateRelationalCondition(0,maxDepth,dataObj));
                    c.SetRight((dataObj.GetPercentage() < 0.5)? CreateLogicalCondition(0,maxDepth,dataObj) : CreateRelationalCondition(0,maxDepth,dataObj));
                }else{
                    c.SetLeft(null);
                    c.SetRight((dataObj.GetPercentage() < 0.5)? CreateLogicalCondition(0,maxDepth,dataObj) : CreateRelationalCondition(0,maxDepth,dataObj));
                }   
                break;
            case 'R':
                newOperator  = relational[dataObj.GetRandomIntExclusive(0,relational.length)];
                c.SetOperator(newOperator);
                c.SetLeft((dataObj.GetPercentage() < 0.5)? CreateArithmeticCondition(0,maxDepth,dataObj) : CreateAttributeCondition(0,maxDepth,dataObj));
                c.SetRight((dataObj.GetPercentage() < 0.5)? CreateArithmeticCondition(0,maxDepth,dataObj) : CreateAttributeCondition(0,maxDepth,dataObj));
                break;
            case 'A':
                newOperator = arithmetic[dataObj.GetRandomIntExclusive(0, arithmetic.length)];
                c.SetOperator(newOperator);
                c.SetLeft((dataObj.GetPercentage() < 0.5)? CreateArithmeticCondition(0,maxDepth,dataObj) : CreateAttributeCondition(0,maxDepth,dataObj));
                c.SetRight((dataObj.GetPercentage() < 0.5)? CreateArithmeticCondition(0,maxDepth,dataObj) : CreateAttributeCondition(0,maxDepth,dataObj));
                break;
            case 'a':
                newOperator = dataObj.GetAttributesWithoutClass()[dataObj.GetRandomIntExclusive(0, dataObj.GetAttributesWithoutClass().length)];
                c.SetOperator(newOperator);
                c.SetLeft(null);
                c.SetRight(null);
                break;
        }
        //System.out.print(c.GetOperator()+"\n");
    }

    private static void MutateIf(If i, char [] compatible, Data dataObj){
        int newTypeIndex    = dataObj.GetRandomIntExclusive(0,compatible.length);
        char newType        = compatible[newTypeIndex];
        //System.out.println(i.GetType()+" -> "+newType);
        i.SetType(newType);
        switch(newType){
            case 'I':
                i.SetCondition((dataObj.GetPercentage() < 0.5)? CreateLogicalCondition(0,maxDepth,dataObj) : CreateRelationalCondition(0,maxDepth,dataObj));
                i.SetTrue(Grow(0, 3, dataObj));
                i.SetFalse(Grow(0, 3, dataObj));
                i.SetClass(Double.NaN);
                break;
            case 'C':
                i.SetCondition(null);
                i.SetTrue(null);
                i.SetFalse(null);
                //i.SetClass(Data.GetRandomIntInclusive(Data.GetMinMax(Data.GetClass())));
                i.SetClass(dataObj.GetRandomIntExclusive(0, dataObj.GetNumberClasses()));
                break;
        }
    }
    
    public static Individual[] Crossover(Individual a, Individual b, Data dataObj){
        //System.out.print("CROSSOVER ");
        Individual newA = new Individual(a);
        Individual newB = new Individual(b);
        
        if(newA.GetRoot().GetType() != 'C' && newB.GetRoot().GetType() != 'C'  && dataObj.GetPercentage() < 0.5){
            //System.out.print("CONDITION\n"); 
            int conditionPlace;
            char type;
            Condition crossoverA;
            do{
                conditionPlace  = dataObj.GetRandomIntExclusive(0, conditionTypes.length);
                type            = conditionTypes[conditionPlace];
                crossoverA      = GetConditionCrossover(newA, new char[]{type},dataObj);
            }while(crossoverA == null);
            

            Condition tempA;
            //System.out.print(crossoverA.GetType()+" -> ");
            switch(crossoverA.GetType()){
                case 'L':
                    tempA               = new Logical(crossoverA);
                    break;
                case 'R':
                    tempA               = new Relational(crossoverA);
                    break;
                case 'A':
                    tempA               = new Arithmetic(crossoverA);
                    break;
                case 'a':
                    tempA               = new Attribute(crossoverA);
                    break;
                default:
                    tempA               = new Logical(crossoverA);
                    break;
            }
            Condition crossoverB;
            do{
                crossoverB = GetConditionCrossover(newB, conditionCompatible[conditionPlace],dataObj);
            }while(crossoverB == null);
            //System.out.print(crossoverB.GetType()+"\n");
            crossoverA.SetOperator(crossoverB.GetOperator());
            crossoverA.SetLeft(crossoverB.GetLeft());
            crossoverA.SetRight(crossoverB.GetRight());
            crossoverA.SetType(crossoverB.GetType());
            
            crossoverB.SetOperator(tempA.GetOperator());
            crossoverB.SetLeft(tempA.GetLeft());
            crossoverB.SetRight(tempA.GetRight());
            crossoverB.SetType(tempA.GetType());
        }else{
            //System.out.print("IF\n");
            int ifPlace;
            char type;
            If crossoverA;
            do{
                ifPlace     = dataObj.GetRandomIntExclusive(0,ifTypes.length);
                type        = ifTypes[ifPlace];
                crossoverA  = GetIfCrossover(newA, new char[]{type},dataObj);
            }while(crossoverA == null);
            
            //System.out.print(crossoverA.GetType()+" -> ");
            If tempA;
            switch(crossoverA.GetType()){
                case 'I':
                    tempA               = new If(crossoverA);
                    break;
                case 'C':
                    tempA               = new Class(crossoverA);
                    break;
                default:
                    tempA               = new If(crossoverA);
                    break;
            }
            
            If crossoverB           =  GetIfCrossover(newB, ifCompatible[ifPlace],dataObj);
            //System.out.print(crossoverB.GetType()+"\n");
            
            crossoverA.SetCondition(crossoverB.GetCondition());
            crossoverA.SetType(crossoverB.GetType());
            crossoverA.SetTrue(crossoverB.GetTrue());
            crossoverA.SetFalse(crossoverB.GetFalse());
            crossoverA.SetClass(crossoverB.GetClass()); 
            
            crossoverB.SetCondition(tempA.GetCondition());
            crossoverB.SetType(tempA.GetType());
            crossoverB.SetTrue(tempA.GetTrue());
            crossoverB.SetFalse(tempA.GetFalse());
            crossoverB.SetClass(tempA.GetClass());
        } 
        Trim(newA,0,maxDepth,dataObj);
        Trim(newB,0,maxDepth,dataObj); 
        return new Individual[]{newA,newB};
    } 
    
    private static Condition GetConditionCrossover(Individual i,char[] types,Data dataObj){
        ArrayList<Condition> condition  = new ArrayList<>();
        GetConditionNodes(condition,i.GetRoot(),types);
        return condition.size() > 0 ? condition.get(dataObj.GetRandomIntExclusive(0, condition.size())) : null;
    }
    
    private static If GetIfCrossover(Individual i, char[] types,Data dataObj){
        ArrayList<If> if_nodes          = new ArrayList<>();
        GetIfNodes(if_nodes, i.GetRoot(), types);
        return if_nodes.size() > 0 ? if_nodes.get(dataObj.GetRandomIntExclusive(0, if_nodes.size())) : null;
    }
    
    private static void GetConditionNodes(ArrayList<Condition> condition,If i,char[] types){
        if(i.GetType() != 'C'){
            TraverseCondition(condition, i.GetCondition(), types);
            GetConditionNodes(condition, i.GetTrue(), types);
            GetConditionNodes(condition, i.GetFalse(), types);
        }
    }
    
    private static void TraverseCondition(ArrayList<Condition> condition, Condition current, char[] types){
        int i = 0;
        while(i < types.length && current.GetType() != (types[i])){
            ++i;
        }
        if(i < types.length){
            condition.add(current);
        }
        if(current.GetType() != 'a'){
            if(current.GetOperator() != '!'){
                TraverseCondition(condition, current.GetLeft(), types);
            }
            TraverseCondition(condition, current.GetRight(), types);
        }
    }
    
    private static void GetIfNodes(ArrayList<If> a, If current,char[] types){
        int i = 0;
        while(i < types.length && current.GetType() != types[i]){
            ++i;
        }
        if(i < types.length){
            a.add(current);
        }
        if(current.GetType() != 'C'){
            GetIfNodes(a, current.GetTrue(), types);
            GetIfNodes(a, current.GetFalse(), types);
        }
    } 
    
    public static void Trim(Individual i, int depth, int maxDepth,Data dataObj){
        TrimPrimitive(i.GetRoot(), depth, maxDepth,dataObj);
    }
    
    private static void TrimPrimitive(If p, int depth, int maxDepth,Data dataObj){
        if(depth < maxDepth && p.GetType() == 'I'){
            TrimCondition(p.GetCondition(), depth + 1, maxDepth, ' ', dataObj);
            TrimPrimitive(p.GetTrue(), depth + 1, maxDepth, dataObj);
            TrimPrimitive(p.GetFalse(), depth + 1, maxDepth, dataObj);
        }else if(p.GetType() == 'I'){
            p.SetType('C');
            p.SetClass(dataObj.GetRandomIntExclusive(0, dataObj.GetNumberClasses()));
            //p.SetClass(Data.GetRandomIntInclusive(Data.GetMinMax(Data.GetClass())));
            p.SetTrue(null);
            p.SetFalse(null);
            p.SetCondition(null);
        } 
    }
    
    private static void TrimCondition(Condition c, int depth, int maxDepth, char parentType,Data dataObj){
        if(parentType != ' '){
            /*Not the root of the condition*/
            if(depth < maxDepth - 2){
                //No need to trim the tree at this node
                switch(c.GetType()){
                    case 'L':
                        switch(c.GetOperator()){
                            case '!':
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(),dataObj);
                                break;
                            default:
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(),dataObj);
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(), dataObj);
                                break;
                        }
                        break;
                    case 'R':
                        TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(),dataObj);
                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(),dataObj);
                        break;
                    case 'A':
                        TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(),dataObj);
                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(),dataObj);
                        break;
                    case 'a':
                        break;
                    default:
                        TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(),dataObj);
                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(),dataObj);
                        break;
                }
            }else if(depth == maxDepth - 2){
                switch(parentType){
                    case 'L':
                        //parent is logical
                        switch(c.GetType()){
                            case 'L':
                                switch(c.GetOperator()){
                                    case '!':
                                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(), dataObj);
                                        break;
                                    default:
                                        TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(), dataObj);
                                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(), dataObj);
                                        break;
                                }
                                break;
                            case 'R':
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(), dataObj);
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(), dataObj);
                                break;
                            case 'A':
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(), dataObj);
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(), dataObj);
                                break;
                            case 'a':
                                break;
                        }
                        break;
                    case 'R':
                        //parent is relational
                        switch(c.GetType()){
                            case 'A':
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(), dataObj);
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(), dataObj);
                                break;
                            case 'a':
                                break;
                        }
                        break;
                    case 'A':
                        //parent is arithmetic
                        TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(), dataObj);
                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(), dataObj);
                        break; 
                } 
            }else if(depth == maxDepth - 1){
                switch(parentType){
                    case 'L':
                        //parent is logical
                        switch(c.GetType()){
                            case 'L':
                                c.SetType('R');
                                c.SetOperator(relational[dataObj.GetRandomIntExclusive(0, relational.length)]);
                                c.SetLeft(CreateArithmeticCondition(depth + 1, maxDepth,dataObj));
                                c.SetRight(CreateArithmeticCondition(depth + 1, maxDepth,dataObj));
                                break;
                            case 'R':
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(), dataObj);
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(), dataObj);
                                break;
                            case 'A':
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(), dataObj);
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(),dataObj);
                                break;
                            case 'a':
                                break;
                        }
                        break;
                    case 'R':
                        //parent is relational
                        switch(c.GetType()){
                            case 'A':
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(), dataObj);
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(), dataObj);
                                break;
                            case 'a':
                                break;
                        }
                        break;
                    case 'A':
                        //parent is arithmetic
                        switch(c.GetType()){
                            case 'A':
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(), dataObj);
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(), dataObj);
                                break;
                            case 'a':
                                break;
                        }
                        break; 
                }
            }else if(depth == maxDepth){
                switch(parentType){
                    case 'R':
                        //parent is relational
                        switch(c.GetType()){
                            case 'A':
                                c.SetType('a');
                                c.SetOperator(dataObj.GetAttributesWithoutClass()[dataObj.GetRandomIntExclusive(0, dataObj.GetAttributesWithoutClass().length)]);
                                c.SetLeft(null);
                                c.SetRight(null);
                                break;
                            case 'a':
                                break;
                        }
                        break;
                    case 'A':
                        //parent is arithmetic
                        switch(c.GetType()){
                            case 'A':
                                c.SetType('a');
                                c.SetOperator(dataObj.GetAttributesWithoutClass()[dataObj.GetRandomIntExclusive(0,dataObj.GetAttributesWithoutClass().length)]);
                                c.SetLeft(null);
                                c.SetRight(null);
                                break;
                            case 'a':
                                break;
                        }
                        break;
                }
            }
        }else{
            /*Root of Condition*/
            switch(c.GetType()){
                case 'L':
                    switch(c.GetOperator()){
                        case '!':
                            TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(),dataObj);
                            break;
                        case '&':
                            TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(),dataObj);
                            TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(),dataObj);
                            break;
                        case '|':
                            TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType(),dataObj);
                            TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(),dataObj);
                            break;
                    }
                    break;
                case 'R':
                    TrimCondition(c.GetLeft(), depth + 1 , maxDepth, c.GetType(),dataObj);
                    TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType(),dataObj);
                    break;
            }
        }
        
    }  
    
    private static Condition CreateRootCondition(int depth,int maxDepth, Data dataObj) {
        if(depth < maxDepth - 2 && dataObj.GetPercentage() < 0.5)
            return CreateLogicalCondition(depth + 1, maxDepth,dataObj);
        else  
            return CreateRelationalCondition(depth + 1, maxDepth, dataObj);
    }

    private static Logical CreateLogicalCondition(Data dataObj){
        double percentage           = dataObj.GetPercentage();
        char operator             = logical[dataObj.GetRandomIntExclusive(0,logical.length)];
        if(operator != '!'){
            return new Logical(
                    operator,
                    (dataObj.GetPercentage() < 0.5) ? CreateLogicalCondition(dataObj) : CreateRelationalCondition(dataObj),
                    (dataObj.GetPercentage() < 0.5) ? CreateLogicalCondition(dataObj) : CreateRelationalCondition(dataObj)
            );
        }else{
           return new Logical(
                    operator,
                    (dataObj.GetPercentage() < 0.5) ? CreateLogicalCondition(dataObj) : CreateRelationalCondition(dataObj)
            ); 
        }
    }
    
    private static Logical CreateLogicalCondition(int depth, int maxDepth, Data dataObj) {
        double percentage = dataObj.GetPercentage();
        if(depth < maxDepth - 2){
            if(percentage < 1/3*1/3)
                return new Logical(
                         GeneticOperators.logical[2],//"NOT"
                        (dataObj.GetPercentage() < 0.5) ? CreateLogicalCondition(depth + 1, maxDepth,dataObj) : CreateRelationalCondition(depth + 1, maxDepth,dataObj)
                );
            else 
                return new Logical(
                        GeneticOperators.logical[dataObj.GetRandomIntExclusive(0, GeneticOperators.logical.length-1)],
                        (dataObj.GetPercentage() < 0.5) ? CreateLogicalCondition(depth + 1, maxDepth,dataObj) : CreateRelationalCondition(depth + 1, maxDepth, dataObj),
                        (dataObj.GetPercentage() < 0.5) ? CreateLogicalCondition(depth + 1, maxDepth,dataObj) : CreateRelationalCondition(depth + 1, maxDepth, dataObj)
                );
        }else{
            if(percentage < 1/3*1/3)
                return new Logical(
                        GeneticOperators.logical[2],
                        CreateRelationalCondition(depth + 1, maxDepth, dataObj)
                );
            else
                return new Logical(
                        GeneticOperators.logical[dataObj.GetRandomIntExclusive(0, GeneticOperators.logical.length-1)],
                        CreateRelationalCondition(depth + 1, maxDepth, dataObj),
                        CreateRelationalCondition(depth + 1, maxDepth, dataObj)
                );
        } 
    }

    private static Relational CreateRelationalCondition(Data dataObj){
        char  operator                 = relational[dataObj.GetRandomIntExclusive(0,relational.length)];
        return new Relational(
                operator,
                (dataObj.GetPercentage() < 0.5) ? CreateArithmeticCondition(dataObj) : CreateAttributeCondition(dataObj),
                (dataObj.GetPercentage() < 0.5) ? CreateArithmeticCondition(dataObj) : CreateAttributeCondition(dataObj)
        );
    }
    
    private static Relational CreateRelationalCondition(int depth, int maxDepth, Data dataObj) {
        if(depth < maxDepth - 1)
            return new Relational(
                GeneticOperators.relational[dataObj.GetRandomIntExclusive(0, GeneticOperators.relational.length)],
                (dataObj.GetPercentage() < 0.5) ? CreateArithmeticCondition(depth + 1, maxDepth, dataObj) : CreateAttributeCondition(depth+1, maxDepth,dataObj),
                (dataObj.GetPercentage() < 0.5)  ? CreateArithmeticCondition(depth + 1, maxDepth, dataObj) : CreateAttributeCondition(depth+1, maxDepth,dataObj)
            );
        else 
            return new Relational(
                GeneticOperators.relational[dataObj.GetRandomIntExclusive(0, GeneticOperators.relational.length)],
                CreateArithmeticCondition(depth + 1, maxDepth, dataObj),
                CreateArithmeticCondition(depth + 1, maxDepth, dataObj)
            );
    }

    private static Arithmetic CreateArithmeticCondition(Data dataObj){
        char operator                 = arithmetic[dataObj.GetRandomIntExclusive(0,arithmetic.length)];
        return new Arithmetic(
                operator,
                (dataObj.GetPercentage() < 0.5) ? CreateArithmeticCondition(dataObj) : CreateAttributeCondition(dataObj),
                (dataObj.GetPercentage() < 0.5) ? CreateArithmeticCondition(dataObj) : CreateAttributeCondition(dataObj)
        );
    }
    
    private static Arithmetic CreateArithmeticCondition(int depth, int maxDepth, Data dataObj) {
        double p    = dataObj.GetPercentage();
        if(depth < maxDepth - 1)
            return new Arithmetic(
                    GeneticOperators.arithmetic[dataObj.GetRandomIntExclusive(0, GeneticOperators.arithmetic.length)],
                    (dataObj.GetPercentage() < 0.5) ? CreateArithmeticCondition(depth + 1, maxDepth,dataObj) : CreateAttributeCondition(depth + 1, maxDepth,dataObj),
                    (dataObj.GetPercentage() < 0.5) ? CreateArithmeticCondition(depth + 1, maxDepth,dataObj) : CreateAttributeCondition(depth + 1, maxDepth,dataObj)
            );
        else
            return new Arithmetic(
                    GeneticOperators.arithmetic[dataObj.GetRandomIntExclusive(0, GeneticOperators.arithmetic.length)],
                    CreateAttributeCondition(depth+1, maxDepth,dataObj),
                    CreateAttributeCondition(depth+1, maxDepth,dataObj)
            ); 
    }

    private static Attribute CreateAttributeCondition(Data dataObj){
        return new Attribute(dataObj.GetAttributesWithoutClass()[dataObj.GetRandomIntExclusive(0,dataObj.GetAttributesWithoutClass().length)]);
    }
    
    private static Attribute CreateAttributeCondition(int depth, int maxDepth, Data dataObj){
        return new Attribute(dataObj.GetAttributesWithoutClass()[dataObj.GetRandomIntExclusive(0,dataObj.GetAttributesWithoutClass().length)]);
    }
    
}
