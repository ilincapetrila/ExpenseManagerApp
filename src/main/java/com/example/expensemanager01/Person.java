package com.example.expensemanager01;

public class Person {
    private String userUID;
    private String firstName;
    private String lastName;
    private String email;


    // Default constructor required for Firestore
    public Person() {}

    public Person(String userUID, String firstName, String lastName, String email) {
        this.userUID = userUID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() { return lastName; }
    public String getEmail() {
        return email;
    }

}
