package GeneticProgram;
 
public class Arithmetic extends Relational{
    
    /**
     * @param operator -(+,-,*,/)
     * @param r - either a arithmetic(+,-,*,/) or Attribute
     * @param l - either a arithmetic(+,-,*,/) or Attribute
     */
    public Arithmetic(char operator, Arithmetic l, Arithmetic r){
        super('A',operator,l,r);
    }
    
    protected Arithmetic(char type,char operator, Arithmetic l, Arithmetic r){
        super(type,operator,l,r);
    }
    
    public Arithmetic(Condition c){
        super(c);
    }
}
