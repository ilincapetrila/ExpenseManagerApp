package com.example.expensemanager01;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ChallengeViewHolder> {

    private List<Challenge> challenges;

    public ChallengesAdapter(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_challenge, parent, false);
        return new ChallengeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Challenge challenge = challenges.get(position);

        holder.textDescription.setText(challenge.getDescription());
        holder.textStartDate.setText("Start Date: " + challenge.getStartDate());
        holder.textDuration.setText("Duration: " + challenge.getDuration() + " Days");

        Switch switchActive = holder.itemView.findViewById(R.id.switchActive);

        switchActive.setChecked(challenge.isActive());

        holder.checkboxCompleted.setChecked(challenge.isCompleted());

        holder.checkboxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challenge.setCompleted(isChecked);
            updateChallengeCompletionStatusInFirestore(challenge.getUserUID(), challenge.getDescription(), isChecked);

        });

    }
    private void updateChallengeCompletionStatusInFirestore(String userUID, String description, boolean completed) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference challengesCollection = firestore.collection("challenges");

        Query query = challengesCollection
                .whereEqualTo("userUID", userUID)
                .whereEqualTo("description", description);

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            String challengeId = document.getId();
                            challengesCollection.document(challengeId)
                                    .update("completed", completed)
                                    .addOnSuccessListener(aVoid -> {
                                        // Update successful
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the update failure
                                    });
                        }
                    } else {
                        // Handle the query failure
                        Exception exception = task.getException();
                        if (exception != null) {
                            // Log the exception or show an error message
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    static class ChallengeViewHolder extends RecyclerView.ViewHolder {

        TextView textDescription;
        TextView textStartDate;
        TextView textDuration;
        Switch switchActive;
        CheckBox checkboxCompleted;

        public ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescription = itemView.findViewById(R.id.textDescription);
            textStartDate = itemView.findViewById(R.id.textStartDate);
            textDuration = itemView.findViewById(R.id.textDuration);
            switchActive = itemView.findViewById(R.id.switchActive);
            checkboxCompleted = itemView.findViewById(R.id.checkboxCompleted);

        }
    }
}
