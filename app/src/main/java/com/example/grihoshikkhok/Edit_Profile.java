package com.example.grihoshikkhok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.grihoshikkhok.models.StudentData;
import com.example.grihoshikkhok.models.TeacherData;

public class Edit_Profile extends AppCompatActivity {
    EditText uname,uemail,unum,uadress,ulevel,udepartment,uinstitution,usubj,ureqdetails;
    String funame,fuemail,funum,fuadress,fulevel,fudepartment,fuinstitution,fusubj,fureqdetails;
    boolean isStudent=false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference infoRef=database.getReference("Info");
    DatabaseReference myRef1 = database.getReference("Student");
    DatabaseReference myRef2 = database.getReference("Teacher");
    DatabaseReference myRef3 = database.getReference("Users");
    String UID;
    StudentData studentData;
    TeacherData teacherData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        UID= FirebaseAuth.getInstance().getUid();
        getViews();
        infoRef.child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot=task.getResult();
                    ///Assign to class var
                    String userType=String.valueOf(dataSnapshot.child("userType").getValue());
                    if(userType.compareTo("Student")==0){
                        isStudent=true;
                        myRef1.child(UID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                studentData = dataSnapshot.getValue(StudentData.class);
                                setData();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });
                    }else{
                        myRef2.child(UID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                teacherData= dataSnapshot.getValue(TeacherData.class);
                                setData();
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
        findViewById(R.id.Update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funame=uname.getText().toString();
                fuemail=uemail.getText().toString();
                funum=unum.getText().toString();
                fuadress=uadress.getText().toString();
                fulevel=ulevel.getText().toString();
                fudepartment=udepartment.getText().toString();
                fuinstitution=uinstitution.getText().toString();
                fusubj=usubj.getText().toString();
                fureqdetails=ureqdetails.getText().toString();
                try {
                    updateData();
                    Toast.makeText(Edit_Profile.this, "Successfully Updated.", Toast.LENGTH_SHORT).show();
//                    Intent i=new Intent(Edit_Profile.this,Profile.class);
//                    i.putExtra("isother",true);
//                    i.putExtra("userid",UID);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
                    finish();
                }catch (Exception e){
                    Toast.makeText(Edit_Profile.this, "Update Failed.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        findViewById(R.id.Updatepass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Edit_Profile.this,Edit_pass.class);
                startActivity(i);
            }
        });

    }
     void getViews(){
        uname=findViewById(R.id.uname);
        uemail=findViewById(R.id.uemail);
        unum=findViewById(R.id.uphone);
        uadress=findViewById(R.id.uaddress);
        ulevel=findViewById(R.id.ulevel);
        udepartment=findViewById(R.id.udepartment);
        uinstitution=findViewById(R.id.uinstitution);
        usubj=findViewById(R.id.usubject);
        ureqdetails=findViewById(R.id.udetails);
    }
    void setData(){
        if(isStudent){
            uname.setText(studentData.getName());
            uemail.setText(studentData.getEmail());
            unum.setText(studentData.getPhone());
            uadress.setText(studentData.getAddress());
            ulevel.setText(studentData.getLevel());
            udepartment.setText(studentData.getDepartment());
            uinstitution.setText(studentData.getInstitution());
            usubj.setText(studentData.getSubject());
            ureqdetails.setText(studentData.getDetails());
        }else{
            uname.setText(teacherData.getName());
            uemail.setText(teacherData.getEmail());
            unum.setText(teacherData.getPhone());
            uadress.setText(teacherData.getAddress());
            ulevel.setText(teacherData.getLevel());
            udepartment.setText(teacherData.getDepartment());
            uinstitution.setText(teacherData.getInstitution());
            usubj.setText(teacherData.getSubject());
            ureqdetails.setText(teacherData.getDetails());
        }
    }
    void updateData(){
        myRef3.child(UID).child("username").setValue(funame);
        if(isStudent){
            //if(funame.compareTo(studentData.getName())!=0){}
            myRef1.child(UID).child("name").setValue(funame);
            myRef1.child(UID).child("email").setValue(fuemail);
            myRef1.child(UID).child("phone").setValue(funum);
            myRef1.child(UID).child("address").setValue(fuadress);
            myRef1.child(UID).child("level").setValue(fulevel);
            myRef1.child(UID).child("department").setValue(fudepartment);
            myRef1.child(UID).child("institution").setValue(fuinstitution);
            myRef1.child(UID).child("subject").setValue(fusubj);
            myRef1.child(UID).child("details").setValue(fureqdetails);

        }else{
            myRef2.child(UID).child("name").setValue(funame);
            myRef2.child(UID).child("email").setValue(fuemail);
            myRef2.child(UID).child("phone").setValue(funum);
            myRef2.child(UID).child("address").setValue(fuadress);
            myRef2.child(UID).child("level").setValue(fulevel);
            myRef2.child(UID).child("department").setValue(fudepartment);
            myRef2.child(UID).child("institution").setValue(fuinstitution);
            myRef2.child(UID).child("subject").setValue(fusubj);
            myRef2.child(UID).child("details").setValue(fureqdetails);
        }
    }

}