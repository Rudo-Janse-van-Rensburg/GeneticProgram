package GeneticProgram;
//! Primitive class
/*!
This class represents an abstract class for all Primitives in the parse tree.
*/
public abstract class Primitive {
    
    private char type;
    
    protected Primitive(char type){
        this.type   = type;
    }
    
    public char GetType(){
        return this.type;
    }  
    
    public void SetType(char type){
        this.type   = type;
    }
    
    public abstract String ToString(); 
    
    public void Print(){
        System.out.print(this.ToString());
    }
} 