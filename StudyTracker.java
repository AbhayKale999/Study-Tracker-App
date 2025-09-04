// File: StudyTracker.java
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class StudyTracker {
    private final ArrayList<StudyLog> database = new ArrayList<>();
    private final String CSV_FILE_NAME = "StudyTracker.csv";

    public StudyTracker() {
        loadFromCSV();
    }

    public List<StudyLog> getLogs() {
        // Return an unmodifiable list to prevent direct manipulation from the GUI
        return Collections.unmodifiableList(database);
    }

    public void addLog(StudyLog log) {
        database.add(log);
        saveToCSV();
    }
    
    public void updateLog(int index, StudyLog updatedLog) {
        if (index >= 0 && index < database.size()) {
            database.set(index, updatedLog);
            saveToCSV();
        }
    }

    public void deleteLog(int index) {
        if (index >= 0 && index < database.size()) {
            database.remove(index);
            saveToCSV();
        }
    }
    
    public String getSummaryBySubject() {
        if (database.isEmpty()) return "No logs to summarize.";

        TreeMap<String, Double> summaryMap = new TreeMap<>();
        for (StudyLog log : database) {
            summaryMap.put(log.getSubject(), summaryMap.getOrDefault(log.getSubject(), 0.0) + log.getDuration());
        }

        StringBuilder summaryText = new StringBuilder("--- Study Summary by Subject ---\n\n");
        for (String subject : summaryMap.keySet()) {
            summaryText.append(String.format("Subject: %-15s Total Study: %.2f hours%n", subject, summaryMap.get(subject)));
        }
        return summaryText.toString();
    }

    private void saveToCSV() {
        try (FileWriter fw = new FileWriter(CSV_FILE_NAME)) {
            fw.write("Date,Subject,Duration,Description\n");
            for (StudyLog log : database) {
                fw.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        log.getDate(),
                        log.getSubject().replace("\"", "\"\""),
                        log.getDuration(),
                        log.getDescription().replace("\"", "\"\"")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromCSV() {
        File file = new File(CSV_FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (values.length == 4) {
                    LocalDate date = LocalDate.parse(values[0].replaceAll("^\"|\"$", ""));
                    String subject = values[1].replaceAll("^\"|\"$", "");
                    double duration = Double.parseDouble(values[2].replaceAll("^\"|\"$", ""));
                    String description = values[3].replaceAll("^\"|\"$", "");
                    database.add(new StudyLog(date, subject, duration, description));
                }
            }
        } catch (IOException | DateTimeParseException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}