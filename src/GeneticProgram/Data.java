package GeneticProgram;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public final class Data {
    
    private String[] attributes;
    private String className;
    public int classPosition;
    private HashMap<String, Integer> attributePosition;
    private HashMap<Integer, String> positionAttribute;
    private ArrayList<Double>[] data_list; 
    private double[][] data_array; 
    private int dataSize; 
    private Random random;
    private int numClasses;
    
    public Data(long seed, int numClasses, String filename, String classname){
        this.random     = new Random(seed);
        this.numClasses = numClasses;
        this.ReadData(filename, classname);
    }
    
    public void ReadData(String filename, String classname){
        this.className          = classname;
        this.random             = new Random(42069);
        String line             = "";
        String splitBy          = ",";
        String [] items;
        try{
            BufferedReader br       = new BufferedReader(new FileReader(filename));
            line                    = br.readLine();
            items                   = line.split(splitBy);
            this.attributes         = new String[items.length-1];
            this.data_list          = new ArrayList[this.attributes.length];
            this.attributePosition  = new HashMap<>();
            this.positionAttribute  = new HashMap<>();
            for (int i = 0; i < this.attributes.length; i++) {
                this.attributes[i]          = items[i+1]; 
                this.attributePosition.put(this.attributes[i], i);
                this.positionAttribute.put(i,this.attributes[i]);
                this.data_list[i]           = new ArrayList<>(); 
            } 
            int row                 = 0;
            while((line = br.readLine()) != null){
                items   = line.split(splitBy);
                for (int i = 0; i < this.attributes.length; i++) {
                    this.data_list[i].add(Double.parseDouble(items[i+1])); 
                }
                ++row;
            }
            this.dataSize           = row;
            this.data_array         = new double[dataSize][attributes.length];
            for (int i = 0; i < this.dataSize; i++) {
                for (int j = 0; j < attributes.length; j++) {
                    this.data_array[i][j] = this.data_list[j].get(i);
                }
            }
            this.data_list          = null;
        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
        }
    }
        public void SetRandomSeed(long seed){
        this.random  = new Random(seed);
    }
    
    public void SetNumberClasses(int numClasses){
        this.numClasses = numClasses;
    }
    
    public int GetNumberClasses(){
        return this.numClasses;
    } 
    
    public int GetNumberAttributes(){
        return this.attributes.length;
    }
    
    public int GetDataSize(){
        return this.dataSize;
    }
    
    public String GetAttribute(int position){
        return attributes[position];
    }
    
    public int GetPosition(String attribute){
        return this.attributePosition.get(attribute);
    }
    
    public String[] GetAttributes(){
        return this.attributes;
    }
    
    public String GetClass(){
        return this.className;
    }
     
    public double[][] GetData(){
        return this.data_array;
    }
    
    public String ToString() {
        return Arrays.toString(this.attributes);
    }
     
    
    public double GetPercentage(){
        return this.random.nextDouble();
    } 
    
    public int GetRandomIntInclusive(double[] minmax){
        int min         = (int)Math.floor(minmax[0]); 
        int max         = (int)Math.floor( minmax[1]); 
        return (int)Math.floor(this.random.nextInt(max-min+1))+min;
    }
    
    public int GetRandomIntExclusive(double[] minmax){
        int min         = (int)Math.floor(minmax[0]); 
        int max         = (int)Math.floor( minmax[1]);
        return (int)Math.floor(this.random.nextInt(max-min))+min;
    }
    
    public int GetRandomIntInclusive(int min, int max){
        return (int)Math.floor(this.random.nextInt(max-min+1))+min;
    }
    
    public int GetRandomIntExclusive(int min, int max){
        return (int)Math.floor(this.random.nextInt(max-min))+min;
    } 
}
