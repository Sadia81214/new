package com.example.grihoshikkhok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grihoshikkhok.models.Rate;
import com.example.grihoshikkhok.models.StudentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.grihoshikkhok.models.TeacherData;

public class Profile extends AppCompatActivity {
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference infoRef=database.getReference("Info");
    DatabaseReference myRef1 = database.getReference("Student");
    DatabaseReference myRef2 = database.getReference("Teacher");
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teacher");
    TeacherData teacherData;
    StudentData studentData;
    String userType,uID,tel,rat;
    boolean ismyprofile,ismy=false,isStudent=false;
    Button profbt,edubt,contbt;
    TextView titlename,t1,t2,t3,t4,t5,t6,t7,tv1,tv2,tv3,tv4,tv5,tv6,tv7,rating;
    ImageView imgv;
    Button msg,call,rate,edit,doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        edit=findViewById(R.id.editprof);
        doc=findViewById(R.id.viewdoc);
        if(getIntent().getExtras().getBoolean("isother")==true){
            edit.setVisibility(View.VISIBLE);
            doc.setVisibility(View.VISIBLE);
        }
        //Get user id from getExtra
        uID=getIntent().getStringExtra("userid");
        if(mAuth.getUid().compareTo(uID)==0){
            ismy=true;
        }
        //ismyprofile=getIntent().getExtras().getBoolean("ismy");
        //here testing
//        uID="vSBrg0qINcXS2K2OTzMtELZNl493";
        getviews();
        imgv=findViewById(R.id.profilepic);
        //String ref="images/"+uID;
//        String ref="images/bgH7taQI1mT7CRhK4H9KFbjZ7Uk2.jpg";
        String ref="images/"+uID+".jpg";
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(ref);
        final long TWO_MEGABYTE = 2048 * 2048*10;
        mImageRef.getBytes(TWO_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imgv.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failed......................");
                    }
                });
        if(!isStudent){
            reference.child(uID).child("Rating").addValueEventListener(new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double total_rating = 0.0,count=0.0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Rate rate = snapshot.getValue(Rate.class);
                        assert rate != null;
                        total_rating+=rate.getRating();
                        count++;
                    }
                    if (count!=0)
                    rat=String.format ("%.1f",total_rating/count)+"/5"+" ("+(int)count+")";

                    else
                        rat="0/0 (0)";

                    rating.setText(rat);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
       infoRef.child(uID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot=task.getResult();
                    ///Assign to class var
                    String userType=String.valueOf(dataSnapshot.child("userType").getValue());
                    if(userType.compareTo("Student")==0){
                        isStudent=true;
                        myRef1.child(uID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                 studentData = dataSnapshot.getValue(StudentData.class);
                                setProfileData();
                                tel=studentData.getPhone();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });
                    }else{
                        myRef2.child(uID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                teacherData= dataSnapshot.getValue(TeacherData.class);
                                setProfileData();
                                tel=teacherData.getPhone();
                                rating.setVisibility(View.VISIBLE);

                                rating.setVisibility(View.VISIBLE);
                                //String r=teacherData.ge
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });
                    }
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile.this,Edit_Profile.class);
                startActivity(intent);
            }
        });
        profbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileData();
            }
        });
        edubt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEduData();
            }
        });
        contbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContactData();
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile.this,MessageActivity.class);
                intent.putExtra("userid",uID);
                startActivity(intent);
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Profile.this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) Profile.this, new String[]{android.Manifest.permission.CALL_PHONE},1
                    );

                } else {

// else block means user has already accepted.And make your phone call here.
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
                    Profile.this.startActivity(intent);

                }
            }
        });
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStudent){
                    Intent intent=new Intent(Profile.this,viewdocuments.class);
                    intent.putExtra("uID",uID);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(Profile.this,Tecdocview.class);
                    intent.putExtra("uID",uID);
                    startActivity(intent);
                }
            }
        });
    }

    void getviews(){
        titlename=findViewById(R.id.titlename);
        profbt=findViewById(R.id.profbtn);
        edubt=findViewById(R.id.edubtn);
        contbt=findViewById(R.id.contbtn);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        t4=findViewById(R.id.t4);
        t5=findViewById(R.id.t5);
        t6=findViewById(R.id.t6);
        t7=findViewById(R.id.t7);
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        tv3=findViewById(R.id.tv3);
        tv4=findViewById(R.id.tv4);
        tv5=findViewById(R.id.tv5);
        tv6=findViewById(R.id.tv6);
        tv7=findViewById(R.id.tv7);
        rate=findViewById(R.id.rate);
        msg=findViewById(R.id.message);
        call=findViewById(R.id.call);
        rating=findViewById(R.id.rating);

    }
    void setProfileData(){
        t1.setText("Name : ");
        t2.setText("ID : ");
        t3.setText("Gender : ");
        t4.setText("Education : ");
        t5.setText("Location : ");
        t6.setText("Subject : ");
        t7.setText("Requirements : ");
        t4.setVisibility(View.VISIBLE);
        tv4.setVisibility(View.VISIBLE);
        t5.setVisibility(View.VISIBLE);
        tv5.setVisibility(View.VISIBLE);
        t6.setVisibility(View.VISIBLE);
        tv6.setVisibility(View.VISIBLE);
        t7.setVisibility(View.VISIBLE);
        tv7.setVisibility(View.VISIBLE);
        msg.setVisibility(View.GONE);
        call.setVisibility(View.GONE);
        rate.setVisibility(View.GONE);
        if(isStudent==true){
            titlename.setText(studentData.getName());
            tv1.setText(studentData.getName());
            tv2.setText(studentData.getId());
            tv3.setText(studentData.getGender());
            tv4.setText(studentData.getLevel());
            tv5.setText(studentData.getAddress());
            tv6.setText(studentData.getSubject());
            tv7.setText(studentData.getDetails());
        }else{
            titlename.setText(teacherData.getName());
            tv1.setText(teacherData.getName());
            tv2.setText(teacherData.getId());
            tv3.setText(teacherData.getGender());
            tv4.setText(teacherData.getLevel());
            tv5.setText(teacherData.getAddress());
            tv6.setText(teacherData.getSubject());
            tv7.setText(teacherData.getDetails());
        }
    }
    void setEduData(){
        t1.setText("Level : ");
        t2.setText("Department : ");
        t3.setText("Institution : ");
        t4.setVisibility(View.INVISIBLE);
        tv4.setVisibility(View.INVISIBLE);
        t5.setVisibility(View.INVISIBLE);
        tv5.setVisibility(View.INVISIBLE);
        t6.setVisibility(View.INVISIBLE);
        tv6.setVisibility(View.INVISIBLE);
        t7.setVisibility(View.INVISIBLE);
        tv7.setVisibility(View.INVISIBLE);
        msg.setVisibility(View.GONE);
        call.setVisibility(View.GONE);
        rate.setVisibility(View.GONE);
        if(isStudent==true){
            titlename.setText(studentData.getName());
            tv1.setText(studentData.getLevel());
            tv2.setText(studentData.getDepartment());
            tv3.setText(studentData.getInstitution());
        }else{
            titlename.setText(teacherData.getName());
            tv1.setText(teacherData.getLevel());
            tv2.setText(teacherData.getDepartment());
            tv3.setText(teacherData.getInstitution());
        }
    }
    void setContactData(){
        t1.setText("Contact Number : ");
        t2.setText("Email : ");
        t3.setText("Address : ");
        t4.setVisibility(View.INVISIBLE);
        tv4.setVisibility(View.INVISIBLE);
        t5.setVisibility(View.INVISIBLE);
        tv5.setVisibility(View.INVISIBLE);
        t6.setVisibility(View.INVISIBLE);
        tv6.setVisibility(View.INVISIBLE);
        t7.setVisibility(View.INVISIBLE);
        tv7.setVisibility(View.INVISIBLE);
        if(!ismy){
            msg.setVisibility(View.VISIBLE);
            call.setVisibility(View.VISIBLE);
        }
        if(isStudent==true){
            titlename.setText(studentData.getName());
            tv1.setText(studentData.getPhone());
            tv2.setText(studentData.getEmail());
            tv3.setText(studentData.getAddress());
        }else{
            titlename.setText(teacherData.getName());
            tv1.setText(teacherData.getPhone());
            tv2.setText(teacherData.getEmail());
            tv3.setText(teacherData.getAddress());
            if(!ismy){
                rate.setVisibility(View.GONE);
            }
        }
    }
}