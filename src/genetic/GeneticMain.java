package genetic;

import genetic.alg.Genetic;
import genetic.data.Chromosome;
import genetic.results.GeneticResultsPrinter;
import genetic.results.TaskInstanceWithSolution;
import taskInstance.TaskInstance;
import taskInstance.TaskInstanceReader;

import java.util.List;

public class GeneticMain {
    public static void main(String[] args) {

/*        for (int i = 200; i < 10000; i += 300) {
            for (int j = 1; j < 11; j++) {

                System.out.println("Population: " + i);
                System.out.println("Mutation: " + j);

                List<TaskInstance> taskInstances = TaskInstanceReader.getTaskInstanceFromDirectory("C:\\a");

                int finalI = i;
                int finalJ = j;
                List<TaskInstanceWithSolution> taskInstanceWithSolutions = taskInstances.stream().map(taskInstance -> {
                    Genetic genetic = new Genetic(taskInstance);
                    Chromosome chromosome = genetic.solve(finalI, finalJ);

                    return new TaskInstanceWithSolution(taskInstance, chromosome);
                }).toList();

                taskInstanceWithSolutions.forEach(taskInstanceWithSolution -> {
                    GeneticResultsPrinter.printGeneticResult(taskInstanceWithSolution.getChromosome(), taskInstanceWithSolution.getTaskInstance(), false);
                });
            }
        }*/

        List<TaskInstance> taskInstances = TaskInstanceReader.getTaskInstanceFromDirectory("C:\\a");

        List<TaskInstanceWithSolution> taskInstanceWithSolutions = taskInstances.stream().map(taskInstance -> {
            Genetic genetic = new Genetic(taskInstance);
            Chromosome chromosome = genetic.solve(300, 1);

            return new TaskInstanceWithSolution(taskInstance, chromosome);
        }).toList();

        taskInstanceWithSolutions.forEach(taskInstanceWithSolution -> {
            GeneticResultsPrinter.printGeneticResult(taskInstanceWithSolution.getChromosome(), taskInstanceWithSolution.getTaskInstance(), false);
        });
    }
}
