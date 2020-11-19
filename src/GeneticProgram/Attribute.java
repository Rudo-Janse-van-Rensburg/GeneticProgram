/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneticProgram;
 
public class Attribute extends Arithmetic {
    
    /**
     * @param attribute_name - an attribute 
     */
    public Attribute(char attribute_name){
        super('a',attribute_name,null,null); 
    } 
    
    public Attribute(Condition c){
        super(c);
    } 
}
