package results;

import java.util.stream.Collectors;

public class ResultPrinter {

    public static void printResults(Results results, boolean all) {
        System.out.println("\nInstance: " + results.getTaskInstance().getInstanceName());
        System.out.println("Best score: " + results.getMaxExecutionTime());

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
