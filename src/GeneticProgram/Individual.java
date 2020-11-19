package GeneticProgram;

public class Individual { 
    private If root;

    public Individual(If p){
        this.root   = p;
    }
    
    public Individual(Individual i){
        this.root = new If(i.GetRoot());
    }
    
    public If GetRoot(){
        return this.root;
    }
    
    public void SetRoot(If root){
        this.root = root;
    }
    
    public String ToString(){
        return this.root.ToString();
    }
    
    public void Print(){
        this.root.Print();
    } 
    
    
}
