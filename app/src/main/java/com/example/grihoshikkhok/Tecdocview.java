package com.example.grihoshikkhok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Tecdocview extends AppCompatActivity {
    ImageView i1,i2,i3;
    String uID,ref1,ref2,ref3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecdocview);
        getViews();
        uID=getIntent().getStringExtra("uID");
        ref1="docs/NIDFront"+uID+".jpg";
        ref2="docs/NIDBack"+uID+".jpg";
        ref3="docs/ID"+uID+".jpg";
        StorageReference mImageRef1 = FirebaseStorage.getInstance().getReference(ref1);
        StorageReference mImageRef2 = FirebaseStorage.getInstance().getReference(ref2);
        StorageReference mImageRef3 = FirebaseStorage.getInstance().getReference(ref3);
        final long TWO_MEGABYTE = 2048 * 2048*15;
            mImageRef1.getBytes(TWO_MEGABYTE)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            i1.setImageBitmap(bm);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Failed......................");
                        }
                    });
        mImageRef2.getBytes(TWO_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        i2.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failed......................");
                    }
                });
        mImageRef3.getBytes(TWO_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        i3.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failed......................");
                    }
                });
    }
    void getViews(){
        i1=findViewById(R.id.timg0);
        i2=findViewById(R.id.timg1);
        i3=findViewById(R.id.timg2);
    }
}