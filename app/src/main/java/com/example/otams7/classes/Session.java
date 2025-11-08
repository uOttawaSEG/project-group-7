package com.example.otams7.classes;


public class Session {
    private String date;
    private String startTime;
    private String endTime;
    private String tutorId;
    private String status;

    private String studentName;

    public Session() {}

    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getTutorId() { return tutorId; }
    public String getStatus() { return status; }

    public void setDate(String date) { this.date = date; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setTutorId(String tutorId) { this.tutorId = tutorId; }
    public void setStatus(String status) { this.status = status; }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
