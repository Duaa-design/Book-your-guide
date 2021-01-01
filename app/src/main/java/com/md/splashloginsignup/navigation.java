package com.md.splashloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class navigation extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar ;
    FirebaseAuth mAuth;

    Fragment fragment;
    DatabaseReference uidRef;
    private String currentUserId;



    @Override
    //initialize your activity
    protected void onCreate(Bundle savedInstanceState) {
//The FragmentManager class provides methods that allow you to add, remove, and replace fragments to an activity at runtime
        super.onCreate(savedInstanceState);

        // setContentView means set xml layout to this java file or activity and it will render as the UI of this activity.
        setContentView(R.layout.activity_navigation);


        chipNavigationBar = (ChipNavigationBar)findViewById(R.id.chipNavigationBar);
//  getSupportFragmentManager() Return the FragmentManager for interacting with fragments associated with this activity.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new Search()).commit();

      final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
       mAuth = FirebaseAuth.getInstance();


      Tourist();

        }




      private void Tourist() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                fragment=null;

                   switch (id){

                    case R.id.home:
                       fragment=new Search();

                        break;
                    case R.id.profile:

                         fragment = new touristProfile();


                        break;
                    case R.id.chat:
                        fragment = new ChatsFragment();
                        break;

                    case R.id.request:

                        fragment = new RequestsFragment();
                        break;
                }

                if(fragment!=null){

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment).commit();
            }


            }
        });

    }

    @Override
    protected void onStart() {//when the activity will be visible
        super.onStart();


        FirebaseUser ruser = mAuth.getCurrentUser();

        if (ruser != null) {
            updateUserStatus("online");
        }
    }

    @Override
    protected void onStop() {// the activity still in memory but not visible , the user enter the home button
        super.onStop();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            updateUserStatus("offline");
        }

    }

    @Override
    protected void onDestroy() {// the activite will be shut down and not connected to firebase
        super.onDestroy();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            updateUserStatus("offline");
        }
    }


    private void updateUserStatus(String state) {

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







