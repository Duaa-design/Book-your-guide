package com.md.splashloginsignup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class navigation_tourGuide extends AppCompatActivity {
    ChipNavigationBar chipNav ;
    FirebaseAuth mAuth;
    DatabaseReference refrence;
    FirebaseDatabase database;
    Fragment fragment;
    DatabaseReference uidRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

       // final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setContentView(R.layout.activity_navigation_tout_guide);
        chipNav = (ChipNavigationBar) findViewById(R.id.chipNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentt_container,new TourGuide_Profile()).commit();
        TourGuidee();
        mAuth = FirebaseAuth.getInstance();

    }
    private void TourGuidee() {
        chipNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                fragment=null;


                switch (id){
                    case R.id.profilee:
                        fragment = new TourGuide_Profile();
                       // fragment=new Search();

                        break;
                    case R.id.requestt:
                        fragment =new RequestsFragment();


                        break;
                     case R.id.chatt:
                        fragment = new ChatsFragment();
                        break;
                }
                if(fragment!=null){

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentt_container,fragment).commit();
                }


            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser ruser = mAuth.getCurrentUser();



        if (ruser != null) {
            updateUserStatus("online");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();


        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            updateUserStatus("offline");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            updateUserStatus("offline");
        }
    }

    public void updateUserStatus(String state) {
        String savecurrentTime, savecurrentDate;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        savecurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        savecurrentTime = currentTime.format(calendar.getTime());




        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", savecurrentTime);
        hashMap.put("date", savecurrentDate);
        hashMap.put("state", state);
        currentUserId = mAuth.getUid();
        uidRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);


        uidRef.child("userState").setValue(hashMap);

    }




}

