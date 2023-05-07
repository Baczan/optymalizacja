package greedy;

import java.util.ArrayList;
import java.util.List;

public class Process {
    private String processName;
    private Integer currentExecutionTime = 0;

    private List<Integer> assignedTasks = new ArrayList<>();

    public Process(String processName) {
        this.processName = processName;
    }

    public void assignTask(Integer task) {
        this.assignedTasks.add(task);
        this.currentExecutionTime += task;
    }

    public Integer getCurrentExecutionTime() {
        return currentExecutionTime;
    }

    public void setCurrentExecutionTime(Integer currentExecutionTime) {
        this.currentExecutionTime = currentExecutionTime;
    }

    public List<Integer> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(List<Integer> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
