package GeneticProgram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//!Evolution class
/*!
This class oversees and manages the evolutionary processes. 
*/
public class Evolution {
    private final Data          data;                   /*!< A pointer to the data */
    private Individual          bestIndividual;         /*!< A pointer to the best individual encountered */
    private final Generation    currentGeneration;      /*!< Holds the current generation of individuals */
    private final Generation    nextGeneration;         /*!< Holds the next generation of individual */
    private final int           convergenceThreshold;   /*!< The number of generations permitted to have the same "best fitness" */
    private final int           tournamentSize;         /*!< The size of the tournament */
    private final int           maxGeneration;          /*!< The maximum number of generations */
    private final double        applicationRate;        /*!< The rate at which mutation is applied */
    
    //! A constructor
    /*!
    @param data - a pointer to the data object
    @param populationSize - the size of the population
    @param convergenceThreshold - the number of generations that may have the same best fitness before the search can be regarded as converged
    @param tournamentSize -the size of the tournament
    @param maxGeneration - the maximum number of generations
    @param applicationRate - the rate at which mutation is applied
    */
    public Evolution(Data data, int populationSize, int convergenceThreshold, int tournamentSize, int maxGeneration, double applicationRate) {
        this.data                   = data;
        this.convergenceThreshold   = convergenceThreshold;
        this.currentGeneration      = new Generation(populationSize);
        this.nextGeneration         = new Generation(populationSize);
        this.tournamentSize         = tournamentSize;
        this.applicationRate        = applicationRate;
        this.maxGeneration          = maxGeneration;
    }
    
    
    //! Search Method
    /*!
    Starts the genetic program evolutionary process. It produces a Individual, which is the Individual with the best Fitness that was 
    encountered in the search.
    */
    public Individual Start(){
        this.bestIndividual         = null;
        int numberEqualGenerations  = 0;
        double bestFitness          = 0;
        double lastFitness          = 0;
        double currentFitness       = 0;
        /*Reset */
        int currGeneration          = 0;
        currentGeneration.Clear();
        nextGeneration.Clear();
        
        /*1.) Create initial population.*/
        System.out.println("1.) INITIAL POPULATION");
        InitialiseGeneration(currentGeneration);
        //Print(currGeneration, currentGeneration);
        while(numberEqualGenerations < convergenceThreshold && currGeneration < maxGeneration){
            /*2.) Evolve generation.*/
            
            //System.out.print("\rGENERATION #"+(currGeneration+1)+" - Fitness :   "+currentGeneration.GetFittestScore());
            EvolveGeneration(currentGeneration,nextGeneration);
            lastFitness     = currGeneration;
            //currentFitness  = currentGeneration.GetIndividualFitness(0);
            currentFitness  = currentGeneration.GetFittestScore();
            if(currentFitness > bestFitness){
                bestIndividual  = currentGeneration.GetFittestIndividual();
                bestFitness = currentFitness;
                numberEqualGenerations = 0;
            }else if(currentFitness == bestFitness || currentFitness == lastFitness){
                ++numberEqualGenerations;
            }else{
                numberEqualGenerations = 0;
            }
            Print(currGeneration, numberEqualGenerations,currentGeneration);
            ++currGeneration;
            
        }
        return bestIndividual;
        //return currentGeneration.GetIndividual(0);
    } 
    
    
    
    private void EvolveGeneration(Generation current,Generation next){
        next.Clear();
        for (int i = 0; i < (int)Math.floor(next.GetPopulationSize()*applicationRate); i++) {
            Individual mutant  = new Individual(TournamentSelection(current));
            GeneticOperators.Mutate(mutant,this.data);
            next.Add(mutant, IndividualFactory.Fitness(mutant,this.data));
        }
        
        while(!next.Full()){
            Individual[] children   = GeneticOperators.Crossover(TournamentSelection(current), TournamentSelection(current),this.data);
            for (Individual children1 : children) {
                next.Add(children1, IndividualFactory.Fitness(children1,this.data));
            }
        }
        
        current.Clear(); 
        for (int i = 0; i < next.GetOccupancy(); i++) {  
            current.Add(next.GetIndividual(i), IndividualFactory.Fitness(next.GetIndividual(i),this.data));
        }
        next.Clear(); 
    }
    
    private void InitialiseGeneration(Generation g){
        Individual individual               = null;
        HashMap<String,Character> unique    = new HashMap();
        unique.clear();
        for (int i = 0; i < g.GetPopulationSize(); i++) {
            individual  = IndividualFactory.PopOneOut(this.data);
            while(unique.containsKey(individual.ToString())){
                individual  = IndividualFactory.PopOneOut(this.data);
            }
            g.Add(individual, IndividualFactory.Fitness(individual,this.data));
            unique.put(individual.ToString(), ' ');
        }
    }
    
    private Individual TournamentSelection(Generation g){
        //System.out.println("Getting Tournament");
        Generation tournament   = new Generation(tournamentSize);
        Individual candidate    = null;
        for (int i = 0; i < tournamentSize; i++) {
            candidate   = g.GetIndividual(this.data.GetRandomIntExclusive(0,g.GetPopulationSize()));
            tournament.Add(candidate,IndividualFactory.Fitness(candidate,this.data));
        }
        candidate   = tournament.GetFittestIndividual();
        tournament.Clear();
        return candidate;
    }
    
    
    public void Print(int generationNumber,int numberEqualGenerations,Generation g){
        System.out.println("=======================");
        System.out.println("GENERATION  :   "+generationNumber);
        
        System.out.println("FITNESSES   :   "+Arrays.toString(g.GetFitnesses()));
        //System.out.println("FITTEST     :   ");
        
        System.out.println("-----------------------");
        for (int i = 0; i < 1; i++) {
            System.out.println("Convergence :   ("+numberEqualGenerations+"/"+convergenceThreshold+")");
            System.out.println("Individual  :   ("+(i+1)+"/"+g.GetOccupancy()+")");
            //System.out.println(g.GetIndividual(i).ToString());
            System.out.println("FITNESS     :   "+g.GetFittestScore());
        }

        System.out.println("-----------------------");
        
        System.out.println("=======================");
    }
}
