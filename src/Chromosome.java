import java.util.List;

public class Chromosome {
    private List<Genom> genoms;

    private int fitness = 0;

    private int score = 0;

    private int selectionCount = 0;

    public Chromosome() {
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSelectionCount() {
        return selectionCount;
    }

    public void setSelectionCount(int selectionCount) {
        this.selectionCount = selectionCount;
    }
}
