package greedy;

import java.util.ArrayList;
import java.util.List;

public class Process {
    private int processNumber;
    private Integer currentExecutionTime = 0;

    private List<Integer> assignedTasks = new ArrayList<>();

    public Process(int processNumber) {
        this.processNumber = processNumber;
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

    public int getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(int processNumber) {
        this.processNumber = processNumber;
    }
}
