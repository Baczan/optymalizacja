package results;

import greedy.Process;
import taskInstance.TaskInstance;

import java.util.List;

public class Results {

    private TaskInstance taskInstance;

    private List<Process> processes;

    private int maxExecutionTime;

    public Results(TaskInstance taskInstance, List<Process> processes) {
        this.taskInstance = taskInstance;
        this.processes = processes;

        maxExecutionTime = processes.stream()
                .mapToInt(process -> process.getAssignedTasks()
                        .stream().mapToInt(Integer::intValue)
                        .sum())
                .max().
                getAsInt();
    }

    public TaskInstance getTaskInstance() {
        return taskInstance;
    }

    public void setTaskInstance(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public int getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public void setMaxExecutionTime(int maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }
}
