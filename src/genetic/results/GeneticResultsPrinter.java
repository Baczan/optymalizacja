package genetic.results;

import genetic.data.Chromosome;
import genetic.data.Genom;
import taskInstance.TaskInstance;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneticResultsPrinter {

    public static void printGeneticResult(Chromosome chromosome, TaskInstance taskInstance, boolean printProcesses) {
        Results results = getResults(chromosome, taskInstance);
        print(results, printProcesses);
    }

    private static Results getResults(Chromosome chromosome, TaskInstance taskInstance) {
        Results result = new Results();
        result.setChromosome(chromosome);
        result.setInstanceName(taskInstance.getInstanceName());

        int sum = chromosome.getGenoms().stream().mapToInt(Genom::getExecutionTime).sum();
        int sum1 = taskInstance.getTasks().stream().mapToInt(Integer::intValue).sum();

        result.setIntegrityCheck(sum1 == sum);

        return result;
    }

    private static void print(Results results, boolean printProcesses) {

        System.out.println("\n" + results.getInstanceName());
        System.out.println("Best fitness: " + results.getChromosome().getFitness());
        System.out.println("Passed integrity check: " + results.isIntegrityCheck());

        System.out.println();

        if (printProcesses) {
            Map<Integer, List<Genom>> processes = results.getChromosome().getGenoms()
                    .stream().collect(Collectors.groupingBy(Genom::getProcess));

            List<Integer> keys = processes.keySet().stream().sorted().toList();

            for (Integer key : keys) {
                List<Genom> genoms = processes.get(key);
                String tasks = genoms.stream()
                        .map(genom -> String.valueOf(genom.getExecutionTime()))
                        .collect(Collectors.joining("|"));

                System.out.println("Process " + key + " " + tasks);
            }

            System.out.println();
        }
    }

}
