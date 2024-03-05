package com.example.expensemanager01;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Invoices extends AppCompatActivity {
    Button openCameraButton;
    GridView gridView;
    List<ImageItem> images = new ArrayList<>();
    private ImageItemUploader imageItemUploader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String currentUserUID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        CollectionReference imagesCollectionRef = FirebaseFirestore.getInstance().collection("images");

        Query query = imagesCollectionRef.whereEqualTo("userUID", currentUserUID);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    images.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String imageUrl = document.getString("url");
                        Date date = document.getDate("date");

                        ImageItem imageItem = new ImageItem(imageUrl, date);

                        images.add(imageItem);
                    }

                    ((ImageAdapter) gridView.getAdapter()).notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                });

        imageItemUploader = new ImageItemUploader();

        openCameraButton = findViewById(R.id.buttonAddInvoice);
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_open_camera, 200);
            }
        });

        gridView = findViewById(R.id.grid);
        ImageAdapter adapter = new ImageAdapter(this, images);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem clickedImageItem = ((ImageAdapter) gridView.getAdapter()).getImageItem(position);
                Bitmap clickedBitmap = clickedImageItem.getBitmap();
                openLargeImageView(clickedBitmap);
            }

        });

        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Invoices.this, MainBudget.class);
            startActivity(intent);
        });

        ImageView statisticsIcon = findViewById(R.id.statisticsIcon);
        statisticsIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Invoices.this, Statistics.class);
            startActivity(intent);
        });

        ImageView challangeIcon = findViewById(R.id.challangeIcon);
        challangeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Invoices.this, Challanges.class);
            startActivity(intent);
        });

        ImageView userIcon = findViewById(R.id.userIcon);
        userIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Invoices.this, Profile.class);
            startActivity(intent);
        });

        ImageView addIcon = findViewById(R.id.addIcon);
        addIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Invoices.this, AddNewItem.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            imageItemUploader.uploadImage(photo);

            ImageItem imageItem = new ImageItem(photo, new Date());
            images.add(imageItem);

            ((ImageAdapter) gridView.getAdapter()).notifyDataSetChanged();
        }
    }
    private void openLargeImageView(Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_large_image, null);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        ImageView imageViewLarge = dialogView.findViewById(R.id.imageViewLarge);
        imageViewLarge.setImageBitmap(bitmap);

        dialog.show();
    }


}
