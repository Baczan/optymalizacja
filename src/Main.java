import java.io.IOException;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {
/*        TaskInstanceGenerator.generateTaskInstance();*/

        TaskInstance taskInstance = TaskInstanceReader.readTaskInstance("C:\\a\\m50.txt");
        Genetic.solve(taskInstance);

/*        TaskInstance taskInstance = TaskInstanceReader.readTaskInstance("C:\\a\\m25.txt");
        Genetic.solve(taskInstance);*/

/*        List<Process> processes = TaskInstanceSolver.solveTaskInstance(taskInstance);
        printResults(processes);*/
    }

    private static void printResults(List<Process> processes) {
        IntSummaryStatistics summaryStatistics = processes.stream()
                .map(Process::getAssignedTasks)
                .mapToInt(integers -> integers.stream().mapToInt(Integer::intValue).sum())
                .summaryStatistics();

        System.out.println("Średnia: " + summaryStatistics.getAverage());
        System.out.println("Max: " + summaryStatistics.getMax());
        System.out.println("Min: " + summaryStatistics.getMin());

        for (Process process : processes) {
            List<String> taskList = process.getAssignedTasks().stream().map(String::valueOf).collect(Collectors.toList());
            System.out.print("\n");
            System.out.print(getProcessName(process, processes.get(processes.size() - 1).getProcessName().length()) + "|");
            System.out.print(String.join("|", taskList));
            System.out.print("|");
        }
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