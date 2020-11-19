package GeneticProgram;
 
public class Logical extends Condition {
     
    
    /**
     * @param operator - AND or OR
     * @param r - either a relational operator(>,<,>=,<=,==) or another logical condition(AND, OR)
     * @param l
     */
    public Logical(String operator, Logical l,Logical r){
        super("Logical",operator,l,r);
    }
    
    public Logical(Condition c){
        super(c);
    }
    
    /**
     * @param operator - NOT
     * @param r - either a relational operator(>,<,>=,<=,==) or another logical condition(NOT)
     */
    public Logical(String operator, Logical r){
        super("Logical",operator,null,r);
        
    }
    
    protected Logical(String type,String operator, Logical l, Logical r){
        super(type,operator,l,r);
    } 

    
}
