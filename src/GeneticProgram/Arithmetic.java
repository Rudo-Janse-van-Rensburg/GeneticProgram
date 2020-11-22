package GeneticProgram;

//! Arithmetic Class
/*!
This class represents an Arithmetic node in the condition sub-branch,
for which it is an intermediate node.
*/
public class Arithmetic extends Relational{
    
    //! A constructor
    /*!
    @param operator -(+,-,*,/)
    @param r - either a arithmetic(+,-,*,/) or Attribute
    @param l - either a arithmetic(+,-,*,/) or Attribute
    */
    public Arithmetic(char operator, Arithmetic l, Arithmetic r){
        super('A',operator,l,r);
    }
    
    protected Arithmetic(char type,char operator, Arithmetic l, Arithmetic r){
        super(type,operator,l,r);
    }
    
    //!A copy constructor
    /*!
    @param c - a condition node to copy.
    */
    public Arithmetic(Condition c){
        super(c);
    }
}
