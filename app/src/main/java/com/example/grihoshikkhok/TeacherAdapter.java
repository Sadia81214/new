package com.example.grihoshikkhok;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.grihoshikkhok.models.Rate;
import com.example.grihoshikkhok.models.TeacherData;

import java.util.List;


public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.MyViewholder>

{

    Context context;
    List<TeacherData> mTeachers;

    public TeacherAdapter(Context context, List < TeacherData > mTeachers) {
        this.context = context;
        this.mTeachers = mTeachers;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.teacher, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder ( @NonNull final TeacherAdapter.MyViewholder holder,
                                   int position) {
        final TeacherData teachers = mTeachers.get(position);

        holder.name.setText(teachers.getName());
        holder.price.setText(teachers.getCostrange());
        holder.subjects.setText(teachers.getSubject());
        holder.rating.setText("0/0 (0)");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("userid", teachers.getUserId());
                intent.putExtra("ismy", false);
                context.startActivity(intent);
            }
        });
        holder.sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", teachers.getUserId());
                context.startActivity(intent);
            }
        });

        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(teachers.getUserId());
            }
        });

        holder.callnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CALL_PHONE}, 1
                    );

                } else {

// else block means user has already accepted.And make your phone call here.
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + teachers.getPhone()));
                    context.startActivity(intent);

                }
            }
        });

        String ref = "images/" + teachers.getUserId() + ".jpg";
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(ref);
        final long TWO_MEGABYTE = 2048 * 2048 * 15;
        mImageRef.getBytes(TWO_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.propic.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failed......................");
                    }
                });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teacher").child(teachers.getUserId()).child("Rating");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total_rating = 0.0, count = 0.0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rate rate = snapshot.getValue(Rate.class);
                    assert rate != null;
                    total_rating += rate.getRating();
                    count++;
                }
                if (count != 0) {
                    holder.rating.setText(String.format("(%.1f/5)", total_rating / count));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    public void ShowDialog(String ratedID)
    {
        DatabaseReference TeacherRatingRef = FirebaseDatabase.getInstance().getReference().child("Teacher");
        LinearLayout linearLayout = new LinearLayout(context);
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(context);
        final RatingBar rating = new RatingBar(context);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        String UID=currentUser.getUid();


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        rating.setLayoutParams(lp);
        rating.setNumStars(5);
        rating.setMax(5);
        rating.setStepSize(1);

        linearLayout.addView(rating);
        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Rate!! ");
        popDialog.setView(linearLayout);

        // Button OK
        popDialog.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                txtView.setText(String.valueOf(rating.getProgress()));
                                Rate rate = new Rate(
                                        UID, rating.getProgress(), UID, ratedID
                                );
                                TeacherRatingRef.child(ratedID).child("Rating").child(UID).setValue(rate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                    }
                                });

                            }

                        })

                // Button Cancel
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        popDialog.create();
        popDialog.show();

    }

    @Override
    public int getItemCount () {
        return mTeachers.size();
    }


    static class MyViewholder extends RecyclerView.ViewHolder {
        public TextView name,price,rating,subjects,sendmsg,callnow,rate;
        public ImageView propic;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            rating = itemView.findViewById(R.id.rating);
            subjects = itemView.findViewById(R.id.subjects);
            sendmsg = itemView.findViewById(R.id.sendmsg);
            callnow = itemView.findViewById(R.id.call);
            rate = itemView.findViewById(R.id.rate);
            propic = itemView.findViewById(R.id.propic);
        }
    }
}
