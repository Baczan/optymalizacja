package genetic.data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Chromosome {
    private List<Genom> genoms;
    private int fitness = 0;

    public Chromosome() {
    }


    public void updateFitness() {
        Map<Integer, List<Genom>> assignedTask = genoms.stream().collect(Collectors.groupingBy(Genom::getProcess));

        fitness= assignedTask.values().stream()
                .mapToInt(genoms -> genoms.stream().mapToInt(Genom::getExecutionTime).sum())
                .max().getAsInt();
    }

    public Chromosome(List<Genom> genoms) {
        this.genoms = genoms;
    }

    public List<Genom> getGenoms() {
        return genoms;
    }

    public void setGenoms(List<Genom> genoms) {
        this.genoms = genoms;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}
