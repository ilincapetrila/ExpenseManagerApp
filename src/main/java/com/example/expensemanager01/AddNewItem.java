package com.example.expensemanager01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class AddNewItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddNewItem.this, MainBudget.class);
            startActivity(intent);
        });

        ImageView statisticsIcon = findViewById(R.id.statisticsIcon);
        statisticsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddNewItem.this, Statistics.class);
            startActivity(intent);
        });

        ImageView challangeIcon = findViewById(R.id.challangeIcon);
        challangeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddNewItem.this, Challanges.class);
            startActivity(intent);
        });

        ImageView billsIcon = findViewById(R.id.billsIcon);
        billsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddNewItem.this, Invoices.class);
            startActivity(intent);
        });

        ImageView userIcon = findViewById(R.id.userIcon);
        userIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddNewItem.this, Profile.class);
            startActivity(intent);
        });

        ImageView addManuallyIcon = findViewById(R.id.addItemManuallyButton);
        addManuallyIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddNewItem.this, AddItemManually.class);
            startActivity(intent);
        });

        ImageView scanItemButton = findViewById(R.id.scanItemButton);
        scanItemButton.setOnClickListener(view -> {
            Toast.makeText(AddNewItem.this, "Scanning will be implemented later", Toast.LENGTH_SHORT).show();
        });

    }
}