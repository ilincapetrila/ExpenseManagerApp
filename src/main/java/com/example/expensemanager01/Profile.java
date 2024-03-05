package com.example.expensemanager01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, MainBudget.class);
            startActivity(intent);
        });

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String currentUSerUID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        db.collection("users")
                .whereEqualTo("userUID", currentUSerUID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                            String firstName = document.getString("firstName");
                            String lastName = document.getString("lastName");
                            String email = document.getString("email");

                            EditText editTextFName = findViewById(R.id.editTextFName);
                            EditText editTextLName = findViewById(R.id.editTextLName);
                            EditText editTextEmail = findViewById(R.id.editTextTextEmailAddress);

                            editTextFName.setText(firstName);
                            editTextLName.setText(lastName);
                            editTextEmail.setText(email);
                        } else {
                            Toast.makeText(Profile.this, "No user found with the specified UID", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Profile.this, "Error getting user data: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });



        ImageView statisticsIcon = findViewById(R.id.statisticsIcon);
        statisticsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, Statistics.class);
            startActivity(intent);
        });

        ImageView challangeIcon = findViewById(R.id.challangeIcon);
        challangeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, Challanges.class);
            startActivity(intent);
        });

        ImageView billsIcon = findViewById(R.id.billsIcon);
        billsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, Invoices.class);
            startActivity(intent);
        });

        ImageView addIcon = findViewById(R.id.addIcon);
        addIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, AddNewItem.class);
            startActivity(intent);
        });


        Button logOutButton = findViewById(R.id.buttonLogOut);

        // Set an OnClickListener for the button
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}