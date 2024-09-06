package com.example.grihoshikkhok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grihoshikkhok.models.StudentData;
import com.example.grihoshikkhok.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class TeacherHome extends AppCompatActivity {
    TextView username;
    ImageView propic;
    RecyclerView recyclerView;
    List<StudentData> mStudent,oStudent = new ArrayList<>();
    StudentAdapter studentAdapter;
    AppCompatButton  setfilter, reset,searcharea;
//    LinearLayout filterlayout;
    CheckBox rad1,rad2,rad3,rad4,rad5,rad6;
    FirebaseUser mAuth= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userref = database.getReference("Users");
    LinearLayout gotoprofile;
    ImageButton inbox,searchdrop;
    LinearLayout searchlay;
    ImageButton resetsearch;
    LinearLayout resetsearchlay;
    RelativeLayout homebar;

    boolean math = false,physics = false , ict = false, eng = false , bengali = false, chem = false;

    boolean filterstate = false,searchstate = false;

    private static final String[] COUNTRIES = new String[] {
            "Dhaka", "Chittagong", "Rajshahi", "Sylhet", "Khulna", "Barisal"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        filter = findViewById(R.id.filterbutton);
//        filterlayout = findViewById(R.id.filterlayout);
        setfilter = findViewById(R.id.setfilter);
        rad1 = findViewById(R.id.rad1);
        rad2 = findViewById(R.id.rad2);
        rad3 = findViewById(R.id.rad3);
        rad4 = findViewById(R.id.rad4);
        rad5 = findViewById(R.id.rad5);
        rad6 = findViewById(R.id.rad6);
        inbox = findViewById(R.id.inbox);
        username = findViewById(R.id.username);
        propic = findViewById(R.id.propic);
//        reset = findViewById(R.id.resetbutton);
        gotoprofile = findViewById(R.id.gotoprofile);
        propic = findViewById(R.id.propic);
//        searcharea = findViewById(R.id.searchAreaButton);
        searchdrop = findViewById(R.id.searchdrop);
        searchlay = findViewById(R.id.searchlay);
        resetsearch = findViewById(R.id.resetsearch);
        homebar = findViewById(R.id.homebar);
        resetsearchlay = findViewById(R.id.resetsearchlay);
        homebar = findViewById(R.id.homebar);

        String uID=mAuth.getUid();
//        gotoprofile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(TeacherHome.this,Edit_Profile.class);
////                intent.putExtra("userid",students.getId());
//                startActivity(intent);
//            }
//        });


        userref.child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    username.setText(user.getUsername());
                } else {
                    // Handle the case where user data is not available
                    username.setText("Teacher");
                    // Log this event or handle it as appropriate for your app
                    Log.w("TeacherHome", "User data is null for UID: " + uID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error case
                Log.e("TeacherHome", "Failed to load user data: " + error.getMessage());
                Toast.makeText(TeacherHome.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView areafield = (AutoCompleteTextView)
                findViewById(R.id.searchbyarea);
        areafield.setAdapter(adapter);

        searchdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchstate){
                    searchlay.setVisibility(View.VISIBLE);
                    searchdrop.setImageResource(R.drawable.ic_baseline_search_off_24);
                    searchstate=true;
                }else {
                    searchlay.setVisibility(View.GONE);
                    searchdrop.setImageResource(R.drawable.ic_baseline_search_24);
                    searchstate=false;
                }
            }
        });


        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeacherHome.this,ChatActivity.class);
                startActivity(intent);
            }
        });

//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mStudent = oStudent;
//                studentAdapter = new StudentAdapter(TeacherHome.this,mStudent);
//                recyclerView.setAdapter(studentAdapter);
//                reset.setVisibility(View.GONE);
//            }
//        });

        resetsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudent = oStudent;
                studentAdapter = new StudentAdapter(TeacherHome.this,mStudent);
                recyclerView.setAdapter(studentAdapter);
//                reset.setVisibility(View.GONE);
                homebar.setVisibility(View.VISIBLE);
                resetsearchlay.setVisibility(View.GONE);
                searchdrop.setImageResource(R.drawable.ic_baseline_search_24);
            }
        });

//        mStudent = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            mStudent.add(new StudentData(
//                    "adasadssddsa","sadsadasda","daasdddad","dasdsasdasd","dsadsasadas","daddasdasdsada",
//                    "adasadssddsa","sadsadasda","daasdddad","dasdsasdasd","dsadsasadas","bgH7taQI1mT7CRhK4H9KFbjZ7Uk2",
//                    true,"id"
//            ));
//        }
//        oStudent = mStudent;
//        studentAdapter = new StudentAdapter(TeacherHome.this,mStudent);
//        recyclerView.setAdapter(studentAdapter);

        String ref="images/"+uID+".jpg";
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(ref);
        final long TWO_MEGABYTE = 2048 * 2048*15;
        mImageRef.getBytes(TWO_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        propic.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failed......................");
                    }
                });

//        filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!filterstate){
//                    filterlayout.setVisibility(View.VISIBLE);
//                    filterstate = true;
//                }else{
//                    filterlayout.setVisibility(View.GONE);
//                    filterstate = false;
//                }
//            }
//        });

        gotoprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TeacherHome.this);
                View bottomsheetview = LayoutInflater.from(getApplicationContext()).inflate((R.layout.bottom_sheet_layout),
                        findViewById(R.id.bottomsheetcontainer));
                bottomsheetview.findViewById(R.id.editprofile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(TeacherHome.this, Profile.class);
                        intent.putExtra("userid",uID);
                        intent.putExtra("isother",true);
                        startActivity(intent);
                    }
                });
//                bottomsheetview.findViewById(R.id.archivedshops).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bottomSheetDialog.dismiss();
//                        Intent intent = new Intent(StudentHome.this, ArchiveActivity.class);
//                        startActivity(intent);
//                    }
//                });
                bottomsheetview.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(TeacherHome.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
                bottomSheetDialog.setContentView(bottomsheetview);
                bottomSheetDialog.show();
            }
        });


        setfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ict = rad3.isChecked();
                physics = rad2.isChecked();
                math = rad1.isChecked();
//                Toast.makeText(StudentHome.this,math+" "+physics+" "+ict,Toast.LENGTH_SHORT).show();
                List<StudentData> tempStudent = new ArrayList<>();
                mStudent=oStudent;
                for (StudentData student:mStudent
                ) {
                    if( student.getAddress().toLowerCase().contains(areafield.getText().toString().toLowerCase())){
                        if(student.getSubject().toLowerCase().contains("physics") && rad1.isChecked()){
                            tempStudent.add(student);
                        }
                        else if(student.getSubject().toLowerCase().contains("math") && rad2.isChecked()){
                            tempStudent.add(student);
                        }
                        else if(student.getSubject().toLowerCase().contains("ict") && rad3.isChecked()){
                            tempStudent.add(student);
                        }
                        else if(student.getSubject().toLowerCase().contains("english") && rad4.isChecked()){
                            tempStudent.add(student);
                        }
                        else if(student.getSubject().toLowerCase().contains("bengali") && rad5.isChecked()){
                            tempStudent.add(student);
                        }
                        else if(student.getSubject().toLowerCase().contains("chemistry") && rad6.isChecked()){
                            tempStudent.add(student);
                        }else if(!rad1.isChecked() && !rad2.isChecked() && !rad3.isChecked() && !rad4.isChecked() && !rad5.isChecked() && !rad6.isChecked()){
                            tempStudent.add(student);
                        }
                    }



                }
                if (tempStudent.isEmpty()){
                    Toast.makeText(TeacherHome.this, "No result found", Toast.LENGTH_SHORT).show();
                }
                mStudent = tempStudent;
                studentAdapter = new StudentAdapter(TeacherHome.this,mStudent);
                recyclerView.setAdapter(studentAdapter);
                resetsearchlay.setVisibility(View.VISIBLE);
                searchlay.setVisibility(View.GONE);
                homebar.setVisibility(View.GONE);
                resetsearch.setVisibility(View.VISIBLE);
            }
        });


//        searcharea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                List<StudentData> tempStudent = new ArrayList<>();
//                mStudent=oStudent;
//                for (StudentData student:mStudent
//                ) {
//                    if(student.getAddress().toLowerCase().contains(areafield.getText().toString().toLowerCase())){
//
//                            tempStudent.add(student);
//
//                    }
//                }
//                if (tempStudent.isEmpty()){
//                    Toast.makeText(TeacherHome.this, "No result found", Toast.LENGTH_SHORT).show();
//                }
//                mStudent = tempStudent;
//                studentAdapter = new StudentAdapter(TeacherHome.this,mStudent);
//                recyclerView.setAdapter(studentAdapter);
//                homebar.setVisibility(View.GONE);
//                resetsearch.setVisibility(View.VISIBLE);
//                searchlay.setVisibility(View.GONE);
//                resetsearchlay.setVisibility(View.VISIBLE);
//                reset.setVisibility(View.VISIBLE);
//            }
//        });

        mStudent = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mStudent.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    StudentData studentData = snapshot.getValue(StudentData.class);
                    mStudent.add(studentData);
                }
                oStudent = mStudent;
                studentAdapter = new StudentAdapter(TeacherHome.this,mStudent);
                recyclerView.setAdapter(studentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}