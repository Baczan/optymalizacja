package results;

import java.util.stream.Collectors;

public class ResultPrinter {

    public static void printResults(Results results, boolean all) {
        System.out.println("\nInstance: " + results.getTaskInstance().getInstanceName());
        System.out.println("Best score: " + results.getMaxExecutionTime());
        System.out.println("Optimal value: " + results.getTaskInstance().getOptimalValue());
        float relativeError = Math.abs(results.getTaskInstance().getOptimalValue()-results.getMaxExecutionTime())/results.getTaskInstance().getOptimalValue();
        System.out.println("Relative error: " + relativeError);

        if (all) {
            results.getProcesses().forEach(process -> {
                String tasksString = process.getAssignedTasks().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","));

                System.out.println("Process " + process.getProcessNumber() + " - " + tasksString);
            });
        }
    }

}
