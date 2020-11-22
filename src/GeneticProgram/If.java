package GeneticProgram;
//! If Class
/*!
This class reperesnts an If node in the If sub-branch,
for which it is an intermediate node.
*/
public class If extends Primitive{
    
    private If t; /*!< The If node that represents the action that should be taken should the condition evaluate true.*/
    private If f;/*!< The If node that represents the action that should be taken should the condition evaluate false. */
    private Condition condition;/*!< The Condition node that represents the condition of the If statement.*/
    private double cls;/*!< The class value, should this be a class node.*/
     
    //!A constructor
    /*!
    @param c - the condition 
    @param t - the If node for when the condition evaluates true
    @param f - the If node for when the condition evaluates false
    */
    public If(Condition c, If t, If f){
        super('I');
        this.condition  = c;
        this.t          = t;
        this.f          = f;
    } 
    
    protected If(char type,double cls){
        super(type);
        this.cls        = cls;
        this.t          = null;
        this.f          = null;
        this.condition  = null;
    }
    //!A copy constructor
    /*!
    @param p -An If node to copy
    */
    public If(If p){
        super(p.GetType());
        this.t          = null;
        this.f          = null;
        this.condition  = null;
        if(p.GetType() == 'I'){
            this.t          = new If(p.t);
            this.f          = new If(p.f);
            
            switch(p.GetCondition().GetType()){
                case 'L':
                    SetCondition(new Logical(p.GetCondition()));
                    break;
                case 'R':
                    SetCondition(new Relational(p.GetCondition()));
                    break;
                default:
                    this.condition = null;
            }
        }
        this.cls        = p.cls; 
    }
    
    public Condition GetCondition(){
        return this.condition;
    }
    
    public void SetCondition(Condition c){
        this.condition = c;
    }
    
    public If GetTrue(){
        return this.t;
    }
    
    public void SetTrue(If t){
        this.t  = t;
    }
    
    public If GetFalse(){
        return this.f;
    }
    
    public void SetFalse(If f){
        this.f  = f;
    }
    
    public double GetClass(){
        return this.cls;
    }
    
    public void SetClass(double cls){
        this.cls    = cls;
    }

    public String ToString(){
        return (
                (this.GetType() == 'C') 
                    ? this.cls + "" 
                    : "if ("+condition.ToString()+"){\n"+t.ToString()+"\n"+f.ToString()+"}"
                ); 
    }
    
}
