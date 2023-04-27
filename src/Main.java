import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {
        /*        TaskInstanceGenerator.generateTaskInstance();*/

        File directory = new File("C:\\a");
        List<File> files = Arrays.asList(directory.listFiles());

        List<TaskInstance> taskInstances = files.stream()
                .filter(file -> !file.isDirectory())
                .map(file -> TaskInstanceReader.readTaskInstance(file.getAbsolutePath()))
                .toList();

        List<Results> results = taskInstances.stream().map(taskInstance -> {
            Genetic genetic = new Genetic();
            Chromosome chromosome = genetic.solve(taskInstance);

            Results result = new Results();
            result.setChromosome(chromosome);
            result.setInstanceName(taskInstance.getInstanceName());

            int sum = chromosome.getGenoms().stream().mapToInt(Genom::getExecutionTime).sum();
            int sum1 = taskInstance.getTasks().stream().mapToInt(Integer::intValue).sum();

            result.setIntegrityCheck(sum1 == sum);

            return result;
        }).toList();

        results.forEach(Main::print);

        System.out.println("a");



/*        TaskInstance taskInstance = TaskInstanceReader.readTaskInstance("C:\\a\\m25.txt");
        Genetic.solve(taskInstance);*/

/*        List<Process> processes = TaskInstanceSolver.solveTaskInstance(taskInstance);
        printResults(processes);*/
    }

    private static void print(Results results) {

        System.out.println("\n" + results.getInstanceName());
        System.out.println("Best fitness: " + results.getChromosome().getFitness());
        System.out.println("Passed integrity check: " + results.isIntegrityCheck());

        System.out.println();

        Map<Integer, List<Genom>> processes = results.getChromosome().getGenoms()
                .stream().collect(Collectors.groupingBy(Genom::getProcess));

/*        List<Integer> keys = processes.keySet().stream().sorted().toList();

        for (Integer key : keys) {
            List<Genom> genoms = processes.get(key);
            String tasks = genoms.stream()
                    .map(genom -> String.valueOf(genom.getExecutionTime()))
                    .collect(Collectors.joining("|"));

            System.out.println("Process " + key + " " + tasks);
        }

        System.out.println();*/
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