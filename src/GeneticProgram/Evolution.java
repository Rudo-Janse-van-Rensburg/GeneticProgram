package GeneticProgram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Evolution {
    private Individual          bestIndividual;
    private final Generation    currentGeneration;
    private final Generation    nextGeneration;
    private final int           convergenceThreshold;
    private final int           tournamentSize;
    private final int           maxGeneration;
    private final double        applicationRate;
    public Evolution(int populationSize, int convergenceThreshold, int tournamentSize, int maxGeneration, double applicationRate) {
        this.convergenceThreshold   = convergenceThreshold;
        this.currentGeneration      = new Generation(populationSize);
        this.nextGeneration         = new Generation(populationSize);
        this.tournamentSize         = tournamentSize;
        this.applicationRate        = applicationRate;
        this.maxGeneration          = maxGeneration;
    }
    
    public Individual Start(){
        this.bestIndividual         = null;
        int numberEqualGenerations  = 0;
        double bestFitness          = 0;
        double lastFitness          = 0;
        double currentFitness       = 0;
        /*Reset */
        int currGeneration          = 0;
        //hall_of_fame.clear();
        currentGeneration.Clear();
        nextGeneration.Clear();
        
        /*1.) Create initial population.*/
        System.out.println("1.) INITIAL POPULATION");
        InitialiseGeneration(currentGeneration);
        //Print(currGeneration, currentGeneration);
        while(numberEqualGenerations < convergenceThreshold && currGeneration < maxGeneration){
            /*2.) Evolve generation.*/
            
            System.out.print("\rGENERATION #"+(currGeneration+1)+" - Fitness :   "+currentGeneration.GetIndividualFitness(0));
            EvolveGeneration(currentGeneration,nextGeneration);
            lastFitness     = currGeneration;
            currentFitness  = currentGeneration.GetIndividualFitness(0);
            if(currentFitness > bestFitness){
                bestIndividual  = currentGeneration.GetIndividual(0);
                bestFitness = currentFitness;
                numberEqualGenerations = 0;
            }else if(currentFitness == bestFitness || currentFitness == lastFitness){
                ++numberEqualGenerations;
            }else{
                numberEqualGenerations = 0;
            }
            //Print(currGeneration, numberEqualGenerations,currentGeneration);
            ++currGeneration;
            
        }
        return bestIndividual;
        //return currentGeneration.GetIndividual(0);
    } 
    
    
    
    private void EvolveGeneration(Generation current,Generation next){
        next.Clear();
        for (int i = 0; i < (int)Math.floor(next.GetPopulationSize()*applicationRate); i++) {
            Individual mutant  = new Individual(TournamentSelection(current));
            GeneticOperators.Mutate(mutant);
            next.Add(mutant, IndividualFactory.Fitness(mutant));
        }
        
        while(!next.Full()){
            Individual[] children   = GeneticOperators.Crossover(TournamentSelection(current), TournamentSelection(current));
            for (Individual children1 : children) {
                next.Add(children1, IndividualFactory.Fitness(children1));
            }
        }
        
        current.Clear(); 
        for (int i = 0; i < next.GetOccupancy(); i++) {  
            current.Add(next.GetIndividual(i), IndividualFactory.Fitness(next.GetIndividual(i)));
        }
        next.Clear();
        
        /*
        next.Clear();
        while(!next.Full()){
            //System.out.println(next.GetOccupancy()+"/"+next.GetPopulationSize());
            if(Data.GetPercentage() > mutationChance){
                //System.out.println("CROSSOVER");
                Individual[] children   = GeneticOperators.Crossover(TournamentSelection(current), TournamentSelection(current));
                for (int i = 0; i < children.length; i++) {
                    next.Add(children[i], IndividualFactory.Fitness(children[i]));
                }
            }else{
                //System.out.println("MUTATION");
                Individual mutant       = new Individual(TournamentSelection(current));
                GeneticOperators.Mutate(mutant);
                next.Add(mutant, IndividualFactory.Fitness(mutant));
            }
        }
        current.Clear();
        //current.Clear();
        //Print(0, next);
        for (int i = 0; i < next.GetOccupancy(); i++) {  
            current.Add(next.GetIndividual(i), IndividualFactory.Fitness(next.GetIndividual(i)));
        }
        next.Clear();*/
    }
    
    private void InitialiseGeneration(Generation g){
        Individual individual               = null;
        HashMap<String,Character> unique    = new HashMap();
        unique.clear();
        for (int i = 0; i < g.GetPopulationSize(); i++) {
            individual  = IndividualFactory.PopOneOut();
            while(unique.containsKey(individual.ToString())){
                individual  = IndividualFactory.PopOneOut();
            }
            g.Add(individual, IndividualFactory.Fitness(individual));
            unique.put(individual.ToString(), ' ');
        }
    }
    
    private Individual TournamentSelection(Generation g){
        //System.out.println("Getting Tournament");
        Generation tournament   = new Generation(tournamentSize);
        Individual candidate    = null;
        for (int i = 0; i < tournamentSize; i++) {
            candidate   = g.GetIndividual(Data.GetRandomIntExclusive(0,g.GetPopulationSize()));
            tournament.Add(candidate,IndividualFactory.Fitness(candidate));
        }
        candidate   = tournament.GetIndividual(0);
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
            System.out.println("FITNESS     :   "+g.GetIndividualFitness(i));
        }

        System.out.println("-----------------------");
        
        System.out.println("=======================");
    }
}
