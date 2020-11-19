 
package GeneticProgram;
 
public class Class extends If{
    
    public Class(double cls){
        super('C',cls);
    }
    
    public Class(If c){
        super('C',c.GetClass());
    }
    
}
