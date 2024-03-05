package com.example.expensemanager01;

public class Item {
    private String itemName;
    private double itemCost;
    private String userUID;
    private int month;
    private String category;

    // Default constructor required for Firestore
    public Item() {}

    public Item(String itemName, double itemCost, String userUID, int month, String category) {
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.userUID = userUID;
        this.month = month;
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemCost() {
        return itemCost;
    }

    public String getUserUID() {
        return userUID;
    }

    public int getMonth() {
        return month;
    }

    public String getCategory() {
        return category;
    }
}
