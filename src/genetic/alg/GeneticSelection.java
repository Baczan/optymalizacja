package genetic.alg;

import genetic.data.Chromosome;
import genetic.data.Population;

import java.util.*;

public class GeneticSelection {
    private List<Integer> cumulativeScore = new ArrayList<>();
    private int score = 0;


    public void prepareForRandomSelection(Population population, float progress) {
        cumulativeScore = new ArrayList<>();
        score = 0;

        float progressScaler = 1 - (progress * 2);
        if(progressScaler < 0){
            progressScaler = 0;
        }

/*        float progressScaler = 1;*/

        Chromosome lastChromosome = population.getChromosomes().get(population.getChromosomes().size() - 1);
        for (Chromosome chromosome : population.getChromosomes()) {
            int chromosomeScore = Math.round((lastChromosome.getFitness() - chromosome.getFitness()) * progressScaler) + 1;
            //int chromosomeScore = chromosome.getFitness();
            score += chromosomeScore;
            cumulativeScore.add(score);
        }
    }

    public Chromosome selectRandom(Population population, Random random) {
        int randomNumber = random.nextInt(0, score + 1);

        for (int i = 0; i < cumulativeScore.size(); i++) {
            Integer chromosomeCumulativeScore = cumulativeScore.get(i);
            if (chromosomeCumulativeScore > randomNumber) {
                Chromosome chromosome = population.getChromosomes().get(i);
                chromosome.setSelectionCount(chromosome.getSelectionCount() + 1);
                return chromosome;
            }
        }

        return population.getChromosomes().get(population.getChromosomes().size() - 1);
    }
}
