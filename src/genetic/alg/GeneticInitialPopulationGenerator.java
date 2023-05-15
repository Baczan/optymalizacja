package genetic.alg;

import genetic.data.Chromosome;
import genetic.data.Genom;
import genetic.data.Population;
import greedy.GreedySolver;
import greedy.Process;
import taskInstance.TaskInstance;

import java.util.ArrayList;
import java.util.Comparator;
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

    public Population generateFromLpt(TaskInstance taskInstance, int size) {
        List<Process> processes = GreedySolver.solveTaskInstance(taskInstance);

        List<Chromosome> chromosomes = IntStream.range(0, size)
                .mapToObj(v -> {

                    List<Genom> genomes = new ArrayList<>();

                    for (int i = 0; i < processes.size(); i++) {
                        Process process = processes.get(i);
                        for (int j = 0; j < process.getAssignedTasks().size(); j++) {
                            genomes.add(new Genom(process.getAssignedTasks().get(j), i));
                        }
                    }

                    return new Chromosome(genomes);
                })
                .collect(Collectors.toList());

        return new Population(chromosomes);
    }

    public Population half(TaskInstance taskInstance, int size){
        Population population1 = generateInitialPopulationRandom(taskInstance, size/2);
        Population population2 = generateFromLpt(taskInstance, size/2);

        List<Chromosome> a = new ArrayList<>();
        a.addAll(population1.getChromosomes());
        a.addAll(population2.getChromosomes());

        a.forEach(chromosome -> {
            chromosome.getGenoms().sort(Comparator.comparingInt(Genom::getExecutionTime));
        });

        return new Population(a);
    }
}
