package GeneticProgram;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Data {
    
    private static String[] attributes;
    private static String className;
    public static int classPosition;
    private static HashMap<String, Integer> attributePosition;
    private static HashMap<Integer, String> positionAttribute;
    private static ArrayList<Double>[] data_list; 
    private static double[][] data_array; 
    //private static double[][] attributeMinMax;
    private static int dataSize; 
    private static Random random;
    private static int numClasses;
    
    public static void SetRandomSeed(long seed){
        random  = new Random(seed);
    }
    
    public static void SetNumberClasses(int numClasses){
        Data.numClasses = numClasses;
    }
    public static int GetNumberClasses(){
        return Data.numClasses;
    } 
    
    public static int GetNumberAttributes(){
        return attributes.length;
    }
    
    public static int GetDataSize(){
        return dataSize;
    }
    
    public static String GetAttribute(int position){
        return attributes[position];
    }
    
    public static int GetPosition(String attribute){
        return Data.attributePosition.get(attribute);
    }
    
    public static void ReadData(String filename, String classname){
        Data.className          = classname;
        Data.random             = new Random(42069);
        String line             = "";
        String splitBy          = ",";
        String [] items;
        try{
            BufferedReader br       = new BufferedReader(new FileReader(filename));
            line                    = br.readLine();
            items                   = line.split(splitBy);
            Data.attributes         = new String[items.length-1];
            Data.data_list          = new ArrayList[Data.attributes.length];
            Data.attributePosition  = new HashMap<>();
            Data.positionAttribute  = new HashMap<>();
            //Data.attributeMinMax    = new double[Data.attributes.length][2];
            for (int i = 0; i < Data.attributes.length; i++) {
                Data.attributes[i]          = items[i+1]; 
                Data.attributePosition.put(Data.attributes[i], i);
                Data.positionAttribute.put(i,Data.attributes[i]);
                Data.data_list[i]           = new ArrayList<>();
                /*
                Data.attributeMinMax[i][0]  = Integer.MAX_VALUE; 
                Data.attributeMinMax[i][1]  = Integer.MIN_VALUE; 
                */
            } 
            int row                 = 0;
            while((line = br.readLine()) != null){
                items   = line.split(splitBy);
                for (int i = 0; i < Data.attributes.length; i++) {
                    Data.data_list[i].add(Double.parseDouble(items[i+1]));
                    /*
                    if(data_list[i].get(row) > Data.attributeMinMax[i][1]){
                        Data.attributeMinMax[i][1] = data_list[i].get(row);
                    }
                    if(data_list[i].get(row) < Data.attributeMinMax[i][0]){
                        Data.attributeMinMax[i][0] = data_list[i].get(row);
                    }
                    */
                }
                ++row;
            }
            Data.dataSize           = row;
            Data.data_array         = new double[dataSize][attributes.length];
            for (int i = 0; i < Data.dataSize; i++) {
                for (int j = 0; j < attributes.length; j++) {
                    Data.data_array[i][j] = Data.data_list[j].get(i);
                }
            }
            Data.data_list          = null;
        }catch(IOException e){
            System.err.println("Error: "+e.getMessage());
        }
    }
    
    public static String[] GetAttributes(){
        return Data.attributes;
    }
    
    public static String GetClass(){
        return Data.className;
    }
    
    /*
    public static double[] GetMinMax(String attribute){
        return Data.attributeMinMax[Data.attributePosition.get(attribute)];
    }
    */
    
    public static double[][] GetData(){
        return Data.data_array;
    }
    
    public static String ToString() {
        return Arrays.toString(Data.attributes);
    }
     
    
    public static double GetPercentage(){
        return Data.random.nextDouble();
    } 
    
    public static int GetRandomIntInclusive(double[] minmax){
        int min         = (int)Math.floor(minmax[0]); 
        int max         = (int)Math.floor( minmax[1]); 
        return (int)Math.floor(Data.random.nextInt(max-min+1))+min;
    }
    
    public static int GetRandomIntExclusive(double[] minmax){
        int min         = (int)Math.floor(minmax[0]); 
        int max         = (int)Math.floor( minmax[1]);
        return (int)Math.floor(Data.random.nextInt(max-min))+min;
    }
    
    public static int GetRandomIntInclusive(int min, int max){
        return (int)Math.floor(Data.random.nextInt(max-min+1))+min;
    }
    
    public static int GetRandomIntExclusive(int min, int max){
        return (int)Math.floor(Data.random.nextInt(max-min))+min;
    } 
}
