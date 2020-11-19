package GeneticProgram; 

public class Relational extends Logical{ 
    
    /**
     * @param operator -(>,<,>=,<=,==)
     * @param r - either a relational operator(>,<,>=,<=,==), arithmetic(+,-,*,/) or Attribute
     * @param l - either a relational operator(>,<,>=,<=,==), arithmetic(+,-,*,/) or Attribute
     */
    public Relational(String operator, Relational l, Relational r){
        super('R',operator,l,r);
    }
    
    protected Relational(char type,String operator, Relational l, Relational r){
        super(type,operator,l,r);
    }
    
    public Relational(Condition c){
        super(c);
    }
    
    
    
    
}
