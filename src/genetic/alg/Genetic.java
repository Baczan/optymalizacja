package genetic.alg;

import genetic.data.Chromosome;
import genetic.data.Genom;
import genetic.data.Population;
import taskInstance.TaskInstance;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Genetic {
    //Services
    private final GeneticInitialPopulationGenerator generateInitialPopulation = new GeneticInitialPopulationGenerator();
    private final GeneticSelection geneticSelection = new GeneticSelection();

    //Fields
    private final TaskInstance taskInstance;

    private final Random random = new Random();


    //Simulation control
    private final int populationSize = 1000;

    private int mutationRate = 0;

    private float elitePercentage = 0.05f;

    //Simulation duration
    private final int duration = 30;
    private final ChronoUnit unit = ChronoUnit.SECONDS;

    //Loging
    private Chromosome bestResult;


    public Genetic(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }

    public Chromosome solve() {
        mutationRate = taskInstance.getTaskNumber() * 10;

        Population population = generateInitialPopulation.generateInitialPopulationRandom(taskInstance, populationSize);
        population = applySteps(population);

        System.out.println("Start time: " + new Date() + "\n");

        int populationNumber = 0;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + TimeUnit.of(unit).toMillis(duration)) {
            populationNumber++;
            population = applySteps(population);

            if (populationNumber % 1000 == 0) {
                System.out.println("taskInstance.TaskInstance: " + taskInstance.getInstanceName() + " Population " + (populationNumber) + " best result: " + bestResult.getFitness());
            }
        }

        System.out.println("\nEnd time: " + new Date());
        System.out.println("Best result: " + bestResult.getFitness());

        return population.getChromosomes().get(0);
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
        geneticSelection.prepareForRandomSelection(population);

        int eliteNumber = Math.round(populationSize * elitePercentage);


        //Copy elite chromosomes
        Population newPopulation = new Population();
        for (int i = 0; i < eliteNumber; i++) {
            Chromosome newChromosome = new Chromosome();
            List<Genom> newGenoms = new ArrayList<>();

            Chromosome chromosome = population.getChromosomes().get(i);
            for (Genom genome : chromosome.getGenoms()) {
                newGenoms.add(new Genom(genome.getExecutionTime(), genome.getProcess()));
            }

            newChromosome.setGenoms(newGenoms);
            newPopulation.getChromosomes().add(newChromosome);
        }

        //Create new chromosomes from parents
        for (int i = eliteNumber; i < populationSize; i++) {
            Chromosome parent1 = geneticSelection.selectRandom(population);
            Chromosome parent2 = geneticSelection.selectRandom(population);
            Chromosome newChromosome = cross(parent1, parent2);
            newPopulation.getChromosomes().add(newChromosome);
        }

        return newPopulation;
    }

    private Chromosome cross(Chromosome parent1, Chromosome parent2) {

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
        int eliteNumber = Math.round(populationSize * elitePercentage);

        for (int i = eliteNumber; i < population.getChromosomes().size(); i++) {
            Chromosome chromosome = population.getChromosomes().get(i);
            for (Genom genom : chromosome.getGenoms()) {
                if (random.nextInt(0, mutationRate) == 0) {
                    genom.setProcess(random.nextInt(0, taskInstance.getProcessNumber()));
                }
            }
        }
    }
}
