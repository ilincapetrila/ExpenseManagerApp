package com.example.expensemanager01;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.*;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ImageItemUploader {
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public ImageItemUploader() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void uploadImage(Bitmap bitmap) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageName = "image_" + timestamp + ".jpg";
        StorageReference imageRef = storageReference.child("images/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("ImageItemUploader", "Image uploaded successfully");

                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();

                        FirebaseAuth fAuth = FirebaseAuth.getInstance();
                        String currentUser = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                        ImageItem imageItem = new ImageItem(downloadUrl, new Date(), currentUser);
                        saveImageItemToFirestore(imageItem);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e("ImageItemUploader", "Error uploading image", e);
            }
        });
    }

    private void saveImageItemToFirestore(ImageItem imageItem) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("images")
                .add(imageItem)
                .addOnSuccessListener(documentReference -> {
                })
                .addOnFailureListener(e -> {
                });
    }
}