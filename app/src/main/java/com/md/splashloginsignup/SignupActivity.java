package com.md.splashloginsignup;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.core.view.LayoutInflaterCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    TextView login_label1;
    ImageButton imagebuttonn;
    Button btn_signup;
    EditText tour_full_name, tour_email_address, tour_password, tour_phonenumber;
    private FirebaseAuth mAuth;
    String usersId;
    String Tourist = "Tourist";
    FirebaseFirestore fstore;
    String status="Tourist";
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String currentUserId;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);


        login_label1=findViewById(R.id.login_label1);
        imagebuttonn = findViewById(R.id.imagebuttonn);
        imagebuttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //initialise mProgressDialog
        mProgressDialog  = new ProgressDialog(SignupActivity.this);
// Write a message to the database
        tour_full_name = (EditText) findViewById(R.id.tourG_full_name);
        tour_email_address = (EditText) findViewById(R.id.tour_email_address);
        tour_password = (EditText) findViewById(R.id.tourg_password);
        tour_phonenumber = (EditText) findViewById(R.id.tour_phonenumber);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();




        login_label1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullName = tour_full_name.getText().toString();
                final String email = tour_email_address.getText().toString().trim();
                final String pass = tour_password.getText().toString().trim();
                final String phonN = tour_phonenumber.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(SignupActivity.this, "Please enter your name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(SignupActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(phonN)) {
                    Toast.makeText(SignupActivity.this, "Please enter your number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pass.length() < 8) {
                    Toast.makeText(SignupActivity.this, "password must be 8 or long", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!email.matches(emailPattern)){
                    Toast.makeText(SignupActivity.this, "invalid email address", Toast.LENGTH_LONG).show();
                    return;
                }
                // register the user in firebase
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //show dialog
                            mProgressDialog.show();
                            //set content view
                            mProgressDialog.setContentView(R.layout.progress_dialog);
                            //set transparent background
                            mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                            FirebaseUser ruser = mAuth.getCurrentUser();
                            usersId = ruser.getUid();
                            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(usersId);

                            Map<String, Object> user = new HashMap<>();
                            //insert the data using put method
                            user.put("Sign_up_as",Tourist);
                            user.put("fullName",fullName); // using tour_full_name to retrive the full name that user entered
                            user.put("email",email);
                            user.put("phoneNumber",phonN);
                            user.put("password",pass);
                            user.put("user ID",usersId);
                            user.put("status",status);

                            Toast.makeText(SignupActivity.this,"User created succsesfully !",Toast.LENGTH_LONG).show();

                            //now i store and insert the information in the firebase
                            mDatabaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), navigation.class));
                                    }else{
                                        Toast.makeText(SignupActivity.this,"Error !"+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }

                                }
                            });



                        }
                    }
                });}});}





    }


