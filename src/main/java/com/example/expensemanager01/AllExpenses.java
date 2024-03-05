package com.example.expensemanager01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class AllExpenses extends AppCompatActivity {
    private FirebaseFirestore db;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_expenses);

        db = FirebaseFirestore.getInstance();
        loadItems();


        ImageView statisticsIcon = findViewById(R.id.statisticsIcon);
        statisticsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AllExpenses.this, Statistics.class);
            startActivity(intent);
        });

        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AllExpenses.this, MainBudget.class);
            startActivity(intent);
        });

        ImageView challangeIcon = findViewById(R.id.challangeIcon);
        challangeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AllExpenses.this, Challanges.class);
            startActivity(intent);
        });

        ImageView billsIcon = findViewById(R.id.billsIcon);
        billsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AllExpenses.this, Invoices.class);
            startActivity(intent);
        });

        ImageView addIcon = findViewById(R.id.addIcon);
        addIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AllExpenses.this, AddNewItem.class);
            startActivity(intent);
        });

        ImageView userIcon = findViewById(R.id.userIcon);
        userIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AllExpenses.this, Profile.class);
            startActivity(intent);
        });
    }
    private void loadItems() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String currentUserUID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        db.collection("items")
//                .whereEqualTo("category", category)
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
            View itemView = inflater.inflate(R.layout.item_view_all, itemContainer, false);

            TextView itemName = itemView.findViewById(R.id.textItemName);
            TextView itemPrice = itemView.findViewById(R.id.textItemPrice);
            TextView itemCategory = itemView.findViewById(R.id.textItemCategory);

            itemName.setText(item.getString("itemName"));
            itemPrice.setText(item.getLong("itemCost").toString() + " Lei");
            itemCategory.setText(item.getString("category"));

            CharSequence text = itemCategory.getText();

            if (text.equals("Alcohol")) {
                itemCategory.setOnClickListener(view -> openItemsByCategory("Alcohol"));
//                itemCategory.setTextColor(0xFFFF0000);
            } else if (text.equals("Bills")) {
                itemCategory.setOnClickListener(view -> openItemsByCategory("Bills"));
            } else if (text.equals("Groceries")) {
                itemCategory.setOnClickListener(view -> openItemsByCategory("Groceries"));
            } else if (text.equals("Clothes")) {
                itemCategory.setOnClickListener(view -> openItemsByCategory("Clothes"));
            } else if (text.equals("Sweets")) {
                itemCategory.setOnClickListener(view -> openItemsByCategory("Sweets"));
            } else if (text.equals("Travel")) {
                itemCategory.setOnClickListener(view -> openItemsByCategory("Travel"));
            }
            itemContainer.addView(itemView);
        }
    }
    private void openItemsByCategory(String category) {
        Intent intent = new Intent(AllExpenses.this, ItemsByCategory.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

}