 
package GeneticProgram;
 
public abstract class Primitive {
    
    private String type;
    
    protected Primitive(String type){
        this.type   = type;
    }
    
    public String GetType(){
        return this.type;
    }  
    
    public void SetType(String type){
        this.type   = type;
    }
    
    public abstract String ToString(); 
    
    public void Print(){
        System.out.print(this.ToString());
    }
} 