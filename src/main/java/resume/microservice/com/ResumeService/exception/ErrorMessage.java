package resume.microservice.com.ResumeService.exception;

import java.util.Date;

public class ErrorMessage {

    private Date timestamp;
    private String message;
    private int status;
    private String statusValue;

    public ErrorMessage() {};

    public ErrorMessage(int status, Date timestamp, String message) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }


    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getStatusValue() {
        return statusValue;
    }

}
