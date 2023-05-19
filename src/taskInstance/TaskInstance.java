package taskInstance;

import java.util.List;

public class TaskInstance {
    private int processNumber;
    private int taskNumber;

    private float optimalValue;

    private String instanceName;

    private List<Integer> tasks;

    public int getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(int processNumber) {
        this.processNumber = processNumber;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public List<Integer> getTasks() {
        return tasks;
    }

    public void setTasks(List<Integer> tasks) {
        this.tasks = tasks;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public float getOptimalValue() {
        return optimalValue;
    }

    public void setOptimalValue(float optimalValue) {
        this.optimalValue = optimalValue;
    }
}
