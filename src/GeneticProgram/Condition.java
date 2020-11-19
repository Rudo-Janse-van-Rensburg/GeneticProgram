package GeneticProgram;

public abstract class Condition extends Primitive {

    private Condition lhs;
    private Condition rhs;
    private String operator;

    protected Condition(String type, String operator, Condition lhs, Condition rhs) {
        super(type);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    protected Condition(Condition c) {
        super(c.GetType());
        this.operator = c.operator;
        switch(c.GetType()){
            case "Logical":
                switch(c.GetOperator()){
                    case "NOT":
                        switch(c.rhs.GetType()){
                            case "Logical":
                                this.rhs    = new Logical(c.rhs);
                                break;
                            case "Relational":
                                this.rhs    = new Relational(c.rhs);
                                break;
                        }
                        break;
                    default:
                        switch(c.rhs.GetType()){
                            case "Logical":
                                this.rhs    = new Logical(c.rhs);
                                break;
                            case "Relational":
                                this.rhs    = new Relational(c.rhs);
                                break;
                        }
                        switch(c.lhs.GetType()){
                            case "Logical":
                                this.lhs    = new Logical(c.lhs);
                                break;
                            case "Relational":
                                this.lhs    = new Relational(c.lhs);
                                break;
                        }
                        break; 
                }
                break;
            case "Relational":
                switch(c.lhs.GetType()){
                    case "Arithmetic":
                        this.lhs    = new Arithmetic(c.lhs);
                        break;
                    case "Attribute":
                        this.lhs    = new Attribute(c.lhs);
                        break;
                }
                switch(c.rhs.GetType()){
                    case "Arithmetic":
                        this.rhs    = new Arithmetic(c.rhs);
                        break;
                    case "Attribute":
                        this.rhs    = new Attribute(c.rhs);
                        break;
                }
                break;
            case "Arithmetic":
                switch(c.lhs.GetType()){
                    case "Arithmetic":
                        this.lhs    = new Arithmetic(c.lhs);
                        break;
                    case "Attribute":
                        this.lhs    = new Attribute(c.lhs);
                        break;
                }
                switch(c.rhs.GetType()){
                    case "Arithmetic":
                        this.rhs    = new Arithmetic(c.rhs);
                        break;
                    case "Attribute":
                        this.rhs    = new Attribute(c.rhs);
                        break;
                }
                break;
            case "Attribute":
                this.lhs    = null;
                this.rhs    = null;
                break;
        } 
    }

    public void SetLeft(Condition left) {
        this.lhs = left;
    }

    public Condition GetLeft() {
        return this.lhs;
    }

    public void SetRight(Condition right) {
        this.rhs = right;
    }

    public Condition GetRight() {
        return this.rhs;
    }

    public String GetOperator() {
        return this.operator;
    }

    public void SetOperator(String operator) {
        this.operator = operator;
    }

    public String ToString() {
        String string = "";
        switch(this.GetType()){
            case "Logical":
                switch(this.GetOperator()){
                    case "NOT":
                        string  = "( "+operator+" "+rhs.ToString() + ")";
                        break;
                    default:
                        string  = "( "+(lhs != null ? lhs.ToString() : "")+" "+operator+" "+rhs.ToString()+" )";
                        break;
                }
                break;
            case "Relational":
                string  = "( "+lhs.ToString()+" "+operator+" "+rhs.ToString()+" )"; 
                break;
            case "Arithmetic":
                string  = "( "+lhs.ToString()+" "+operator+" "+rhs.ToString()+" )";
                break;
            case "Attribute":
                string  = operator;
                break;
            default:
                string  = "( "+lhs.ToString()+" "+operator+" "+rhs.ToString()+" )"; 
                break;
             
        }
        return string; 
        
    }

    public void Print() {
        if (this.lhs == null && this.rhs != null) {
            System.out.print("(" + operator + " " + rhs.ToString());
        } else {
            System.out.print("(" + lhs.ToString() + operator + rhs.ToString());
        }
    }
}
