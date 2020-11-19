package GeneticProgram;

public class If extends Primitive{
    
    private If t;
    private If f;
    private Condition condition;
    private double cls;
     
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
        /*
        if(p.GetType().equals("If")){
            this.t          = (p.GetType().equals("If")) ? new If(p.t) : null;
            this.f          = (p.GetType().equals("If")) ? new If(p.f) : null;
        
            switch(p.GetCondition().GetType()){
                case "Logical":
                    SetCondition(new Logical(p.GetCondition()));
                    break;
                case "Relational":
                    SetCondition(new Relational(p.GetCondition()));
                    break;
                default:
                    this.condition  = null;
            }
        }

        this.cls        = p.cls;
        */
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
        /*
        return (this.GetType().equals("Class")) 
                ? this.cls + "" 
                : "if ("+condition.ToString()+"){\n"+t.ToString()+"\n"+f.ToString()+"}";
        */
    }
    
}
