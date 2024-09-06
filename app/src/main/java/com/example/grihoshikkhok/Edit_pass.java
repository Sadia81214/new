package com.example.grihoshikkhok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Edit_pass extends AppCompatActivity {
    EditText op,np,cnp;
    Button uppass;
    String UID;
    boolean isStudent=false;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference infoRef=database.getReference("Info");
    DatabaseReference myRef1 = database.getReference("Student");
    DatabaseReference myRef2 = database.getReference("Teacher");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);
        UID=mAuth.getCurrentUser().getUid();
        op=findViewById(R.id.oldpass);
        np=findViewById(R.id.newpass);
        cnp=findViewById(R.id.cfnewpass);
        uppass=findViewById(R.id.uppassbtn);
        uppass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opas,npas,cnpas;
                opas=op.getText().toString();
                npas=np.getText().toString();
                cnpas=cnp.getText().toString();
                //Update work here
                infoRef.child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot dataSnapshot=task.getResult();
                            ///Assign to class var
                            String userType=String.valueOf(dataSnapshot.child("userType").getValue());
                            if(userType.compareTo("Student")==0){
                                isStudent=true;
                            }
                            String currentpass=String.valueOf(dataSnapshot.child("userPassword").getValue());
                            if(currentpass.compareTo(opas)==0){
                                if (npas.compareTo(cnpas)==0 && npas.isEmpty()==false && cnpas.isEmpty()==false){
                                    try {
                                        //Main
                                        infoRef.child(UID).child("userPassword").setValue(npas);
                                        if(isStudent){
                                            myRef1.child(UID).child("password").setValue(npas);
                                        }else{
                                            myRef2.child(UID).child("password").setValue(npas);
                                        }
                                        //After
                                        Toast.makeText(Edit_pass.this, "Successfully Updated.", Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(Edit_pass.this,Edit_Profile.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                    }catch (Exception e){
                                        Toast.makeText(Edit_pass.this, "Update Failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(Edit_pass.this, "Please enter confirm password correctly", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(Edit_pass.this, "Wrong Old password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }
}