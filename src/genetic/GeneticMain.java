package genetic;

import genetic.alg.Genetic;
import greedy.Process;
import results.ResultPrinter;
import results.Results;
import taskInstance.TaskInstance;
import taskInstance.TaskInstanceReader;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class GeneticMain {
    public static void main(String[] args) {
        List<TaskInstance> taskInstances = TaskInstanceReader.getTaskInstanceFromDirectory("C:\\opt\\genetic");

        List<Results> results = taskInstances.stream().map(taskInstance -> {
            Genetic genetic = new Genetic(taskInstance, 1000, 0.0005f, 0.0005f, 0.05f, 3, ChronoUnit.MINUTES, true);
            List<Process> processes = genetic.solve();

            return new Results(taskInstance, processes);
        }).collect(Collectors.toList());

        results.forEach(results1 -> {
            ResultPrinter.printResults(results1, false);
        });
    }
}
