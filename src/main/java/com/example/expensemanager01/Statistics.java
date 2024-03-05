package com.example.expensemanager01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics extends AppCompatActivity {

    private float variation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String userID = fAuth.getCurrentUser().getUid();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Map<String, Integer> groupedSpendingsByCategory = new HashMap<>();

        List<TextView> categories = Arrays.asList(findViewById(R.id.statisticsCategory1),
                findViewById(R.id.statisticsCategory2), findViewById(R.id.statisticsCategory3),
                findViewById(R.id.statisticsCategory4), findViewById(R.id.statisticsCategory5),
                findViewById(R.id.statisticsCategory6));
        List<ProgressBar> progressBars = Arrays.asList(findViewById(R.id.statiticsProgressBarCategory1),
                findViewById(R.id.statiticsProgressBarCategory2), findViewById(R.id.statiticsProgressBarCategory3),
                findViewById(R.id.statiticsProgressBarCategory4), findViewById(R.id.statiticsProgressBarCategory5),
                findViewById(R.id.statiticsProgressBarCategory6));

        TextView prevMonthSpendingsLabel = findViewById(R.id.statisticsPreviousMonthSpendings);
        TextView currentMonthSpendingsLabel = findViewById(R.id.statisticsCurrentMonthSpendings);

        db.collection("items")
                .whereEqualTo("userUID", userID)
                .whereEqualTo("month", currentMonth)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        TextView labelCurrentMonth = findViewById(R.id.statisticsCurrentMonth);

                        float currentMonthSpendings = 0;
                        int position = 0;

                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String category = document.getString("category");
                            if (!groupedSpendingsByCategory.containsKey(category))
                                groupedSpendingsByCategory.put(category, document.get("itemCost", Integer.class));
                            else
                                groupedSpendingsByCategory.put(category, groupedSpendingsByCategory.get(category) + document.get("itemCost", Integer.class));
                        }

                        for (String key : groupedSpendingsByCategory.keySet())
                            currentMonthSpendings += groupedSpendingsByCategory.get(key);
                        currentMonthSpendingsLabel.setText(Float.toString(currentMonthSpendings));
                        labelCurrentMonth.setText("Current\nMonth Spendings:");
                        this.variation = currentMonthSpendings;

                        for (String key : groupedSpendingsByCategory.keySet()) {
                            float percentage = (float) Math.floor(groupedSpendingsByCategory.get(key) / currentMonthSpendings * 10000) / 100;
                            categories.get(position).setText(key + " - " + percentage + "%");
                            progressBars.get(position).setMax((int) currentMonthSpendings);
                            progressBars.get(position).setProgress(groupedSpendingsByCategory.get(key));
                            position++;
                        }

                        while (position < categories.size()) {
                            categories.get(position).setVisibility(View.INVISIBLE);
                            progressBars.get(position).setVisibility(View.INVISIBLE);
                            position++;
                        }
                    }
                });

        if (currentMonth == 1)
            currentMonth = 12;
        else currentMonth--;

        db.collection("items")
                .whereEqualTo("userUID", userID)
                .whereEqualTo("month", currentMonth)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        TextView labelPreviousMonth = findViewById(R.id.statisticsPreviousMonth);

                        float result = 0f;

                        for (DocumentSnapshot document : querySnapshot) {
                            result += document.getLong("itemCost");
                        }
                        prevMonthSpendingsLabel.setText(Float.toString(result));
                        labelPreviousMonth.setText("Previous\nMonth Spendings:");

                        TextView labelVariation = findViewById(R.id.statisticsMonthVariation);

                        DecimalFormat decimalFormat = new DecimalFormat("#.##");

                        if (result != 0f) {
                            this.variation = (this.variation - result) / result * 100;
                            labelVariation.setText("Variation: " + decimalFormat.format(this.variation) + "%");
                        } else {
                            labelVariation.setText("Variation: " + decimalFormat.format(this.variation) + " Rise");
                        }

                    }
                });


        // Bottom Navigation Panel
        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Statistics.this, MainBudget.class);
            startActivity(intent);
        });

        ImageView challengeIcon = findViewById(R.id.challangeIcon);
        challengeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Statistics.this, Challanges.class);
            startActivity(intent);
        });

        ImageView billsIcon = findViewById(R.id.billsIcon);
        billsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Statistics.this, Invoices.class);
            startActivity(intent);
        });

        ImageView userIcon = findViewById(R.id.userIcon);
        userIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Statistics.this, Profile.class);
            startActivity(intent);
        });

        ImageView addIcon = findViewById(R.id.addIcon);
        addIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Statistics.this, AddNewItem.class);
            startActivity(intent);
        });
    }
}
