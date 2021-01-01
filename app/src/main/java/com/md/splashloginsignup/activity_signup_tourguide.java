package com.md.splashloginsignup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class activity_signup_tourguide extends AppCompatActivity {
    Button btn_signuptourg;
    EditText tourg_full_name, tourg_email_address,tourg_password,tourg_phonenumber;

    private FirebaseAuth mAuth1;
    TextView login_label7;



    ImageButton imagebuttonn4;
    String usersId1;
    private DatabaseReference mDatabaseReference;
    ProgressDialog mProgressDialog;
    String email,pass,fullName,phonN;
    String item = "";
    String Tour_Guide = "Tour Guide";
    Map<String, Object> user;
    private String currentUserId;
    Button btnOrder;
    TextView tvItemSelected;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String Status="";

    String userState="";
    String payment="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {//loaded the activity to phone memory


      //  LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup_tourguide);

        imagebuttonn4 = findViewById(R.id.imagebuttonn);

        imagebuttonn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //initialise mProgressDialog
        mProgressDialog  = new ProgressDialog(activity_signup_tourguide.this);
// Write a message to the database
        tourg_full_name = findViewById(R.id.tourg_full_name);
        tourg_email_address = findViewById(R.id.tourg_email_address);
        tourg_password = findViewById(R.id.tourg_password);
        tourg_phonenumber = findViewById(R.id.tourg_phonenumber);
        btn_signuptourg = findViewById(R.id.btn_signuptourg);

// Initialize Firebase Auth
        mAuth1 = FirebaseAuth.getInstance();



        login_label7=findViewById(R.id.login_label7);




        login_label7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Logintourguide.class));
            }
        });




        // select a language code
        btnOrder = (Button) findViewById(R.id.btnOrder);

        tvItemSelected = (TextView) findViewById(R.id.tvItemSelected);

//listItems is array of string
        //getResources() is used to access our application's resources file
        listItems = getResources().getStringArray(R.array.Languages_item);// retrive string array from string file

        checkedItems = new boolean[listItems.length];

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity_signup_tourguide.this);
                mBuilder.setTitle(R.string.dialog_title);
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            mUserItems.add(position);


                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });


                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }

                        tvItemSelected.setText(item);


                    }
                });

                mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            tvItemSelected.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        btn_signuptourg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store the text that user entered to check of it
                fullName = tourg_full_name.getText().toString();
                 email = tourg_email_address.getText().toString().trim();
                pass = tourg_password.getText().toString().trim();
                phonN = tourg_phonenumber.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(activity_signup_tourguide.this, "Please enter your name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(activity_signup_tourguide.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(activity_signup_tourguide.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(phonN)) {
                    Toast.makeText(activity_signup_tourguide.this, "Please enter your number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pass.length() < 8) {
                    Toast.makeText(activity_signup_tourguide.this, "password must be 8 or long", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!email.matches(emailPattern)){
                    Toast.makeText(activity_signup_tourguide.this, "invalid email address", Toast.LENGTH_LONG).show();
                    return;
                }
                // register the user in firebase

                mAuth1.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //show dialog
                            mProgressDialog.show();
                            //set content view
                            mProgressDialog.setContentView(R.layout.progress_dialog);
                            //set transparent background
                            FirebaseUser ruser = mAuth1.getCurrentUser();
                            usersId1 = ruser.getUid();

                            //initialise database Reference for realtime database
                            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

                            mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                          //  Intent intent = new Intent(getApplicationContext(), Uploadfilespage.class);

                            //here we want to store the data by using hash map
                            user = new HashMap<>();
                            //insert the data using put method
                            user.put("Sign_up_as", Tour_Guide);
                            user.put("fullName", fullName); // using tour_full_name to retrive the full name that user entered
                            user.put("email", email);
                            user.put("phoneNumber", phonN);
                            user.put("password", pass);
                            user.put("user ID", usersId1);
                            user.put("Languages", item);
                            user.put("status",Status);
                            user.put("userState",userState);
                            user.put("payment",payment);




                            //now i store and insert the information in the firebase
                            //يخزن البيانات حقت الهاش ماب تحت الاي دي تبع كل يوزر يسجل في التطبيق

                            //OnCompleteListener when a Task completes.
                            mDatabaseReference.child(usersId1).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), Uploadfilespage.class));
                                    }else{
                                        Toast.makeText(activity_signup_tourguide.this,"Error !"+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }

                                }
                            });


                        }else{//if the user already exist
                            Toast.makeText(activity_signup_tourguide.this, "User already exist !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });}});}




}
