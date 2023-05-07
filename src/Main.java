import genetic.Chromosome;
import genetic.Genetic;
import genetic.Genom;
import genetic.Results;
import greedy.Process;
import taskInstance.TaskInstance;
import taskInstance.TaskInstanceReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        taskInstance.TaskInstanceGenerator.generateTaskInstance();
    }
}