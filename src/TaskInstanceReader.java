import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class TaskInstanceReader {

    public static TaskInstance readTaskInstance(String filePath) throws IOException {

        try {
            List<Integer> lines = Files.readAllLines(Path.of(filePath)).stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            TaskInstance taskInstance = new TaskInstance();
            taskInstance.setProcessNumber(lines.get(0));
            taskInstance.setTaskNumber(lines.get(1));
            taskInstance.setTasks(lines.subList(2, lines.get(1) + 2));

            return taskInstance;
        } catch (Exception e) {
            System.out.println("Błąd podczas wczytywania instancji zadania: " + filePath);
            throw e;
        }
    }
}
