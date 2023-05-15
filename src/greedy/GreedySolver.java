package greedy;

import taskInstance.TaskInstance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedySolver {

    //https://en.wikipedia.org/wiki/Longest-processing-time-first_scheduling
    public static List<Process> solveTaskInstance(TaskInstance taskInstance, boolean lpt) {
        List<Integer> tasks = taskInstance.getTasks();

        if(lpt){
            tasks.sort(Comparator.reverseOrder());
        }

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < taskInstance.getProcessNumber(); i++) {
            processes.add(new Process("process " + (i + 1)));
        }

        for (Integer task : tasks) {
            Process process = findProcessWithLowestExecutionTime(processes);
            process.assignTask(task);
        }

        return processes;
    }

    private static Process findProcessWithLowestExecutionTime(List<Process> processes) {

        int minExecutionTime = processes.stream()
                .mapToInt(Process::getCurrentExecutionTime)
                .min()
                .getAsInt();

        return processes.stream()
                .filter(process -> process.getCurrentExecutionTime().equals(minExecutionTime))
                .findFirst().get();
    }
}
