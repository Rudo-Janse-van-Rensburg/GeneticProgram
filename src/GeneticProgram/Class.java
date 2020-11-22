package GeneticProgram;

//! Class class
/*!
This class represents a Class node in the If sub-branch, 
for which it is a terminal node.
*/
public class Class extends If{
    
    //!A constructor
    /*!
    @param cls - a class 
    */
    public Class(double cls){
        super('C',cls);
    }
    
    //!A copy constructor
    /*!
    @param c - a If node to copy.
    */
    public Class(If c){
        super('C',c.GetClass());
    }
    
}
