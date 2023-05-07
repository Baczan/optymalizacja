package genetic.alg;

import genetic.data.Chromosome;
import genetic.data.Population;

import java.util.*;

public class GeneticSelection {

    private final Random random = new Random();
    private List<Integer> cumulativeScore = new ArrayList<>();
    private int score = 0;


    public void prepareForRandomSelection(Population population) {
        cumulativeScore = new ArrayList<>();
        score = 0;

        Chromosome lastChromosome = population.getChromosomes().get(population.getChromosomes().size() - 1);

        for (Chromosome chromosome : population.getChromosomes()) {
            int chromosomeScore = lastChromosome.getFitness() - chromosome.getFitness();
            score += chromosomeScore;
            cumulativeScore.add(score);
        }
    }

    public Chromosome selectRandom(Population population) {
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
