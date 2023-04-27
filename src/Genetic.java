import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Genetic {
    //For random selection
    private static final Random random = new Random();
    private static List<Integer> cumulativeScore = new ArrayList<>();
    private static int score = 0;


    //Simulation control
    private static final int populationSize = 1000;


    //Simulation duration
    private static final int duration = 3;
    private static final ChronoUnit unit = ChronoUnit.MINUTES;


    public static int solve(TaskInstance taskInstance) {
        int bestResult = 10000;
        List<Chromosome> population = generateInitialPopulation(taskInstance, populationSize);
        calculateFitness(population);

        System.out.println("Start time: " + new Date() + "\n");

        int populationNumber = 0;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + TimeUnit.of(unit).toMillis(duration)) {
            populationNumber++;
            population = generateNewPopulation(population);
            calculateFitness(population);
            mutate(population, taskInstance);

            if (populationNumber % 100 == 0) {
                System.out.println("Population " + (populationNumber) + " best result: " + population.get(0).getFitness());
            }

            if (population.get(0).getFitness() < bestResult) {
                bestResult = population.get(0).getFitness();
            }
        }


        System.out.println("\nEnd time: " + new Date());
        System.out.println("Best result: " + bestResult);

        return bestResult;
    }

    private static void mutate(List<Chromosome> population, TaskInstance taskInstance) {
        for (Chromosome chromosome : population) {
            for (Genom genom : chromosome.getGenoms()) {
                if (random.nextInt(0, 1000) == 0) {
                    genom.setProcess(random.nextInt(0, taskInstance.getProcessNumber()));
                }
            }
        }
    }

    private static List<Chromosome> generateInitialPopulation(TaskInstance taskInstance, int size) {
        return IntStream.range(0, size)
                .mapToObj(v -> generateChromosome(taskInstance))
                .collect(Collectors.toList());
    }

    private static Chromosome generateChromosome(TaskInstance taskInstance) {
        List<Genom> genoms = new ArrayList<>();

        for (Integer executionTime : taskInstance.getTasks()) {
            int process = random.nextInt(0, taskInstance.getProcessNumber());
            genoms.add(new Genom(executionTime, process));
        }

        return new Chromosome(genoms);
    }

    private static List<Chromosome> generateNewPopulation(List<Chromosome> population) {
        prepareForRandomSelection(population);

        List<Chromosome> newPopulation = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Chromosome parent1 = selectRandom(population);
            Chromosome parent2 = selectRandom(population);
            Chromosome newChromosome = cross(parent1, parent2);
            newPopulation.add(newChromosome);
        }

        return newPopulation;
    }

    private static Chromosome cross(Chromosome parent1, Chromosome parent2) {

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

    private static void calculateFitness(List<Chromosome> population) {
        population.forEach(Genetic::fitness);
        population.sort(Comparator.comparingInt(Chromosome::getFitness));

        calculateScore(population);
    }

    private static void fitness(Chromosome chromosome) {

        Map<Integer, List<Genom>> assignedTask = chromosome.getGenoms().stream().collect(Collectors.groupingBy(Genom::getProcess));

        int fitness = assignedTask.values().stream()
                .mapToInt(genoms -> genoms.stream().mapToInt(Genom::getExecutionTime).sum())
                .max().getAsInt();

        chromosome.setFitness(fitness);
    }

    private static void calculateScore(List<Chromosome> population) {
        Chromosome lastChromosome = population.get(population.size() - 1);

        for (Chromosome chromosome : population) {
            chromosome.setScore(lastChromosome.getFitness() - chromosome.getFitness());
        }
    }

    private static void prepareForRandomSelection(List<Chromosome> population) {
        cumulativeScore = new ArrayList<>();
        score = 0;

        for (Chromosome chromosome : population) {
            score += chromosome.getScore();
            cumulativeScore.add(score);
        }
    }

    private static Chromosome selectRandom(List<Chromosome> population) {
        int randomNumber = random.nextInt(0, score + 1);

        for (int i = 0; i < cumulativeScore.size(); i++) {
            Integer chromosomeCumulativeScore = cumulativeScore.get(i);
            if (chromosomeCumulativeScore > randomNumber) {
                Chromosome chromosome = population.get(i);
                chromosome.setSelectionCount(chromosome.getSelectionCount() + 1);
                return chromosome;
            }
        }

        return population.get(population.size() - 1);
    }

}
