package GeneticProgram;
//! Logical Class
/*!
This class represents a Logical node in the condition sub-branch,
for which it is an intermediate node.
*/
public class Logical extends Condition {
     
    //! A constructor
    /*!
    @param operator - AND or OR
    @param r - either a relational operator(>,<,>=,<=,==) or another logical condition(AND, OR)
    @param l
    */
    public Logical(char operator, Logical l,Logical r){
        super('L',operator,l,r);
    }
    
    public Logical(Condition c){
        super(c);
    }
    
    //! A constructor
    /*!
    @param operator - NOT
    @param r - either a relational operator(>,<,>=,<=,==) or another logical condition(NOT)
    */ 
    public Logical(char operator, Logical r){
        super('L',operator,null,r);
        
    }
    
    protected Logical(char type,char operator, Logical l, Logical r){
        super(type,operator,l,r);
    } 

    
}
