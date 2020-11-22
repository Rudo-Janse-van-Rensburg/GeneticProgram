package GeneticProgram;
 
import java.util.ArrayList;
//!Generation class
/*!
This class holds the population of individuals within a generation.
*/
public class Generation {
    private final int           populationSize;                     /*!< The maximum population size */
    private int                 occupancy;                          /*!< The number of individuals already in the population */
    private final Individual[]  population;                         /*!< The individuals within the population */
    private final double[]      fitness;                            /*!< The Fitnesses of the individuals within the population */
    private double bestFitness;                                     /*!< The best encountered fitness */
    private Individual bestIndividual;                              /*!< The individual corresponding to the best fitness */
    
    //! A constructor
    /*!
    @param populationSize - the maximum population size
    */
    public Generation(int populationSize){
        this.populationSize = populationSize;
        this.population     = new Individual[this.populationSize];
        this.fitness        = new double[this.populationSize];
        this.Clear();
    }
    
    public int GetPopulationSize(){
        return this.populationSize;
    }
    
    /*!
    Clear and reset the generation
    */
    public void Clear(){
        this.bestFitness    = 0;
        this.bestIndividual = null;
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
        if(this.occupancy < this.populationSize){
            if(fitness > bestFitness){
                bestFitness     = fitness;
                bestIndividual  = individual;
            }
            this.population[this.occupancy] = individual;
            this.fitness[this.occupancy]    = fitness;
            this.occupancy++;
        }
    }
    
    public Individual GetFittestIndividual(){
        return bestIndividual;
    }
    
    public double GetFittestScore(){ 
        return bestFitness;
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
