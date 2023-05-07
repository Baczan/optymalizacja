package genetic;

import taskInstance.TaskInstance;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Genetic {
    //For random selection
    private final Random random = new Random();
    private List<Integer> cumulativeScore = new ArrayList<>();
    private int score = 0;


    //Simulation control
    private final int populationSize = 1000;


    //Simulation duration
    private final int duration = 30;
    private final ChronoUnit unit = ChronoUnit.SECONDS;


    public Chromosome solve(TaskInstance taskInstance) {
        int bestResult = 100000;
        Population population = generateInitialPopulation(taskInstance, populationSize);
        calculateFitness(population);

        System.out.println("Start time: " + new Date() + "\n");

        int populationNumber = 0;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + TimeUnit.of(unit).toMillis(duration)) {
            populationNumber++;
            population = generateNewPopulation(population);
            calculateFitness(population);
            mutate(population, taskInstance);

            if (populationNumber % 1000 == 0) {
                System.out.println("taskInstance.TaskInstance: " + taskInstance.getInstanceName() + " Population " + (populationNumber) + " best result: " + population.getChromosomes().get(0).getFitness());
            }

            if (population.getChromosomes().get(0).getFitness() < bestResult) {
                bestResult = population.getChromosomes().get(0).getFitness();
            }
        }


        System.out.println("\nEnd time: " + new Date());
        System.out.println("Best result: " + bestResult);

        return population.getChromosomes().get(0);
    }

    private void mutate(Population population, TaskInstance taskInstance) {
        for (Chromosome chromosome : population.getChromosomes()) {
            for (Genom genom : chromosome.getGenoms()) {
                if (random.nextInt(0, taskInstance.getTaskNumber() * 10) == 0) {
                    genom.setProcess(random.nextInt(0, taskInstance.getProcessNumber()));
                }
            }
        }
    }

    private Population generateInitialPopulation(TaskInstance taskInstance, int size) {
        List<Chromosome> chromosomes = IntStream.range(0, size)
                .mapToObj(v -> generateChromosome(taskInstance))
                .collect(Collectors.toList());

        return new Population(chromosomes);
    }

    private Chromosome generateChromosome(TaskInstance taskInstance) {
        List<Genom> genoms = new ArrayList<>();

        for (Integer executionTime : taskInstance.getTasks()) {
            int process = random.nextInt(0, taskInstance.getProcessNumber());
            genoms.add(new Genom(executionTime, process));
        }

        return new Chromosome(genoms);
    }

    private Population generateNewPopulation(Population population) {
        prepareForRandomSelection(population);

        int eliteNumber = Math.round(populationSize * 0.1f);

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


        for (int i = eliteNumber; i < populationSize; i++) {
            Chromosome parent1 = selectRandom(population);
            Chromosome parent2 = selectRandom(population);
            Chromosome newChromosome = cross(parent1, parent2);
            newPopulation.getChromosomes().add(newChromosome);
        }

        return newPopulation;
    }

    private Chromosome cross(Chromosome parent1, Chromosome parent2) {

        List<Genom> genoms = new ArrayList<>();

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

            genoms.add(new Genom(baseGenom.getExecutionTime(), baseGenom.getProcess()));
        }

        return new Chromosome(genoms);
    }

    private void calculateFitness(Population population) {
        population.getChromosomes().forEach(this::fitness);
        population.getChromosomes().sort(Comparator.comparingInt(Chromosome::getFitness));

        calculateScore(population);
    }

    private void fitness(Chromosome chromosome) {

        Map<Integer, List<Genom>> assignedTask = chromosome.getGenoms().stream().collect(Collectors.groupingBy(Genom::getProcess));

        int fitness = assignedTask.values().stream()
                .mapToInt(genoms -> genoms.stream().mapToInt(Genom::getExecutionTime).sum())
                .max().getAsInt();

        chromosome.setFitness(fitness);
    }

    private void calculateScore(Population population) {
        Chromosome lastChromosome = population.getChromosomes().get(population.getChromosomes().size() - 1);

        for (Chromosome chromosome : population.getChromosomes()) {
            chromosome.setScore(lastChromosome.getFitness() - chromosome.getFitness());
        }
    }

    private void prepareForRandomSelection(Population population) {
        cumulativeScore = new ArrayList<>();
        score = 0;

        for (Chromosome chromosome : population.getChromosomes()) {
            score += chromosome.getScore();
            cumulativeScore.add(score);
        }
    }

    private Chromosome selectRandom(Population population) {
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
