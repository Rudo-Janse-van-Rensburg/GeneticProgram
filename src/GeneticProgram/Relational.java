package GeneticProgram; 

//! Relational class
/*!
This class represents a Relational node of the conditional sub-branch,
for which it is an intermediate node.
*/
public class Relational extends Logical{ 
    
    //!A constructor
    /*!
    @param operator -(>,<,>=,<=,==)
    @param r - either a relational operator(>,<,>=,<=,==), arithmetic(+,-,*,/) or Attribute
    @param l - either a relational operator(>,<,>=,<=,==), arithmetic(+,-,*,/) or Attribute
    */
    public Relational(char operator, Relational l, Relational r){
        super('R',operator,l,r);
    }
    
    protected Relational(char type,char operator, Relational l, Relational r){
        super(type,operator,l,r);
    }
    //!A copy construcotr
    /*!
    @param c - A conditional node to copy.
    */
    public Relational(Condition c){
        super(c);
    }
    
    
    
    
}
