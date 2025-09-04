// File: StudyLog.java
import java.io.Serializable;
import java.time.LocalDate;

public class StudyLog implements Serializable {
    // Making fields private for better encapsulation
    private LocalDate date;
    private String subject;
    private double duration;
    private String description;

    public StudyLog(LocalDate date, String subject, double duration, String description) {
        this.date = date;
        this.subject = subject;
        this.duration = duration;
        this.description = description;
    }

    // Getter methods
    public LocalDate getDate() { return date; }
    public String getSubject() { return subject; }
    public double getDuration() { return duration; }
    public String getDescription() { return description; }

    // Setter methods (useful for editing)
    public void setDate(LocalDate date) { this.date = date; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setDuration(double duration) { this.duration = duration; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return date + "|" + subject + "|" + duration + "|" + description;
    }
}