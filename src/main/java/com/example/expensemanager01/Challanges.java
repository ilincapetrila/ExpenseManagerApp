package com.example.expensemanager01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Challanges extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private ChallengesAdapter challengesAdapter;
    private List<Challenge> challengesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challanges);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerViewChallenges = findViewById(R.id.recyclerViewChallenges);
        recyclerViewChallenges.setLayoutManager(new LinearLayoutManager(this));

        challengesAdapter = new ChallengesAdapter(challengesList);
        recyclerViewChallenges.setAdapter(challengesAdapter);


        db.collection("challenges")
                .whereEqualTo("userUID", fAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        challengesList.clear();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            String description = document.getString("description");
                            String startDate = document.getString("start_date");
                            String duration = document.getString("duration");
                            boolean active = document.getBoolean("active");
                            boolean completed = document.getBoolean("completed");

                            challengesList.add(new Challenge(description, duration, fAuth.getCurrentUser().getUid(), active, completed, startDate));
                        }
                        challengesAdapter.notifyDataSetChanged();
                    }
                });

        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Challanges.this, MainBudget.class);
            startActivity(intent);
        });

        ImageView statisticsIcon = findViewById(R.id.statisticsIcon);
        statisticsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Challanges.this, Statistics.class);
            startActivity(intent);
        });

        ImageView billsIcon = findViewById(R.id.billsIcon);
        billsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Challanges.this, Invoices.class);
            startActivity(intent);
        });

        ImageView addIcon = findViewById(R.id.addIcon);
        addIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Challanges.this, AddNewItem.class);
            startActivity(intent);
        });

        ImageView userIcon = findViewById(R.id.userIcon);
        userIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Challanges.this, Profile.class);
            startActivity(intent);
        });
    }
}