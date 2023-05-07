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
        List<TaskInstance> taskInstances = TaskInstanceReader.getTaskInstanceFromDirectory("C:\\a");

        List<TaskInstanceWithSolution> taskInstanceWithSolutions = taskInstances.stream().map(taskInstance -> {
            Genetic genetic = new Genetic(taskInstance);
            Chromosome chromosome = genetic.solve();

            return new TaskInstanceWithSolution(taskInstance, chromosome);
        }).toList();

        taskInstanceWithSolutions.forEach(taskInstanceWithSolution -> {
            GeneticResultsPrinter.printGeneticResult(taskInstanceWithSolution.getChromosome(), taskInstanceWithSolution.getTaskInstance(), false);
        });
    }
}
