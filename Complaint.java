import java.time.LocalDate;

public class Complaint {

    private String id;
    private String description;
    private String status; // Pending or Resolved
    private String resolutionDetails;
    private LocalDate date;

    public Complaint(String id, String description, LocalDate date) {
        this.id = id;
        this.description = description;
        this.status = "Pending";
        this.resolutionDetails = "";
        this.date = date;
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public String getStatus() {
        return status;
    }
    public LocalDate getDate() {
        return date;
    }
    public String getResolutionDetails() {
        return resolutionDetails;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }
    public void setResolutionDetails(String resolutionDetails) {
        this.resolutionDetails = resolutionDetails;
    }

    @Override
    public String toString() {
        return "\nComplaint ID: " + id + "\nDescription: " + description + "\nStatus: " + status + "\nDate: " + date + "\nResolution Details: " + resolutionDetails;
    }
}

