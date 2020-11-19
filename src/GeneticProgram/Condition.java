package GeneticProgram;

public abstract class Condition extends Primitive {

    private Condition lhs;
    private Condition rhs;
    private char operator;

    protected Condition(char type, char operator, Condition lhs, Condition rhs) {
        super(type);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    protected Condition(Condition c) {
        super(c.GetType());
        this.operator = c.operator;
        switch(c.GetType()){
            case 'L':
                switch(c.GetOperator()){
                    case '!':
                        switch(c.rhs.GetType()){
                            case 'L':
                                this.rhs    = new Logical(c.rhs);
                                break;
                            case 'R':
                                this.rhs    = new Relational(c.rhs);
                                break;
                        }
                        break;
                    default:
                        switch(c.rhs.GetType()){
                            case 'L':
                                this.rhs    = new Logical(c.rhs);
                                break;
                            case 'R':
                                this.rhs    = new Relational(c.rhs);
                                break;
                        }
                        switch(c.lhs.GetType()){
                            case 'L':
                                this.lhs    = new Logical(c.lhs);
                                break;
                            case 'R':
                                this.lhs    = new Relational(c.lhs);
                                break;
                        }
                        break; 
                }
                break;
            case 'R':
                switch(c.lhs.GetType()){
                    case 'A':
                        this.lhs    = new Arithmetic(c.lhs);
                        break;
                    case 'a':
                        this.lhs    = new Attribute(c.lhs);
                        break;
                }
                switch(c.rhs.GetType()){
                    case 'A':
                        this.rhs    = new Arithmetic(c.rhs);
                        break;
                    case 'a':
                        this.rhs    = new Attribute(c.rhs);
                        break;
                }
                break;
            case 'A':
                switch(c.lhs.GetType()){
                    case 'A':
                        this.lhs    = new Arithmetic(c.lhs);
                        break;
                    case 'a':
                        this.lhs    = new Attribute(c.lhs);
                        break;
                }
                switch(c.rhs.GetType()){
                    case 'A':
                        this.rhs    = new Arithmetic(c.rhs);
                        break;
                    case 'a':
                        this.rhs    = new Attribute(c.rhs);
                        break;
                }
                break;
            case 'a':
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

    public char GetOperator() {
        return this.operator;
    }

    public void SetOperator(char operator) {
        this.operator = operator;
    }

    public String ToString() {
        String string = "";
        switch(this.GetType()){
            case 'L':
                switch(this.GetOperator()){
                    case '!':
                        string  = "( "+operator+" "+rhs.ToString() + ")";
                        break;
                    default:
                        string  = "( "+(lhs != null ? lhs.ToString() : "")+" "+operator+" "+rhs.ToString()+" )";
                        break;
                }
                break;
            case 'R':
                string  = "( "+lhs.ToString()+" "+operator+" "+rhs.ToString()+" )"; 
                break;
            case 'A':
                string  = "( "+lhs.ToString()+" "+operator+" "+rhs.ToString()+" )";
                break;
            case 'a':
                string  = operator+ "";
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
