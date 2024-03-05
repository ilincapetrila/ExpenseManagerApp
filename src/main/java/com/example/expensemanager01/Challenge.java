package com.example.expensemanager01;

public class Challenge {
    private String description;
    private String duration;
    private String userUID;
    private boolean active;
    private boolean completed;
    private String start_date;

    // Default constructor required for Firestore
    public Challenge() {}

    public Challenge(String description, String duration, String userUID, boolean active, boolean completed, String start_date) {
        this.description = description;
        this.duration = duration;
        this.userUID = userUID;
        this.active = active;
        this.completed = completed;
        this.start_date = start_date;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public String getUserUID() {
        return userUID;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public String getStartDate() {
        return start_date;
    }
}
