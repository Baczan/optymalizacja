package genetic.alg;

import genetic.data.Chromosome;
import genetic.data.Genom;
import genetic.data.Population;
import taskInstance.TaskInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneticInitialPopulationGenerator {

    private final Random random = new Random();

    public Population generateInitialPopulationRandom(TaskInstance taskInstance, int size) {
        List<Chromosome> chromosomes = IntStream.range(0, size)
                .mapToObj(v -> generateChromosome(taskInstance))
                .collect(Collectors.toList());

        return new Population(chromosomes);
    }

    private Chromosome generateChromosome(TaskInstance taskInstance) {
        List<Genom> genomes = new ArrayList<>();

        for (Integer executionTime : taskInstance.getTasks()) {
            int process = random.nextInt(0, taskInstance.getProcessNumber());
            genomes.add(new Genom(executionTime, process));
        }

        return new Chromosome(genomes);
    }

}
