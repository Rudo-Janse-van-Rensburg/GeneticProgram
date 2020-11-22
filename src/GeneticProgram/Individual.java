package GeneticProgram;

//! Individual Class
/*!
This class represents an Individual in the population.
*/
public class Individual { 
    private If root;/*!< A pointer to the root of the parse-tree */
     
    //! A copy constructor
    /*!
    @param i - an individual to copy.
    */
    public Individual(Individual i){
        if(i != null){
            this.root = new If(i.GetRoot());    
        }
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
