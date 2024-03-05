package com.example.expensemanager01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddItemManually extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    Button lastClickedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_manually);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final TextInputEditText itemNameEditText = findViewById(R.id.itemName);
        final String[] category = new String[1];
        final TextInputEditText itemPrice = findViewById(R.id.itemPrice);
        Button buttonPrice50 = findViewById(R.id.buttonPrice50);
        buttonPrice50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemPrice.setText("50");

            }
        });
        Button buttonPrice100 = findViewById(R.id.buttonPrice100);
        buttonPrice100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemPrice.setText("100");

            }
        });
        Button buttonPrice200 = findViewById(R.id.buttonPrice200);
        buttonPrice200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemPrice.setText("200");

            }
        });
        Button buttonPrice500 = findViewById(R.id.buttonPrice500);
        buttonPrice500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemPrice.setText("500");

            }
        });
        Button buttonPrice1000 = findViewById(R.id.buttonPrice1000);
        buttonPrice1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemPrice.setText("1000");

            }
        });
        Button buttonPrice1500 = findViewById(R.id.buttonPrice1500);
        buttonPrice1500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemPrice.setText("1500");

            }
        });

        Button buttonCategBills = findViewById(R.id.buttonCategBills);
        Button buttonCategGroceries = findViewById(R.id.buttonCategGroceries);
        Button buttonCategClothes = findViewById(R.id.buttonCategClothes);
        Button buttonCategSweets = findViewById(R.id.buttonCategSweets);
        Button buttonCategAlcohol = findViewById(R.id.buttonCategAlcohol);
        Button buttonCategTravel = findViewById(R.id.buttonCategTravel);

//        buttonCategBills.setOnClickListener(view -> category[0] ="Bills");
//        buttonCategGroceries.setOnClickListener(view -> category[0] = "Groceries");
//        buttonCategClothes.setOnClickListener(view -> category[0] = "Clothes");
//        buttonCategSweets.setOnClickListener(view -> category[0] = "Sweets");
//        buttonCategAlcohol.setOnClickListener(view -> category[0] = "Alcohol");
//        buttonCategTravel.setOnClickListener(view -> category[0] = "Travel");



// ... (your existing code)

        buttonCategBills.setOnClickListener(view -> {
            category[0] ="Bills";
            updateButtonColor((Button) view);
        });

        buttonCategGroceries.setOnClickListener(view -> {
            category[0] = "Groceries";
            updateButtonColor((Button) view);
        });

        buttonCategClothes.setOnClickListener(view -> {
            category[0] = "Clothes";
            updateButtonColor((Button) view);
        });

        buttonCategSweets.setOnClickListener(view -> {
            category[0] = "Sweets";
            updateButtonColor((Button) view);
        });

        buttonCategAlcohol.setOnClickListener(view -> {
            category[0] = "Alcohol";
            updateButtonColor((Button) view);
        });

        buttonCategTravel.setOnClickListener(view -> {
            category[0] = "Travel";
            updateButtonColor((Button) view);
        });

// ...



        Button buttonAddItem = findViewById(R.id.buttonSave);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = itemNameEditText.getText().toString().trim();
                String itemPriceStr = itemPrice.getText().toString().trim();


                if (itemName.isEmpty() || itemPriceStr.isEmpty()) {
                    return;
                }

                double itemPrice = Double.parseDouble(itemPriceStr);
                String userUID = fAuth.getCurrentUser().getUid();
                int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

                Item newItem = new Item(itemName, itemPrice, userUID, currentMonth, category[0]);

                db.collection("items")
                        .add(newItem)
                        .addOnSuccessListener(documentReference -> {
                        })
                        .addOnFailureListener(e -> {
                        });

                Intent intent = new Intent(AddItemManually.this, MainBudget.class);
                startActivity(intent);
            }
        });


        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddItemManually.this, MainBudget.class);
            startActivity(intent);
        });

        ImageView statisticsIcon = findViewById(R.id.statisticsIcon);
        statisticsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddItemManually.this, Statistics.class);
            startActivity(intent);
        });

        ImageView challangeIcon = findViewById(R.id.challangeIcon);
        challangeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddItemManually.this, Challanges.class);
            startActivity(intent);
        });

        ImageView billsIcon = findViewById(R.id.billsIcon);
        billsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddItemManually.this, Invoices.class);
            startActivity(intent);
        });

        ImageView addIcon = findViewById(R.id.addIcon);
        addIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddItemManually.this, AddNewItem.class);
            startActivity(intent);
        });

        ImageView userIcon = findViewById(R.id.userIcon);
        userIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AddItemManually.this, Profile.class);
            startActivity(intent);
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(view -> {
            Intent intent = new Intent(AddItemManually.this, MainBudget.class);
            startActivity(intent);
        });
    }
    // Add this method to update the color of the last clicked button
    private void updateButtonColor(Button clickedButton) {
        if (lastClickedButton != null) {
            lastClickedButton.setBackgroundColor(Color.rgb(217, 200, 255));
            lastClickedButton.setTextColor(Color.rgb(0, 0, 0));
        }

        clickedButton.setBackgroundColor(Color.parseColor("#673AB7"));
        clickedButton.setTextColor(Color.rgb(255, 255, 255));

        lastClickedButton = clickedButton;
    }

}