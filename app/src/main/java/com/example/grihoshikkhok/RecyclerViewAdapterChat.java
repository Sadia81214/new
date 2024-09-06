package com.example.grihoshikkhok;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.grihoshikkhok.models.Chat;
import com.example.grihoshikkhok.models.User;

import java.util.List;

public class RecyclerViewAdapterChat extends RecyclerView.Adapter<RecyclerViewAdapterChat.MyViewholder> {

    Context context ;
    List<User> mUsers ;
    String thelastmsg ;
    FirebaseUser firebaseUser ;
    DatabaseReference databaseReference ;

    public RecyclerViewAdapterChat(Context context , List<User> mUsers) {
        this.context = context;
        this.mUsers= mUsers ;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewholder holder, int position) {
        final User user = mUsers.get(position);
        holder.textView.setText(user.getUsername());

        //holder.textView2.setText("Last message");
        String ref="images/"+user.getId()+".jpg";
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(ref);
        final long TWO_MEGABYTE = 2048 * 2048;
        mImageRef.getBytes(TWO_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imageView.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failed......................");
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MessageActivity.class);
                intent.putExtra("userid",user.getId());
                context.startActivity(intent);
            }
        });
        lastMessage(user.getId(),holder.textView2,user,holder.textView);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    class MyViewholder extends RecyclerView.ViewHolder{
        public TextView textView , textView2 ;
        public ImageView imageView;
        public LinearLayout linearLayout ;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewchat);
            imageView = itemView.findViewById(R.id.imageViewchat);
            linearLayout = itemView.findViewById(R.id.linearlayout);
            textView2 = itemView.findViewById(R.id.lastmessagetextid);

        }
    }

    public void lastMessage (final String userid , final TextView textView , final User user , final TextView textView1)
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid()))
                    {
                        if (!chat.getSender().equals(firebaseUser.getUid()))
                            thelastmsg = chat.getMessage();
                        else
                            thelastmsg = "You : "+chat.getMessage();
                    }

                    textView.setText(thelastmsg);
                    if (chat.getSeenstatus().equals("unseen") && chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid))
                    {
                        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                        textView1.setTypeface(textView.getTypeface(), Typeface.BOLD);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

