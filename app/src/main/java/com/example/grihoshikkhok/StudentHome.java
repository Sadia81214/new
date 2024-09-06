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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.grihoshikkhok.models.TeacherData;
import com.example.grihoshikkhok.models.User;

import java.util.ArrayList;
import java.util.List;

public class StudentHome extends AppCompatActivity {
    TextView username;
    RecyclerView recyclerView;
    List<TeacherData> mTeacher,oteacher = new ArrayList<>();
    TeacherAdapter teacherAdapter;
    AppCompatButton  setfilter,search;
    ImageButton inbox,searchdrop,resetsearch;
    LinearLayout resetsearchlay;
    CheckBox rad1,rad2,rad3,rad4,rad5,rad6;
    EditText teachersearch;
    LinearLayout gotoprofile, searchlay;
    ImageView propic;
    RelativeLayout homebar;


    FirebaseUser mAuth= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userref = database.getReference("Users");

    boolean math = false,physics = false , ict = false, eng = false , bengali = false, chem = false;

    boolean filterstate = false, searchstate = false;

    private static final String[] COUNTRIES = new String[] {
            "Dhaka", "Chittagong", "Rajshahi", "Sylhet", "Khulna", "Barisal"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
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
        teachersearch = findViewById(R.id.teachersearchbox);
//        search = findViewById(R.id.searchTeacherButton);
//        reset = findViewById(R.id.resetbutton);
        gotoprofile = findViewById(R.id.gotoprofile);
        propic = findViewById(R.id.propic);
//        searcharea = findViewById(R.id.searchAreaButton);
        searchdrop = findViewById(R.id.searchdrop);
        searchlay = findViewById(R.id.searchlay);
        resetsearch = findViewById(R.id.resetsearch);
        homebar = findViewById(R.id.homebar);
        resetsearchlay = findViewById(R.id.resetsearchlay);

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


        String uID=mAuth.getUid();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView areafield = (AutoCompleteTextView)
                findViewById(R.id.searchbyarea);
        areafield.setAdapter(adapter);

//        userref.child(uID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful()){
//                    DataSnapshot dataSnapshot=task.getResult();
//                    ///Assign to class var
////                    String usernamew =String.valueOf(dataSnapshot.child("username").getValue());
//                    User user = dataSnapshot.getValue(User.class);
//                    assert user != null;
//                    username.setText(user.getUsername());
//
//                }
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
                    username.setText("Student");
                    // You might want to log this or handle it in some other way
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error case
                Toast.makeText(StudentHome.this, "Failed to load user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentHome.this,ChatActivity.class);
                startActivity(intent);
            }
        });
//
//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTeacher = oteacher;
//                teacherAdapter = new TeacherAdapter(StudentHome.this,mTeacher);
//                recyclerView.setAdapter(teacherAdapter);
//                reset.setVisibility(View.GONE);
//            }
//        });

        resetsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeacher = oteacher;
                teacherAdapter = new TeacherAdapter(StudentHome.this,mTeacher);
                recyclerView.setAdapter(teacherAdapter);
                homebar.setVisibility(View.VISIBLE);
//                reset.setVisibility(View.GONE);
                resetsearchlay.setVisibility(View.GONE);
                searchdrop.setImageResource(R.drawable.ic_baseline_search_24);
            }
        });
//
//        mTeacher = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            mTeacher.add(new TeacherData(
//                    "adasadssddsa","sadsadasda","daasdddad","dasdsasdasd","dsadsasadas","daddasdasdsada",
//                    "adasadssddsa","sadsadasda","daasdddad","dasdsasdasd","dsadsasadas","bgH7taQI1mT7CRhK4H9KFbjZ7Uk2","daddasdasdsada","daddasdasdsada","daddasdasdsada","daddasdasdsada",
//                    true,"id"
//            ));
//        }
//        oteacher = mTeacher;
//        teacherAdapter = new TeacherAdapter(StudentHome.this,mTeacher);
//        recyclerView.setAdapter(teacherAdapter);
//
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
//
//            }
//        });
//
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<TeacherData> tempTeacher = new ArrayList<>();
//                mTeacher=oteacher;
//                for (TeacherData teacher:mTeacher
//                ) {
//                    if(teacher.getName().toLowerCase().contains(teachersearch.getText().toString().toLowerCase())){
//                        tempTeacher.add(teacher);
//                    }
//                }
//                if (tempTeacher.isEmpty()){
//                    Toast.makeText(StudentHome.this, "No result found", Toast.LENGTH_SHORT).show();
//                }
//                mTeacher = tempTeacher;
//                teacherAdapter = new TeacherAdapter(StudentHome.this,mTeacher);
//                recyclerView.setAdapter(teacherAdapter);
//                homebar.setVisibility(View.GONE);
//                resetsearch.setVisibility(View.VISIBLE);
//                searchlay.setVisibility(View.GONE);
//                resetsearchlay.setVisibility(View.VISIBLE);
////                reset.setVisibility(View.VISIBLE);
//            }
//        });
//
//        searcharea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<TeacherData> tempTeacher = new ArrayList<>();
//                mTeacher=oteacher;
//                for (TeacherData teacher:mTeacher
//                ) {
//                    if(teacher.getAddress().toLowerCase().contains(areafield.getText().toString().toLowerCase())){
//                        tempTeacher.add(teacher);
//                    }
//                }
//                if (tempTeacher.isEmpty()){
//                    Toast.makeText(StudentHome.this, "No result found", Toast.LENGTH_SHORT).show();
//                }
//                mTeacher = tempTeacher;
//                teacherAdapter = new TeacherAdapter(StudentHome.this,mTeacher);
//                recyclerView.setAdapter(teacherAdapter);
//                resetsearchlay.setVisibility(View.VISIBLE);
//                searchlay.setVisibility(View.GONE);
//                homebar.setVisibility(View.GONE);
//                resetsearch.setVisibility(View.VISIBLE);
////                reset.setVisibility(View.VISIBLE);
//            }
//        });
//
//
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



        setfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ict = rad3.isChecked();
                physics = rad2.isChecked();
                math = rad1.isChecked();
//                Toast.makeText(StudentHome.this,math+" "+physics+" "+ict,Toast.LENGTH_SHORT).show();
                List<TeacherData> tempTeacher = new ArrayList<>();
                mTeacher=oteacher;
                for (TeacherData teacher:mTeacher
                     ) {
                    if(teacher.getName().toLowerCase().contains(teachersearch.getText().toString().toLowerCase()) && teacher.getAddress().toLowerCase().contains(areafield.getText().toString().toLowerCase())){
                        if(teacher.getSubject().toLowerCase().contains("physics") && rad1.isChecked()){
                            tempTeacher.add(teacher);
                        }
                        else if(teacher.getSubject().toLowerCase().contains("math") && rad2.isChecked()){
                            tempTeacher.add(teacher);
                        }
                        else if(teacher.getSubject().toLowerCase().contains("ict") && rad3.isChecked()){
                            tempTeacher.add(teacher);
                        }
                        else if(teacher.getSubject().toLowerCase().contains("english") && rad4.isChecked()){
                            tempTeacher.add(teacher);
                        }
                        else if(teacher.getSubject().toLowerCase().contains("bengali") && rad5.isChecked()){
                            tempTeacher.add(teacher);
                        }
                        else if(teacher.getSubject().toLowerCase().contains("chemistry") && rad6.isChecked()){
                            tempTeacher.add(teacher);
                        }else if(!rad1.isChecked() && !rad2.isChecked() && !rad3.isChecked() && !rad4.isChecked() && !rad5.isChecked() && !rad6.isChecked()){
                            tempTeacher.add(teacher);
                        }
                    }


                }
                if (tempTeacher.isEmpty()){
                    Toast.makeText(StudentHome.this, "No result found", Toast.LENGTH_SHORT).show();
                }
                mTeacher = tempTeacher;
                teacherAdapter = new TeacherAdapter(StudentHome.this,mTeacher);
                recyclerView.setAdapter(teacherAdapter);
               resetsearchlay.setVisibility(View.VISIBLE);
                searchlay.setVisibility(View.GONE);
                homebar.setVisibility(View.GONE);
                resetsearch.setVisibility(View.VISIBLE);
            }
        });

        gotoprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StudentHome.this);
                View bottomsheetview = LayoutInflater.from(getApplicationContext()).inflate((R.layout.bottom_sheet_layout),
                        findViewById(R.id.bottomsheetcontainer));
                bottomsheetview.findViewById(R.id.editprofile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        Intent intent = new Intent(StudentHome.this, Profile.class);
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
                        Intent intent = new Intent(StudentHome.this, LoginActivity.class);
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

        mTeacher = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teacher");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTeacher.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    TeacherData teacherData = snapshot.getValue(TeacherData.class);
                    mTeacher.add(teacherData);
                }
                oteacher = mTeacher;
                teacherAdapter = new TeacherAdapter(StudentHome.this,mTeacher);
                recyclerView.setAdapter(teacherAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}