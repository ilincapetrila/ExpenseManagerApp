package com.example.expensemanager01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ItemsByCategory extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_by_category);

        db = FirebaseFirestore.getInstance();

        Intent intent2 = getIntent();
        if (intent2.hasExtra("category")) {
            category = intent2.getStringExtra("category");
            TextView categoryTitle = findViewById(R.id.CategoryTitle);
            categoryTitle.setText(category);
            loadItems();

        }



        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        TextView categorySum = findViewById(R.id.categorySum);
        String currentUserUID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

        categorySum.setText("0");

        db.collection("items")
                .whereEqualTo("userUID", currentUserUID)
                .whereEqualTo("month", currentMonth)
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            try {
                                long sumPrices = 0;
                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                    Long priceValue = document.getLong("itemCost");
                                    if (priceValue != null) {
                                        sumPrices += priceValue;
                                    }
                                }
                                categorySum.setText(String.valueOf(sumPrices)+" Lei spent this month");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            categorySum.setText("0 Lei spent this month");
                        }
                    }
                });


        ImageView statisticsIcon = findViewById(R.id.statisticsIcon);
        statisticsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(ItemsByCategory.this, Statistics.class);
            startActivity(intent);
        });

        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(ItemsByCategory.this, MainBudget.class);
            startActivity(intent);
        });

        ImageView challangeIcon = findViewById(R.id.challangeIcon);
        challangeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(ItemsByCategory.this, Challanges.class);
            startActivity(intent);
        });

        ImageView billsIcon = findViewById(R.id.billsIcon);
        billsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(ItemsByCategory.this, Invoices.class);
            startActivity(intent);
        });

        ImageView addIcon = findViewById(R.id.addIcon);
        addIcon.setOnClickListener(view -> {
            Intent intent = new Intent(ItemsByCategory.this, AddNewItem.class);
            startActivity(intent);
        });

        ImageView userIcon = findViewById(R.id.userIcon);
        userIcon.setOnClickListener(view -> {
            Intent intent = new Intent(ItemsByCategory.this, Profile.class);
            startActivity(intent);
        });
    }

    private void loadItems() {
        fAuth = FirebaseAuth.getInstance();
        String currentUserUID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        db.collection("items")
                .whereEqualTo("category", category)
                .whereEqualTo("userUID", currentUserUID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            displayItems(task.getResult().getDocuments());
                        } else {
                            // Handle case where no items are found for the category
                        }
                    } else {
                        // Handle error getting items documents
                    }
                });
    }

    private void displayItems(List<DocumentSnapshot> items) {
        LinearLayout itemContainer = findViewById(R.id.itemContainer); // Replace with your layout ID
        LayoutInflater inflater = LayoutInflater.from(this);

        for (DocumentSnapshot item : items) {
            View itemView = inflater.inflate(R.layout.item_cview_category, itemContainer, false);

            TextView itemName = itemView.findViewById(R.id.textItemName);
            TextView itemPrice = itemView.findViewById(R.id.textItemPrice);

            itemName.setText(item.getString("itemName"));
            itemPrice.setText( item.getLong("itemCost").toString() + " Lei");

            itemContainer.addView(itemView);
        }
    }
}