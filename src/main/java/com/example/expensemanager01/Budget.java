package com.example.expensemanager01;

public class Budget {
    private String userUID;
    private int month;
    private String budget;

    // Default constructor required for Firestore
    public Budget() {}

    public Budget(String userUID, int month, String budget) {
        this.userUID = userUID;
        this.month = month;
        this.budget = budget;
    }

    public String getUserUID() {
        return userUID;
    }

    public int getMonth() {
        return month;
    }

    public String getBudget() {
        return budget;
    }
}

