package greedy;

import taskInstance.TaskInstance;
import taskInstance.TaskInstanceReader;

import java.util.List;

public class GreedyMain {
    public static void main(String[] args) {
        List<TaskInstance> taskInstances = TaskInstanceReader.getTaskInstanceFromDirectory("C:\\a");
        taskInstances.forEach(taskInstance -> {
            List<Process> result = GreedySolver.solveTaskInstance(taskInstance);

            GreedyResultPrinter.printResults(result,taskInstance, false);
        });
    }
}
