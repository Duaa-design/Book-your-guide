package com.md.splashloginsignup;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Logintourguide extends AppCompatActivity {
    TextView show7, signup_btn_label;
    ImageButton imagebuttonn1;
    EditText tourg_email_address, tourg_password;
    Button btnn_login;
    private FirebaseAuth mAuth1;
    private DatabaseReference jLoginDatabase;
    private FirebaseDatabase fDatabase;
    ProgressDialog mProgressDialog;
    DatabaseReference uidRef;
    String email, pass;

    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logintourguide);

        //initialise mProgressDialog
        mProgressDialog = new ProgressDialog(Logintourguide.this);
        show7 = (TextView) findViewById(R.id.forgot_password);
        show7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Forgetpassword.class);
                startActivity(i);
            }
        });
        imagebuttonn1 = findViewById(R.id.imagebuttonn);
        imagebuttonn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signup_btn_label = (TextView) findViewById(R.id.signup_btn_label);
        signup_btn_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), activity_signup_tourguide.class);
                startActivity(i);
            }
        });
        tourg_email_address = (EditText) findViewById(R.id.tourg_email_address);
        tourg_password = (EditText) findViewById(R.id.tourg_password);
        btnn_login = (Button) findViewById(R.id.btnn_login);
        mAuth1 = FirebaseAuth.getInstance();
        btnn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                email = tourg_email_address.getText().toString().trim();
                pass = tourg_password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Logintourguide.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(Logintourguide.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    return;
                }


                if (pass.length() < 8) {
                    Toast.makeText(Logintourguide.this, "password must be 8 or long", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!email.matches(emailPattern)) {
                    Toast.makeText(Logintourguide.this, "invalid email address", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth1.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(Logintourguide.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                //show dialog
                                mProgressDialog.show();
                                //set content view
                                mProgressDialog.setContentView(R.layout.progress_dialog);
                                //set transparent background
                                mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                ///FirebaseDatabase database = FirebaseDatabase.getInstance();

                                //  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                //  DatabaseReference rootRef = FirebaseDatabase.getInstance()
                                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                uidRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                // final Query checkUser = uidRef.orderByChild("email").equalTo(email);
                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.child("email").getValue().equals(email)) {
                                            passwordCheck();

                                        } else {

                                            mProgressDialog.dismiss();
                                            Toast.makeText(Logintourguide.this, "no such user exist !",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.d("Tag", databaseError.getMessage());
                                    }
                                };
                              uidRef.addListenerForSingleValueEvent(valueEventListener);


                            }
                        });
            }
        });
    }

    public void passwordCheck() {
        // final Query checkUserpasswor = uidRef.orderByChild("password");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("password").getValue().equals(pass)) {
                    if (dataSnapshot.child("Sign_up_as").getValue().equals("Tour Guide")) {


                        //put it iin that intent so we can pass it to tourguide class


                        startActivity(new Intent(getApplicationContext(), navigation_tourGuide.class));

                        Toast.makeText(Logintourguide.this, "welcome back tour guide!",
                                Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();


                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(Logintourguide.this, "Sorry ! You are not authorized to access this application!",
                                Toast.LENGTH_SHORT).show();


                    }
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(Logintourguide.this, "wrong password",
                            Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Tag", error.getMessage());
            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);

    }




}




