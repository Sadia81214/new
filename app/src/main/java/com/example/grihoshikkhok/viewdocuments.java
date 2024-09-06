package com.example.grihoshikkhok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class viewdocuments extends AppCompatActivity {
    ImageView i1,i2,i3,i4;
    String uID,ref1,ref2,ref3,ref4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdocuments);
        getViews();
        uID=getIntent().getStringExtra("uID");
        ref1="docs/Bc_id"+uID+".jpg";
        ref2="docs/NIDFront"+uID+".jpg";
        ref3="docs/NIDBack"+uID+".jpg";
        ref4="docs/ID"+uID+".jpg";
        StorageReference mImageRef1 = FirebaseStorage.getInstance().getReference(ref1);
        StorageReference mImageRef2 = FirebaseStorage.getInstance().getReference(ref2);
        StorageReference mImageRef3 = FirebaseStorage.getInstance().getReference(ref3);
        StorageReference mImageRef4 = FirebaseStorage.getInstance().getReference(ref4);
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
        mImageRef4.getBytes(TWO_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        i4.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failed......................");
                    }
                });
    }
    void getViews(){
        i1=findViewById(R.id.img0);
        i2=findViewById(R.id.img1);
        i3=findViewById(R.id.img2);
        i4=findViewById(R.id.img3);
    }
}