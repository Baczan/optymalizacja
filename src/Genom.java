public class Genom {
    private int executionTime;

    private int process;

    public Genom() {
    }

    public Genom(int executionTime, int process) {
        this.executionTime = executionTime;
        this.process = process;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }
}
