package taskInstance;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TaskInstanceReader {

    public static List<TaskInstance> getTaskInstanceFromDirectory(String path){
        File directory = new File(path);
        List<File> files = Arrays.asList(directory.listFiles());

        List<TaskInstance> taskInstances = files.stream()
                .filter(file -> !file.isDirectory())
                .map(file -> TaskInstanceReader.readTaskInstance(file.getAbsolutePath()))
                .toList();

        return taskInstances;
    }

    public static TaskInstance readTaskInstance(String filePath) {

        try {
            File file = new File(filePath);

            List<Integer> lines = Files.readAllLines(Path.of(filePath)).stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            TaskInstance taskInstance = new TaskInstance();
            taskInstance.setProcessNumber(lines.get(0));
            taskInstance.setTaskNumber(lines.get(1));
            taskInstance.setTasks(lines.subList(2, lines.get(1) + 2));

            taskInstance.setInstanceName(file.getName());
            return taskInstance;
        } catch (Exception e) {
            System.out.println("Błąd podczas wczytywania instancji zadania: " + filePath);
        }

        return null;
    }
}
