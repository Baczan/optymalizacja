package genetic;

import taskInstance.TaskInstance;

public class TaskInstanceWithSolution {

    private TaskInstance taskInstance;
    private Chromosome chromosome;

    public TaskInstanceWithSolution(TaskInstance taskInstance, Chromosome chromosome) {
        this.taskInstance = taskInstance;
        this.chromosome = chromosome;
    }

    public TaskInstance getTaskInstance() {
        return taskInstance;
    }

    public void setTaskInstance(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    public void setChromosome(Chromosome chromosome) {
        this.chromosome = chromosome;
    }
}
