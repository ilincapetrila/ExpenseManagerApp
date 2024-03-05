package com.example.expensemanager01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Objects;

public class MainBudget extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_budget);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextView displayBudget = findViewById(R.id.textViewBudget);
        TextView displayBudgetLeft = findViewById(R.id.textViewBudgetLeft);


        String currentUserUID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;


        TextView textViewBudgetMonth = findViewById(R.id.textViewBudgetMonth);
        String month;
        switch (currentMonth) {
            case 1:
                month = "January";
                break;
            case 2:
                month = "February";
                break;
            case 3:
                month = "March";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "October";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "December";
                break;
            default:
                month = "Invalid Month";
                break;
        }

        textViewBudgetMonth.setText("Budget for " + month);


        final int[] budget = new int[1];

        db.collection("budgets")
                .whereEqualTo("userUID", currentUserUID)
                .whereEqualTo("month", currentMonth)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String existingBudget = document.getString("budget");
                            displayBudget.setText(existingBudget);
                            assert existingBudget != null;
                            budget[0] =Integer.parseInt(existingBudget);
                        } else {
                            BudgetDialog.showBudgetDialog(MainBudget.this, new BudgetDialog.BudgetDialogListener() {
                                @Override
                                public void onBudgetSet(String newBudgetValue) {
                                    displayBudget.setText(newBudgetValue);
                                }
                            });
                        }
                    }
                });

        db.collection("items")
                .whereEqualTo("userUID", currentUserUID)
                .whereEqualTo("month", currentMonth)
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
                                if(sumPrices>budget[0])
                                {
                                    displayBudgetLeft.setTextColor(Color.rgb(200, 5, 50));
                                }
                                displayBudgetLeft.setText(String.valueOf(sumPrices));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            displayBudgetLeft.setText(displayBudget.getText().toString());
                        }
                    }
                });


        Button buttonCategBills = findViewById(R.id.buttonCategBills);
        Button buttonCategGroceries = findViewById(R.id.buttonCategGroceries);
        Button buttonCategClothes = findViewById(R.id.buttonCategClothes);
        Button buttonCategSweets = findViewById(R.id.buttonCategSweets);
        Button buttonCategAlcohol = findViewById(R.id.buttonCategAlcohol);
        Button buttonCategTravel = findViewById(R.id.buttonCategTravel);

        buttonCategBills.setOnClickListener(view -> openItemsByCategory("Bills"));
        buttonCategGroceries.setOnClickListener(view -> openItemsByCategory("Groceries"));
        buttonCategClothes.setOnClickListener(view -> openItemsByCategory("Clothes"));
        buttonCategSweets.setOnClickListener(view -> openItemsByCategory("Sweets"));
        buttonCategAlcohol.setOnClickListener(view -> openItemsByCategory("Alcohol"));
        buttonCategTravel.setOnClickListener(view -> openItemsByCategory("Travel"));


//        TextView textViewLast4Media = findViewById(R.id.textViewLast4Media);
//        textViewLast4Media.setText("X");
//
//        String no4text = "No items in the last 4 months";
//
//        final int[] sumPrices = {0};
//        final int[] itemCount = {0};
//        final int[] averagePrice = new int[1];
//        for (int i = 0; i < 4; i++) {
//            int monthToQuery = (currentMonth - i + 12) % 12 + 1; // Ensure a valid value between 1 and 12
//
//            int finalI = i;
//            db.collection("items")
//                    .whereEqualTo("userUID", currentUserUID)
//                    .whereEqualTo("month", monthToQuery)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            if (task.getResult() != null && !task.getResult().isEmpty()) {
//                                // Items found for the respective month
//                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
//                                    Long priceValue = document.getLong("itemCost");
//                                    if (priceValue != null) {
//                                        synchronized (sumPrices) {
//                                            sumPrices[0] += priceValue.intValue();
//                                        }
//                                        synchronized (itemCount) {
//                                            itemCount[0]++;
//                                        }
//                                    }
//                                }
//
//                                // On the last iteration, calculate and display the average
//                                if (finalI == 3) {
//                                    if (itemCount[0] > 0) {
//                                        averagePrice[0] = sumPrices[0] / itemCount[0];
//                                        textViewLast4Media.setText(String.valueOf(averagePrice[0]));
//
//                                        // Update UI on the main thread if needed
//                                        runOnUiThread(() -> {
//                                            textViewLast4Media.setText(String.valueOf(averagePrice[0]));
//
//                                            // Update UI with averagePrice
//                                            // For example, you can set a TextView text
//                                            // textViewAveragePrice.setText(String.valueOf(averagePrice));
//                                        });
//                                    } else {
//                                        textViewLast4Media.setText(no4text);
//                                        // No items in the last 4 months
//                                        runOnUiThread(() -> {
//                                            textViewLast4Media.setText(no4text);
//
//                                            // Update UI with averagePrice
//                                            // For example, you can set a TextView text
//                                            // textViewAveragePrice.setText(String.valueOf(averagePrice));
//                                        });
//                                        Log.d("AveragePrice", "No items in the last 4 months.");
//                                    }
//                                }
//                            }
//                        } else {
//                            // Handle the error if there are issues fetching documents
//                            textViewLast4Media.setText(no4text);
//                            Log.e("AveragePrice", "Error fetching items: " + task.getException());
//                        }
//                    });
//
//        }


        TextView seeAllExpenses = findViewById(R.id.SeeAllExpensesButton);
        seeAllExpenses.setOnClickListener(view -> {
            Intent intent = new Intent(MainBudget.this, AllExpenses.class);
            startActivity(intent);
        });

        ImageView statisticsIcon = findViewById(R.id.statisticsIcon);
        statisticsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(MainBudget.this, Statistics.class);
            startActivity(intent);
        });

        ImageView challangeIcon = findViewById(R.id.challangeIcon);
        challangeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(MainBudget.this, Challanges.class);
            startActivity(intent);
        });

        ImageView billsIcon = findViewById(R.id.billsIcon);
        billsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(MainBudget.this, Invoices.class);
            startActivity(intent);
        });

        ImageView addIcon = findViewById(R.id.addIcon);
        addIcon.setOnClickListener(view -> {
            Intent intent = new Intent(MainBudget.this, AddNewItem.class);
            startActivity(intent);
        });

        ImageView userIcon = findViewById(R.id.userIcon);
        userIcon.setOnClickListener(view -> {
            Intent intent = new Intent(MainBudget.this, Profile.class);
            startActivity(intent);
        });

    }
    private void openItemsByCategory(String category) {
        Intent intent = new Intent(MainBudget.this, ItemsByCategory.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}