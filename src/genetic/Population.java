package genetic;

import java.util.ArrayList;
import java.util.List;

public class Population {
    private List<Chromosome> chromosomes = new ArrayList<>();

    public Population() {
    }

    public Population(List<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }

    public List<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(List<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }
}
