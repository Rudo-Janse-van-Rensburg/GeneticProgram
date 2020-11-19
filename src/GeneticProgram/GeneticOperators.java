package GeneticProgram;
 
import java.util.ArrayList;

public class GeneticOperators {
    
    public static int initialMaxDepth                   = 5;
    
    public static int maxDepth                          = 10;
            
    private final static String[] logical               = new String[]{
        "AND", "OR", "NOT"
    };
    
    private final static String[] relational            = new String[]{
        ">", "<", ">=", "<=", "=="
    };
    
    private final static String[] arithmetic            = new String[]{
        "+", "-", "*", "/"
    };
    
    private final static String[] conditionTypes        = new String[]{
        "Logical",
        "Relational",
        "Arithmetic",
        "Attribute"
    };
    
    private final static String[] ifTypes               = new String[]{
        "If",
        "Class"
    };
    
    private final static String[][] conditionCompatible = new String[][]{
        {"Logical","Relational"},
        {"Relational","Logical"},
        {"Arithmetic","Attribute"},
        {"Attribute","Arithmetic"} 
    }; 
    
    private final static String[][] ifCompatible        = new String[][]{
        {"If","Class"},
        {"Class","If"}
    }; 
    
    private static String[] attributes;

    public static void InitializeAttributes() {
        attributes = new String[Data.GetNumberAttributes()-1];
        int pos = 0;
        for (int i = 0; i < Data.GetNumberAttributes(); i++) 
            if (!Data.GetAttribute(i).equalsIgnoreCase(Data.GetClass())) 
                GeneticOperators.attributes[pos++] = Data.GetAttribute(i);
    } 
    
    public static If Grow(int depth, int maxDepth) {
        if (depth < maxDepth) {
            if (depth == 0 || Data.GetPercentage() <= 0.5) {
                Condition c = CreateRootCondition(depth + 1, maxDepth);
                If t        = Grow(depth + 1 , maxDepth);
                If f        = Grow(depth + 1 , maxDepth);
                return new If(c, t, f);
            } else 
                return new Class(Data.GetRandomIntExclusive(0, Data.GetNumberClasses()));
            
        } else 
            return new Class(Data.GetRandomIntExclusive(0, Data.GetNumberClasses()));
    } 
    
    public static void Mutate(Individual m){
        //System.out.print("MUTATING ");
        if(!m.GetRoot().GetType().equals("Class") && Data.GetPercentage() < 0.5){
            //System.out.print("CONDITION\n");
            int conditionPlace;
            String type;
            Condition mutationPoint = null;
            do{
                conditionPlace  = Data.GetRandomIntExclusive(0, conditionTypes.length);
                type            = conditionTypes[conditionPlace];
                mutationPoint   = GetConditionCrossover(m, new String[]{type});
            }while(mutationPoint == null);
            MutateCondition(mutationPoint,conditionCompatible[conditionPlace]);
        }else{
            //System.out.print("IF\n");
            int ifPlace;
            String type;
            If mutationPoint    = null;
            do{
                ifPlace         = Data.GetRandomIntExclusive(0, ifTypes.length);
                type            = ifTypes[ifPlace];
                mutationPoint   = GetIfCrossover(m, new String[]{type});
            }while(mutationPoint == null);
            MutateIf(mutationPoint,ifCompatible[ifPlace]);
        }
        Trim(m, 0, maxDepth);
    }
    
    private static void MutateCondition(Condition c, String [] compatible){
        int newTypeIndex    = Data.GetRandomIntExclusive(0, compatible.length);
        String newType      = compatible[newTypeIndex];
        //System.out.println(c.GetType()+" -> "+newType);
        //System.out.print(c.GetOperator()+" -> ");
        c.SetType(newType);
        String newOperator  = "";
        switch (newType) {
            case "Logical":
                newOperator  = logical[Data.GetRandomIntExclusive(0,logical.length)];
                c.SetOperator(newOperator);
                if(!c.GetOperator().equals("NOT")){
                    c.SetLeft((Data.GetPercentage() < 0.5)? CreateLogicalCondition(0,maxDepth) : CreateRelationalCondition(0,maxDepth));
                    c.SetRight((Data.GetPercentage() < 0.5)? CreateLogicalCondition(0,maxDepth) : CreateRelationalCondition(0,maxDepth));
                }else{
                    c.SetLeft(null);
                    c.SetRight((Data.GetPercentage() < 0.5)? CreateLogicalCondition(0,maxDepth) : CreateRelationalCondition(0,maxDepth));
                }   
                break;
            case "Relational":
                newOperator  = relational[Data.GetRandomIntExclusive(0,relational.length)];
                c.SetOperator(newOperator);
                c.SetLeft((Data.GetPercentage() < 0.5)? CreateArithmeticCondition(0,maxDepth) : CreateAttributeCondition(0,maxDepth));
                c.SetRight((Data.GetPercentage() < 0.5)? CreateArithmeticCondition(0,maxDepth) : CreateAttributeCondition(0,maxDepth));
                break;
            case "Arithmetic":
                newOperator = arithmetic[Data.GetRandomIntExclusive(0, arithmetic.length)];
                c.SetOperator(newOperator);
                c.SetLeft((Data.GetPercentage() < 0.5)? CreateArithmeticCondition(0,maxDepth) : CreateAttributeCondition(0,maxDepth));
                c.SetRight((Data.GetPercentage() < 0.5)? CreateArithmeticCondition(0,maxDepth) : CreateAttributeCondition(0,maxDepth));
                break;
            case "Attribute":
                newOperator = attributes[Data.GetRandomIntExclusive(0, attributes.length)];
                c.SetOperator(newOperator);
                c.SetLeft(null);
                c.SetRight(null);
                break;
        }
        //System.out.print(c.GetOperator()+"\n");
    }

    private static void MutateIf(If i, String [] compatible){
        int newTypeIndex    = Data.GetRandomIntExclusive(0,compatible.length);
        String newType      = compatible[newTypeIndex];
        //System.out.println(i.GetType()+" -> "+newType);
        i.SetType(newType);
        switch(newType){
            case "If":
                i.SetCondition((Data.GetPercentage() < 0.5)? CreateLogicalCondition(0,maxDepth) : CreateRelationalCondition(0,maxDepth));
                i.SetTrue(Grow(0, 3));
                i.SetFalse(Grow(0, 3));
                i.SetClass(Double.NaN);
                break;
            case "Class":
                i.SetCondition(null);
                i.SetTrue(null);
                i.SetFalse(null);
                //i.SetClass(Data.GetRandomIntInclusive(Data.GetMinMax(Data.GetClass())));
                i.SetClass(Data.GetRandomIntExclusive(0, Data.GetNumberClasses()));
                break;
        }
    }
    
    public static Individual[] Crossover(Individual a, Individual b){
        //System.out.print("CROSSOVER ");
        Individual newA = new Individual(a);
        Individual newB = new Individual(b);
        
        if(!newA.GetRoot().GetType().equals("Class") && !newB.GetRoot().GetType().equals("Class") && Data.GetPercentage() < 0.5){
            //System.out.print("CONDITION\n"); 
            int conditionPlace;
            String type;
            Condition crossoverA;
            do{
                conditionPlace  = Data.GetRandomIntExclusive(0, conditionTypes.length);
                type            = conditionTypes[conditionPlace];
                crossoverA      = GetConditionCrossover(newA, new String[]{type});
            }while(crossoverA == null);
            

            Condition tempA;
            //System.out.print(crossoverA.GetType()+" -> ");
            switch(crossoverA.GetType()){
                case "Logical":
                    tempA               = new Logical(crossoverA);
                    break;
                case "Relational":
                    tempA               = new Relational(crossoverA);
                    break;
                case "Arithmetic":
                    tempA               = new Arithmetic(crossoverA);
                    break;
                case "Attribute":
                    tempA               = new Attribute(crossoverA);
                    break;
                default:
                    tempA               = new Logical(crossoverA);
                    break;
            }
            Condition crossoverB;
            do{
                crossoverB = GetConditionCrossover(newB, conditionCompatible[conditionPlace]);
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
            String type;
            If crossoverA;
            do{
                ifPlace     = Data.GetRandomIntExclusive(0,ifTypes.length);
                type        = ifTypes[ifPlace];
                crossoverA  = GetIfCrossover(newA, new String[]{type});
            }while(crossoverA == null);
            
            //System.out.print(crossoverA.GetType()+" -> ");
            If tempA;
            switch(crossoverA.GetType()){
                case "If":
                    tempA               = new If(crossoverA);
                    break;
                case "Class":
                    tempA               = new Class(crossoverA);
                    break;
                default:
                    tempA               = new If(crossoverA);
                    break;
            }
            
            If crossoverB           =  GetIfCrossover(newB, ifCompatible[ifPlace]);
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
        Trim(newA,0,maxDepth);
        Trim(newB,0,maxDepth); 
        return new Individual[]{newA,newB};
    } 
    
    private static Condition GetConditionCrossover(Individual i,String[] types){
        ArrayList<Condition> condition  = new ArrayList<>();
        GetConditionNodes(condition,i.GetRoot(),types);
        return condition.size() > 0 ? condition.get(Data.GetRandomIntExclusive(0, condition.size())) : null;
    }
    
    private static If GetIfCrossover(Individual i, String[] types){
        ArrayList<If> if_nodes          = new ArrayList<>();
        GetIfNodes(if_nodes, i.GetRoot(), types);
        return if_nodes.size() > 0 ? if_nodes.get(Data.GetRandomIntExclusive(0, if_nodes.size())) : null;
    }
    
    private static void GetConditionNodes(ArrayList<Condition> condition,If i,String[] types){
        if(!i.GetType().equals("Class")){
            TraverseCondition(condition, i.GetCondition(), types);
            GetConditionNodes(condition, i.GetTrue(), types);
            GetConditionNodes(condition, i.GetFalse(), types);
        }
    }
    
    private static void TraverseCondition(ArrayList<Condition> condition, Condition current, String[] types){
        int i = 0;
        while(i < types.length && !current.GetType().equals(types[i])){
            ++i;
        }
        if(i < types.length){
            condition.add(current);
        }
        if(!current.GetType().equals("Attribute")){
            if(!current.GetOperator().equals("NOT")){
                TraverseCondition(condition, current.GetLeft(), types);
            }
            TraverseCondition(condition, current.GetRight(), types);
        }
    }
    
    private static void GetIfNodes(ArrayList<If> a, If current,String[] types){
        int i = 0;
        while(i < types.length && !current.GetType().equals(types[i])){
            ++i;
        }
        if(i < types.length){
            a.add(current);
        }
        if(!current.GetType().equals("Class")){
            GetIfNodes(a, current.GetTrue(), types);
            GetIfNodes(a, current.GetFalse(), types);
        }
    } 
    
    public static void Trim(Individual i, int depth, int maxDepth){
        TrimPrimitive(i.GetRoot(), depth, maxDepth);
    }
    
    private static void TrimPrimitive(If p, int depth, int maxDepth){
        if(depth < maxDepth && p.GetType().equals("If")){
            TrimCondition(p.GetCondition(), depth + 1, maxDepth, null);
            TrimPrimitive(p.GetTrue(), depth + 1, maxDepth);
            TrimPrimitive(p.GetFalse(), depth + 1, maxDepth);
        }else if(p.GetType().equals("If")){
            p.SetType("Class");
            p.SetClass(Data.GetRandomIntExclusive(0, Data.GetNumberClasses()));
            //p.SetClass(Data.GetRandomIntInclusive(Data.GetMinMax(Data.GetClass())));
            p.SetTrue(null);
            p.SetFalse(null);
            p.SetCondition(null);
        } 
    }
    
    private static void TrimCondition(Condition c, int depth, int maxDepth, String parentType){
        if(parentType != null){
            /*Not the root of the condition*/
            if(depth < maxDepth - 2){
                //No need to trim the tree at this node
                switch(c.GetType()){
                    case "Logical":
                        switch(c.GetOperator()){
                            case "NOT":
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                break;
                            default:
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                break;
                        }
                        break;
                    case "Relational":
                        TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                        break;
                    case "Arithmetic":
                        TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                        break;
                    case "Attribute":
                        break;
                    default:
                        TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                        break;
                }
            }else if(depth == maxDepth - 2){
                switch(parentType){
                    case "Logical":
                        //parent is logical
                        switch(c.GetType()){
                            case "Logical":
                                switch(c.GetOperator()){
                                    case "NOT":
                                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                        break;
                                    default:
                                        TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                        break;
                                }
                                break;
                            case "Relational":
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                break;
                            case "Arithmetic":
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                break;
                            case "Attribute":
                                break;
                        }
                        break;
                    case "Relational":
                        //parent is relational
                        switch(c.GetType()){
                            case "Arithmetic":
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                break;
                            case "Attribute":
                                break;
                        }
                        break;
                    case "Arithmetic":
                        //parent is arithmetic
                        TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                        TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                        break; 
                } 
            }else if(depth == maxDepth - 1){
                switch(parentType){
                    case "Logical":
                        //parent is logical
                        switch(c.GetType()){
                            case "Logical":
                                c.SetType("Relational");
                                c.SetOperator(relational[Data.GetRandomIntExclusive(0, relational.length)]);
                                c.SetLeft(CreateArithmeticCondition(depth + 1, maxDepth));
                                c.SetRight(CreateArithmeticCondition(depth + 1, maxDepth));
                                break;
                            case "Relational":
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                break;
                            case "Arithmetic":
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                break;
                            case "Attribute":
                                break;
                        }
                        break;
                    case "Relational":
                        //parent is relational
                        switch(c.GetType()){
                            case "Arithmetic":
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                break;
                            case "Attribute":
                                break;
                        }
                        break;
                    case "Arithmetic":
                        //parent is arithmetic
                        switch(c.GetType()){
                            case "Arithmetic":
                                TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                                TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                                break;
                            case "Attribute":
                                break;
                        }
                        break; 
                }
            }else if(depth == maxDepth){
                switch(parentType){
                    case "Relational":
                        //parent is relational
                        switch(c.GetType()){
                            case "Arithmetic":
                                c.SetType("Attribute");
                                c.SetOperator(attributes[Data.GetRandomIntExclusive(0, attributes.length)]);
                                c.SetLeft(null);
                                c.SetRight(null);
                                break;
                            case "Attribute":
                                break;
                        }
                        break;
                    case "Arithmetic":
                        //parent is arithmetic
                        switch(c.GetType()){
                            case "Arithmetic":
                                c.SetType("Attribute");
                                c.SetOperator(attributes[Data.GetRandomIntExclusive(0,attributes.length)]);
                                c.SetLeft(null);
                                c.SetRight(null);
                                break;
                            case "Attribute":
                                break;
                        }
                        break;
                    
                }
            }
            
        }else{
            /*Root of Condition*/
            switch(c.GetType()){
                case "Logical":
                    switch(c.GetOperator()){
                        case "NOT":
                            TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                            break;
                        case "AND":
                            TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                            TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                            break;
                        case "OR":
                            TrimCondition(c.GetLeft(), depth + 1, maxDepth, c.GetType());
                            TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                            break;
                    }
                    break;
                case "Relational":
                    TrimCondition(c.GetLeft(), depth + 1 , maxDepth, c.GetType());
                    TrimCondition(c.GetRight(), depth + 1, maxDepth, c.GetType());
                    break;
            }
        }
        
    }  
    
    private static Condition CreateRootCondition(int depth,int maxDepth) {
        if(depth < maxDepth - 2 && Data.GetPercentage() < 0.5)
            return CreateLogicalCondition(depth + 1, maxDepth);
        else  
            return CreateRelationalCondition(depth + 1, maxDepth);
    }

    private static Logical CreateLogicalCondition(){
        double percentage           = Data.GetPercentage();
        String operator             = logical[Data.GetRandomIntExclusive(0,logical.length)];
        if(!operator.equals("NOT")){
            return new Logical(
                    operator,
                    (Data.GetPercentage() < 0.5) ? CreateLogicalCondition() : CreateRelationalCondition(),
                    (Data.GetPercentage() < 0.5) ? CreateLogicalCondition() : CreateRelationalCondition()
            );
        }else{
           return new Logical(
                    operator,
                    (Data.GetPercentage() < 0.5) ? CreateLogicalCondition() : CreateRelationalCondition()
            ); 
        }
    }
    
    private static Logical CreateLogicalCondition(int depth, int maxDepth) {
        double percentage = Data.GetPercentage();
        if(depth < maxDepth - 2){
            if(percentage < 1/3*1/3)
                return new Logical(
                         GeneticOperators.logical[2],//"NOT"
                        (Data.GetPercentage() < 0.5) ? CreateLogicalCondition(depth + 1, maxDepth) : CreateRelationalCondition(depth + 1, maxDepth)
                );
            else 
                return new Logical(
                        GeneticOperators.logical[Data.GetRandomIntExclusive(0, GeneticOperators.logical.length-1)],
                        (Data.GetPercentage() < 0.5) ? CreateLogicalCondition(depth + 1, maxDepth) : CreateRelationalCondition(depth + 1, maxDepth),
                        (Data.GetPercentage() < 0.5) ? CreateLogicalCondition(depth + 1, maxDepth) : CreateRelationalCondition(depth + 1, maxDepth) 
                );
        }else{
            if(percentage < 1/3*1/3)
                return new Logical(
                        GeneticOperators.logical[2],
                        CreateRelationalCondition(depth + 1, maxDepth)
                );
            else
                return new Logical(
                        GeneticOperators.logical[Data.GetRandomIntExclusive(0, GeneticOperators.logical.length-1)],
                        CreateRelationalCondition(depth + 1, maxDepth),
                        CreateRelationalCondition(depth + 1, maxDepth)
                );
        } 
    }

    private static Relational CreateRelationalCondition(){
        String operator                 = relational[Data.GetRandomIntExclusive(0,relational.length)];
        return new Relational(
                operator,
                (Data.GetPercentage() < 0.5) ? CreateArithmeticCondition() : CreateAttributeCondition(),
                (Data.GetPercentage() < 0.5) ? CreateArithmeticCondition() : CreateAttributeCondition()
        );
    }
    
    private static Relational CreateRelationalCondition(int depth, int maxDepth) {
        if(depth < maxDepth - 1)
            return new Relational(
                GeneticOperators.relational[Data.GetRandomIntExclusive(0, GeneticOperators.relational.length)],
                (Data.GetPercentage() < 0.5) ? CreateArithmeticCondition(depth + 1, maxDepth) : CreateAttributeCondition(depth+1, maxDepth),
                (Data.GetPercentage() < 0.5)  ? CreateArithmeticCondition(depth + 1, maxDepth) : CreateAttributeCondition(depth+1, maxDepth)
            );
        else 
            return new Relational(
                GeneticOperators.relational[Data.GetRandomIntExclusive(0, GeneticOperators.relational.length)],
                CreateArithmeticCondition(depth + 1, maxDepth),
                CreateArithmeticCondition(depth + 1, maxDepth)
            );
    }

    private static Arithmetic CreateArithmeticCondition(){
        String operator                 = arithmetic[Data.GetRandomIntExclusive(0,arithmetic.length)];
        return new Arithmetic(
                operator,
                (Data.GetPercentage() < 0.5) ? CreateArithmeticCondition() : CreateAttributeCondition(),
                (Data.GetPercentage() < 0.5) ? CreateArithmeticCondition() : CreateAttributeCondition()
        );
    }
    
    private static Arithmetic CreateArithmeticCondition(int depth, int maxDepth) {
        double p    = Data.GetPercentage();
        if(depth < maxDepth - 1)
            return new Arithmetic(
                    GeneticOperators.arithmetic[Data.GetRandomIntExclusive(0, GeneticOperators.arithmetic.length)],
                    (Data.GetPercentage() < 0.5) ? CreateArithmeticCondition(depth + 1, maxDepth) : CreateAttributeCondition(depth + 1, maxDepth),
                    (Data.GetPercentage() < 0.5) ? CreateArithmeticCondition(depth + 1, maxDepth) : CreateAttributeCondition(depth + 1, maxDepth)
            );
        else
            return new Arithmetic(
                    GeneticOperators.arithmetic[Data.GetRandomIntExclusive(0, GeneticOperators.arithmetic.length)],
                    CreateAttributeCondition(depth+1, maxDepth),
                    CreateAttributeCondition(depth+1, maxDepth)
            ); 
    }

    private static Attribute CreateAttributeCondition(){
        return new Attribute(GeneticOperators.attributes[Data.GetRandomIntExclusive(0,GeneticOperators.attributes.length)]);
    }
    
    private static Attribute CreateAttributeCondition(int depth, int maxDepth){
        return new Attribute(GeneticOperators.attributes[Data.GetRandomIntExclusive(0, GeneticOperators.attributes.length)]);
    }
    
}
