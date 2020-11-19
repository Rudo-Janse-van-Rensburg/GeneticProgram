package GeneticProgram;
 
import java.util.ArrayList;

public class Generation {
    
    private final int           populationSize;
    private int                 occupancy;
    private final Individual[]  population;
    private final double[]      fitness;
   
    
    public Generation(int populationSize){

        this.populationSize = populationSize;
        this.population     = new Individual[this.populationSize];
        this.fitness        = new double[this.populationSize];
        this.Clear();
    }
    
    public int GetPopulationSize(){
        return this.populationSize;
    }
    
    public void Clear(){
        for (int i = 0; i < this.populationSize; i++) {
            this.fitness[i]     = 0;
            this.population[i]  = null;
        }
        this.occupancy      = 0;
    }

    
    public boolean Full(){
        return (this.occupancy == this.populationSize);
    }
    
    public boolean Empty(){
        return occupancy == 0;
    }
    
    public int GetOccupancy(){
        return this.occupancy;
    }
    
    public void Add(Individual individual, double fitness){

        if(occupancy < this.populationSize){
            int pos = 0;
            while(pos < this.occupancy && fitness <= this.fitness[pos]){
                ++pos;
            }
            for (int i = occupancy; i > pos; i--) {
                this.fitness[i]     = this.fitness[i-1];
                this.population[i]  = this.population[i-1];
            }
            this.population[pos]    = individual;
            this.fitness[pos]       = fitness;
            ++occupancy;
        }
        /*
        int pos = 0;
        while(pos < this.populationSize && fitness <= this.fitness[pos]){
            ++pos; 
        }
        if(pos < this.populationSize){
            for (int i = populationSize-1; i > pos; i--) {
                this.fitness[i]     = this.fitness[i-1];
                this.population[i]  = this.population[i-1];
            }
            this.population[pos]    = individual;
            this.fitness[pos]       = fitness;
        }
        
        
        if(occupancy < populationSize)
            ++occupancy;
        */
    }
    
    public Individual GetIndividual(int position){
        if(position >= 0 && position < occupancy){
            return this.population[position];
        }
        return null;
    }
    
    public double GetIndividualFitness(int position){
        if(position >= 0 && position < occupancy){
            return this.fitness[position];
        }
        return 0;
    }
    
    public double[] GetFitnesses(){
        return this.fitness;
    }
    
    public Individual[] GetTournament(int poolSize,Data dataObj){
        Individual[] tournament = new Individual[poolSize];
        for (int i = 0; i < poolSize; i++){
            tournament[i]           = this.population[dataObj.GetRandomIntExclusive(0,occupancy)];
        }
        return tournament;
    }
     
    
    
}
