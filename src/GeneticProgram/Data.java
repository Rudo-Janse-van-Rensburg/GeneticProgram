package GeneticProgram;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public final class Data {
    
    private char classSymbol;
    private char[] symbolsWithoutClass;
    private HashMap<String, Character> hmAttributeSymbol;
    private HashMap<Character, Integer> hmSymbolPosition;
    private ArrayList<Double>[] data_list; 
    private double[][] data_array; 
    private int dataSize; 
    private Random random;
    private int numClasses;
    private int numberAttributes;
    
    public Data(long seed, int numClasses, String filename, String classname){
        this.random     = new Random(seed);
        this.numClasses = numClasses;
        this.ReadData(filename, classname);
    }
    
    public void ReadData(String filename, String classname){
        /*
            Remove unwanted attributes effectively
            
        */
        this.random                     = new Random(42069);
        String line                     = "";
        String splitBy                  = ",";
        String [] items;
        try{
            BufferedReader br                   = new BufferedReader(new FileReader(filename));
            line                                = br.readLine();
            items                               = line.split(splitBy);
            this.numberAttributes               = items.length;
            this.symbolsWithoutClass            = new char[numberAttributes-1];
            this.hmAttributeSymbol              = new HashMap<>();
            this.hmSymbolPosition               = new HashMap<>();
            this.data_list                      = new ArrayList[this.numberAttributes]; 
            int position                        = 0;
            for (int i = 0; i < this.GetNumberAttributes(); i++) {
                this.hmAttributeSymbol.put(items[i], (char)('A'+i));
                this.hmSymbolPosition.put(hmAttributeSymbol.get(items[i]), i);  
                this.data_list[i]           = new ArrayList<>(); 
                if(!items[i].equals(classname)){
                    this.symbolsWithoutClass[position]  =   this.hmAttributeSymbol.get(items[i]); 
                    ++position;
                }else{
                    this.classSymbol    = hmAttributeSymbol.get(classname);
                }
            } 
            int row                     = 0;
            while((line = br.readLine()) != null){
                items   = line.split(splitBy);
                for (int i = 0; i < this.numberAttributes; i++) {
                    this.data_list[i].add(Double.parseDouble(items[i])); 
                }
                ++row;
            }
            this.dataSize           = row;
            this.data_array         = new double[dataSize][this.numberAttributes];
            for (int i = 0; i < this.dataSize; i++) {
                for (int j = 0; j < this.numberAttributes; j++) {
                    this.data_array[i][j] = this.data_list[j].get(i);
                }
            }
            this.data_list          = null;
        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
        }
    }

    public int GetNumberClasses(){
        return this.numClasses;
    } 
    
    public int GetNumberAttributes(){
        return this.numberAttributes;
    }
    
    public int GetDataSize(){
        return this.dataSize;
    } 
    
    public int GetPosition(char symbol){
        return this.hmSymbolPosition.get(symbol);
    }
    
    public char[] GetSymbolsWithoutClass(){
        return this.symbolsWithoutClass;
    } 
    
    public char GetClassSymbol(){
        return this.classSymbol;
    }
     
    public double[][] GetData(){
        return this.data_array;
    } 
    
    public double GetPercentage(){
        return this.random.nextDouble();
    } 
 
    public int GetRandomIntInclusive(int min, int max){
        return (int)Math.floor(this.random.nextInt(max-min+1))+min;
    }
    
    public int GetRandomIntExclusive(int min, int max){
        return (int)Math.floor(this.random.nextInt(max-min))+min;
    } 
}
