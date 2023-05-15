package greedy;

import results.ResultPrinter;
import results.Results;
import taskInstance.TaskInstance;
import taskInstance.TaskInstanceReader;

import java.util.List;
import java.util.stream.Collectors;

public class LptMain {
    public static void main(String[] args) {
        List<TaskInstance> taskInstances = TaskInstanceReader.getTaskInstanceFromDirectory("C:\\opt\\lpt");
        List<Results> results = taskInstances.stream().map(taskInstance -> {
            List<Process> processes = GreedySolver.solveTaskInstance(taskInstance,true);

            return new Results(taskInstance, processes);
        }).collect(Collectors.toList());

        results.forEach(results1 -> {
            ResultPrinter.printResults(results1, false);
        });
    }
}
