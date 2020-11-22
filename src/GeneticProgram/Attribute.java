package GeneticProgram;
 
//! Attribute Class
/*!
This class represents an Attribute node in the condition sub-branch, 
for which it is a terminal node.
*/
public class Attribute extends Arithmetic {
    //!A constructor
    /*!
    @param attribute_name - an attribute 
    */
    public Attribute(char attribute_name){
        super('a',attribute_name,null,null); 
    } 
    
    //!A copy constructor
    /*!
    @param c - a condition node to copy.
    */
    public Attribute(Condition c){
        super(c);
    } 
}
