package com.example.grihoshikkhok;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.example.grihoshikkhok.models.StudentData;

import java.util.List;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewholder>

{

    Context context;
    List<StudentData> mStudents;

    public StudentAdapter(Context context, List < StudentData > mStudents) {
        this.context = context;
        this.mStudents = mStudents;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.student, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder ( @NonNull final StudentAdapter.MyViewholder holder,
                                   int position){
        final StudentData students = mStudents.get(position);

        holder.name.setText(students.getName());
        holder.desc.setText(students.getAddress());
        holder.subjects.setText(students.getSubject());

        holder.sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MessageActivity.class);
                intent.putExtra("userid",students.getUserId());
                context.startActivity(intent);
            }
        });

        holder.callnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CALL_PHONE},1
                    );

                } else {

// else block means user has already accepted.And make your phone call here.
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + students.getPhone()));
                    context.startActivity(intent);

                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Profile.class);
                intent.putExtra("userid",students.getUserId());
                intent.putExtra("ismy",false);
                context.startActivity(intent);
            }
        });

        String ref="images/"+students.getUserId()+".jpg";
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(ref);
        final long TWO_MEGABYTE = 2048 * 2048*15;
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

//        holder.sendmsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Sells")
////                        .child(students.getId());
////                databaseReference1.removeValue();
//            }
//        });
    }

    @Override
    public int getItemCount () {
        return mStudents.size();
    }


    static class MyViewholder extends RecyclerView.ViewHolder {
        public TextView name,desc,subjects,sendmsg,callnow;
        public ImageView propic;
        Button delete;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);
            subjects = itemView.findViewById(R.id.subjects);
            sendmsg = itemView.findViewById(R.id.sendmsg);
            callnow = itemView.findViewById(R.id.call);
            propic = itemView.findViewById(R.id.propic);
        }
    }
}
