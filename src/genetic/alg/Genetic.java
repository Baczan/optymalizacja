package genetic.alg;

import genetic.data.Chromosome;
import genetic.data.Genom;
import genetic.data.Population;
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
    private int populationSize = 10000;

    private int mutationRate = 0;

    private float finalMutationPercentage = 0.0f;
    private float mutationPercentage = 0.005f;

    private float mutationPercentageProgress = 0.01f;

    private float mutationPercentageStagnation = 0.02f;
    private float elitePercentage = 0.05f;

    private int mutX = 0;
    //Simulation duration
    private final int duration = 5;
    private final ChronoUnit unit = ChronoUnit.MINUTES;

    //Loging
    private Chromosome bestResult;
    private int bestResutFit = 1000000;
    private float stagnate = 0.0f;

    private float progressPercentage = 0f;

    public Genetic(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }

    public Chromosome solve(int pop, int muta) {
        populationSize = pop;
        mutX = muta;
        Population population = generateInitialPopulation.generateInitialPopulationRandom(taskInstance, populationSize);
        //Population population = generateInitialPopulation.generateFromLpt(taskInstance, populationSize);
        //Population population = generateInitialPopulation.half(taskInstance, populationSize);
        population = applySteps(population);

        //System.out.println("Start time: " + new Date() + "\n");

        int populationNumber = 0;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + TimeUnit.of(unit).toMillis(duration)) {
            progressPercentage = (System.currentTimeMillis() - startTime) / (float) TimeUnit.of(unit).toMillis(duration);
            populationNumber++;
            population = applySteps(population);

            //System.out.println("taskInstance.TaskInstance: " + taskInstance.getInstanceName() + " Population " + (populationNumber) + " best result: " + bestResult.getFitness());

            if (populationNumber % 1000 == 0) {
                System.out.println("taskInstance.TaskInstance: " + taskInstance.getInstanceName()+" Mutation: "+finalMutationPercentage + " Population " + (populationNumber) + " best result: " + bestResult.getFitness());
            }
        }

        /*System.out.println("\nEnd time: " + new Date());
        System.out.println("Best result: " + bestResult.getFitness());

        System.out.println("Pop: " + populationNumber);
        System.out.println("mut: " + mutationRate);*/

        calculateFitness(population);
        return population.getChromosomes().get(0);
    }

    private Population applySteps(Population population) {

        //Obliczenie jakości rozwiązania
        calculateFitness(population);

        //Zapisz najlepsze rozwiazanie żeby móc je zalogować w konsoli

/*        if (population.getChromosomes().get(0).getFitness() == bestResutFit) {
            stagnate += 0.00001f;

            if (stagnate > 1) {
                stagnate = 1.0f;
            }

        } else {
            stagnate = 0.0f;
        }*/

        bestResult = population.getChromosomes().get(0);
        bestResutFit = population.getChromosomes().get(0).getFitness();

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


        geneticSelection.prepareForRandomSelection(population,progressPercentage);
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
                throw new RuntimeException("sad");
            }

            genomes.add(new Genom(baseGenom.getExecutionTime(), baseGenom.getProcess()));
        }

        return new Chromosome(genomes);
    }

    private void mutate(Population population) {
        calculateMutationRate(population);
        int eliteNumber = Math.round(populationSize * elitePercentage);

        List<Chromosome> elite = population.getChromosomes().subList(eliteNumber, population.getChromosomes().size());
        elite.stream().parallel().forEach(chromosome -> {
            Random random1 = new Random();
            int processNumber = taskInstance.getProcessNumber();
            int mut = mutationRate;
            for (Genom genom : chromosome.getGenoms()) {
                if (mut == 0 || random1.nextInt(0, mut) == 0) {
                    genom.setProcess(random1.nextInt(0, processNumber));
                }
            }
        });

/*        for (int i = eliteNumber; i < population.getChromosomes().size(); i++) {
            Chromosome chromosome = population.getChromosomes().get(i);
            for (Genom genom : chromosome.getGenoms()) {
                if (random.nextInt(0, mutationRate) == 0) {
                    genom.setProcess(random.nextInt(0, taskInstance.getProcessNumber()));
                }
            }
        }*/
    }

    private void calculateMutationRate(Population population) {

        finalMutationPercentage = (mutationPercentage + progressPercentage * mutationPercentageProgress + stagnate * mutationPercentageStagnation);
        mutationRate = Math.round(1 / finalMutationPercentage);

        /*mutationRate = 10 * taskInstance.getTaskNumber() - Math.round(progressPercentage * taskInstance.getTaskNumber() * mutX) - Math.round(stagnate * taskInstance.getTaskNumber());
         */

        if (mutationRate < 0) {
            mutationRate = 0;
        }
    }
}
