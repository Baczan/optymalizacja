package genetic.alg;

import genetic.data.Chromosome;
import genetic.data.Genom;
import genetic.data.Population;
import greedy.Process;
import taskInstance.TaskInstance;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Genetic {

    //Services
    private final GeneticInitialPopulationGenerator generateInitialPopulation = new GeneticInitialPopulationGenerator();
    private final GeneticSelection geneticSelection = new GeneticSelection();

    //Fields
    private final TaskInstance taskInstance;

    //Simulation control
    private final int populationSize;
    private float mutationPercentage;
    private final float mutationPercentageProgress;
    private final float elitePercentage;

    //Simulation duration
    private final int duration;
    private final ChronoUnit unit;

    //Loging
    private Chromosome bestResult;
    private float progressPercentage = 0f;
    float loggedProgress = 0.0f;

    boolean loggingEnabled;


    public Genetic(TaskInstance taskInstance, int populationSize, float mutationPercentage, float mutationPercentageProgress, float elitePercentage, int duration, ChronoUnit timeUnit, boolean loggingEnabled) {
        this.taskInstance = taskInstance;
        this.populationSize = populationSize;
        this.mutationPercentage = mutationPercentage;
        this.mutationPercentageProgress = mutationPercentageProgress;
        this.elitePercentage = elitePercentage;
        this.duration = duration;
        this.unit = timeUnit;
        this.loggingEnabled = loggingEnabled;
    }

    public List<Process> solve() {
        Population population = generateInitialPopulation.generateInitialPopulationRandom(taskInstance, populationSize);
        //Population population = generateInitialPopulation.generateFromLpt(taskInstance, populationSize);
        population = applySteps(population);

        int populationNumber = 0;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + TimeUnit.of(unit).toMillis(duration)) {
            progressPercentage = (System.currentTimeMillis() - startTime) / (float) TimeUnit.of(unit).toMillis(duration);
            populationNumber++;
            population = applySteps(population);

            if (this.loggedProgress + 0.05f < progressPercentage && loggingEnabled) {
                this.loggedProgress += 0.05f;
                System.out.println("Instance: " + taskInstance.getInstanceName() + ", Progress: " + this.loggedProgress + ", Population " + (populationNumber) + ", Best result: " + bestResult.getFitness());
            }
        }

        calculateFitness(population);
        Chromosome chromosome = population.getChromosomes().get(0);

        Map<Integer, List<Genom>> assignedTask = chromosome.getGenoms().stream().collect(Collectors.groupingBy(Genom::getProcess));
        List<Process> processes = assignedTask.entrySet().stream().map(integerListEntry -> {
            Process process = new Process(integerListEntry.getKey());
            List<Integer> tasks = integerListEntry.getValue().stream().map(Genom::getExecutionTime).toList();
            process.setAssignedTasks(tasks);

            return process;
        }).collect(Collectors.toList());

        processes.sort(Comparator.comparingInt(Process::getProcessNumber));
        return processes;
    }

    private Population applySteps(Population population) {

        //Obliczenie jakości rozwiązania
        calculateFitness(population);

        //Zapisz najlepsze rozwiazanie żeby móc je zalogować w konsoli
        bestResult = population.getChromosomes().get(0);

        //Wygenerowanie nowej generacji na podstawie starej
        population = selection(population);

        //mutacja aby uzyskać nowe rozwiązania
        mutate(population);

        return population;
    }

    private void calculateFitness(Population population) {
        population.getChromosomes().stream().parallel().forEach(Chromosome::updateFitness);
        population.getChromosomes().sort(Comparator.comparingInt(Chromosome::getFitness));
    }

    private Population selection(Population population) {
        Population newPopulation = new Population();

        //Copy elite chromosomes
        int eliteNumber = Math.round(populationSize * elitePercentage);
        for (int i = 0; i < eliteNumber; i++) {
            Chromosome newChromosome = new Chromosome();
            List<Genom> newGenomes = new ArrayList<>();

            Chromosome chromosome = population.getChromosomes().get(i);
            for (Genom genome : chromosome.getGenoms()) {
                newGenomes.add(new Genom(genome.getExecutionTime(), genome.getProcess()));
            }
            newChromosome.setGenoms(newGenomes);

            newPopulation.getChromosomes().add(newChromosome);
        }


        geneticSelection.prepareForRandomSelection(population);
        List<Chromosome> crossedChromosomes = IntStream.range(0, populationSize - eliteNumber).parallel().mapToObj(value -> {
            Random random = new Random();
            Chromosome parent1 = geneticSelection.selectRandom(population, random);
            Chromosome parent2 = geneticSelection.selectRandom(population, random);

            return cross(parent1, parent2, random);
        }).collect(Collectors.toList());
        newPopulation.getChromosomes().addAll(crossedChromosomes);

        return newPopulation;
    }

    private Chromosome cross(Chromosome parent1, Chromosome parent2, Random random) {

        List<Genom> genomes = new ArrayList<>();
        for (int i = 0; i < parent1.getGenoms().size(); i++) {
            int randomNumber = random.nextInt(0, 2);

            Genom baseGenom;
            if (randomNumber == 0) {
                baseGenom = parent1.getGenoms().get(i);
            } else if (randomNumber == 1) {
                baseGenom = parent2.getGenoms().get(i);
            } else {
                throw new RuntimeException("");
            }

            genomes.add(new Genom(baseGenom.getExecutionTime(), baseGenom.getProcess()));
        }

        return new Chromosome(genomes);
    }

    private void mutate(Population population) {
        int eliteNumber = Math.round(populationSize * elitePercentage);

        List<Chromosome> elite = population.getChromosomes().subList(eliteNumber, population.getChromosomes().size());
        elite.stream().parallel().forEach(chromosome -> {
            Random random1 = new Random();
            int processNumber = taskInstance.getProcessNumber();
            int mutationRate = calculateMutationRate();
            for (Genom genom : chromosome.getGenoms()) {
                if (mutationRate == 0 || random1.nextInt(0, mutationRate) == 0) {
                    genom.setProcess(random1.nextInt(0, processNumber));
                }
            }
        });
    }

    private int calculateMutationRate() {
        float finalMutationPercentage = (mutationPercentage + progressPercentage * mutationPercentageProgress);
        int mutationRate = Math.round(1 / finalMutationPercentage);

        if (mutationRate < 0) {
            mutationRate = 0;
        }

        return mutationRate;
    }
}
