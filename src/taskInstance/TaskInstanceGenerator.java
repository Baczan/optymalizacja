package taskInstance;

import java.io.FileWriter;
import java.util.Random;

public class TaskInstanceGenerator {
    static String defaultFilePath = "C:\\a\\inst.txt";
    static int defaultProcessInstances = 50;
    static int defaultTaskNumber = 1000;
    static int defaultMinTaskSize = 10;
    static int defaultMaxTaskSize = 1000;

    public static void generateTaskInstance() {
        generateTaskInstance(defaultFilePath, defaultProcessInstances, defaultTaskNumber, defaultMinTaskSize, defaultMaxTaskSize);
    }

    public static void generateTaskInstance(String filePath, int processInstances, int taskNumber, int minTaskSize, int maxTaskSize) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(processInstances + "\n");
            fileWriter.write(String.valueOf(taskNumber));
            Random random = new Random();
            for (int i = 0; i < taskNumber; i++) {
                int taskSize = random.nextInt(minTaskSize, maxTaskSize + 1);
                fileWriter.write("\n" + taskSize);
            }
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Błąd podczas generowania instancji zadania: "+ e.getMessage());
        }
    }
}
