package com.example.expensemanager01;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BudgetDialog {

    public interface BudgetDialogListener {
        void onBudgetSet(String newBudgetValue);
    }

    public static void showBudgetDialog(Context context, BudgetDialogListener listener) {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Set New Budget for month "+currentMonth);

        final EditText budgetEditText = new EditText(context);
        builder.setView(budgetEditText);

        builder.setPositiveButton("Set Budget", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newBudgetValue = budgetEditText.getText().toString();
                listener.onBudgetSet(newBudgetValue);

                saveBudgetToFirestore(currentMonth, newBudgetValue);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();
    }
    private static void saveBudgetToFirestore(int currentMonth, String newBudgetValue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        Budget budget = new Budget(fAuth.getCurrentUser().getUid(), currentMonth, newBudgetValue);

        db.collection("budgets")
                .add(budget)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                    }
                });
    }

}
