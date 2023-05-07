package genetic;

import taskInstance.TaskInstance;
import taskInstance.TaskInstanceReader;

import java.util.List;

public class GeneticMain {
    public static void main(String[] args) {
        List<TaskInstance> taskInstances = TaskInstanceReader.getTaskInstanceFromDirectory("C:\\a");

        List<TaskInstanceWithSolution> taskInstanceWithSolutions = taskInstances.stream().map(taskInstance -> {
            Genetic genetic = new Genetic();
            Chromosome chromosome = genetic.solve(taskInstance);

            return new TaskInstanceWithSolution(taskInstance, chromosome);
        }).toList();

        taskInstanceWithSolutions.forEach(taskInstanceWithSolution -> {
            GeneticResultsPrinter.printGeneticResult(taskInstanceWithSolution.getChromosome(), taskInstanceWithSolution.getTaskInstance(), false);
        });
    }
}
