package com.example.grihoshikkhok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class birthorid extends AppCompatActivity {
    Button imageChoose;
    ImageView imgPreView;
    int IMAGE_REQUEST = 2;
    Bitmap defaultBitmap;
    Button picUpload;
    Uri selectedImage;
    EditText imgurl;
    byte[] img;
    String role;
    StorageReference ImagesRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthorid);
        getViews();
        role = getIntent().getStringExtra("role");

        imageChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE_REQUEST);
            }
        });

        picUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImage != null) {
                    // User selected an image, upload selected image
                    uploadImage(selectedImage);
                } else {
                    // No image selected, upload default image
                    uploadDefaultImage();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                img = baos.toByteArray();
                imgPreView.setImageBitmap(bitmap);
                String id = mAuth.getUid();
                ImagesRef = storageRef.child("docs/Bc_id" + id + ".jpg");
                imgurl.setText(selectedImage.toString());
                picUpload.setText("Upload");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            img = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String id = mAuth.getUid();
        ImagesRef = storageRef.child("docs/Bc_id" + id + ".jpg");

        ImagesRef.putBytes(img)
                .addOnSuccessListener(taskSnapshot -> navigateToNextActivity())
                .addOnFailureListener(e -> {
                    // Handle image upload failure
                    // Display an error message or retry logic
                });
    }

    private void uploadDefaultImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.certificate);
        defaultBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        img = baos.toByteArray();

        String id = mAuth.getUid();
        ImagesRef = storageRef.child("docs/Bc_id" + id + ".jpg");

        ImagesRef.putBytes(img)
                .addOnSuccessListener(taskSnapshot -> navigateToNextActivity())
                .addOnFailureListener(e -> {
                    // Handle default image upload failure
                    // Display an error message or retry logic
                });
    }

    private void getViews() {
        imageChoose = findViewById(R.id.xChoosebtn);
        picUpload = findViewById(R.id.xup);
        imgurl = findViewById(R.id.ximgurl);
        imgPreView = findViewById(R.id.ximagePreview);
    }

    private void navigateToNextActivity() {
        Intent intent = new Intent(birthorid.this, nidfront.class);
        intent.putExtra("role", role);
        startActivity(intent);
        finish(); // Optional: Finish current activity
    }
}
