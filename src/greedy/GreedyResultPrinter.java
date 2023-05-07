package greedy;

import taskInstance.TaskInstance;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class GreedyResultPrinter {
    public static void printResults(List<Process> processes, TaskInstance taskInstance, boolean printProcesses) {
        IntSummaryStatistics summaryStatistics = processes.stream()
                .map(Process::getAssignedTasks)
                .mapToInt(integers -> integers.stream().mapToInt(Integer::intValue).sum())
                .summaryStatistics();

        System.out.println("Instancja: " + taskInstance.getInstanceName());
        System.out.println("Wynik: " + summaryStatistics.getMax());

        if (printProcesses) {
            for (Process process : processes) {
                List<String> taskList = process.getAssignedTasks().stream().map(String::valueOf).collect(Collectors.toList());
                System.out.print("\n");
                System.out.print(getProcessName(process, processes.get(processes.size() - 1).getProcessName().length()) + "|");
                System.out.print(String.join("|", taskList));
                System.out.print("|");
            }
        }

        System.out.println();
    }

    private static String getProcessName(Process process, int maxProcessLength) {
        String newProcessName = process.getProcessName();
        int difference = maxProcessLength - String.valueOf(process.getProcessName()).length();

        newProcessName += ": ";
        for (int i = 0; i < difference; i++) {
            newProcessName += " ";
        }

        return newProcessName;
    }
}
