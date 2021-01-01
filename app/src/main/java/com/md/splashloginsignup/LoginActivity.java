package com.md.splashloginsignup;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.core.view.LayoutInflaterCompat;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.FirebaseFirestore;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    TextView show5;
    TextView show6;
    ImageButton imagebuttonn2;
    EditText tour_email_address,tour_password;
    Button btn_login;
    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    ProgressDialog mProgressDialog;
    DatabaseReference uidRef;
   String email1 ,pass1;
    private String currentUserId;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference jLoginDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

      //  LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //initialise mProgressDialog
       mProgressDialog= new ProgressDialog(LoginActivity.this);
        show5= (TextView)findViewById(R.id.forgot_password);
        show5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Forgetpassword.class);
                startActivity(i);
            }
        });

        show6= (TextView)findViewById(R.id.signup_label4);
        show6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
            }
        });

        tour_email_address=(EditText) findViewById(R.id.tour_email_address);
        tour_password=(EditText) findViewById(R.id.tour_password);
        imagebuttonn2 = (ImageButton)findViewById(R.id.imagebuttonn);
        btn_login = (Button) findViewById(R.id.btn_login);


         mAuth = FirebaseAuth.getInstance();

        imagebuttonn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               email1 = tour_email_address.getText().toString().trim();
               pass1 = tour_password.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (TextUtils.isEmpty(email1)) {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(pass1)) {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    return;
                }


                if (pass1.length() < 8) {
                    Toast.makeText(LoginActivity.this, "password must be 8 or long", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!email1.matches(emailPattern)){
                    Toast.makeText(LoginActivity.this, "invalid email address", Toast.LENGTH_LONG).show();
                    return;
                }


                //authenticate user
                mAuth.signInWithEmailAndPassword(email1, pass1)//OnCompleteListener when a Task completes.
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override

                            public void onComplete(@NonNull Task<AuthResult> task) {

                                    //show dialog
                                    mProgressDialog.show();
                                    //set content view
                                    mProgressDialog.setContentView(R.layout.progress_dialog);
                                    //set transparent background
                                    mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    ///FirebaseDatabase database = FirebaseDatabase.getInstance();

                                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                uidRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                                    // final Query checkUser = uidRef.orderByChild("email").equalTo(email);


                              //A ValueEventListener listens for data changes to a specific location in our database
                                    ValueEventListener valueEventListener = new ValueEventListener() {
                                        @Override
                                        // onDataChange to read a static snapshot of the contents at a given path
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("email").getValue().equals(email1)) {
                                                passwordCheck();

                                            }

                                            else {

                                                mProgressDialog.dismiss();
                                                Toast.makeText(LoginActivity.this, "no such user exist !",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.d("Tag", databaseError.getMessage());
                                        }
                                    };
                                    uidRef.addListenerForSingleValueEvent(valueEventListener);

                                }});}});}

        public void passwordCheck(){
            // final Query checkUserpasswor = uidRef.orderByChild("password");

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child("password").getValue().equals(pass1)) {
                        if (dataSnapshot.child("Sign_up_as").getValue().equals("Tourist")) {






                            Toast.makeText(LoginActivity.this, "welcome back Tourist !",
                                    Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), navigation.class));

                        }else {
                            mProgressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Sorry ! You are not authorized to access this application!",
                                    Toast.LENGTH_SHORT).show();


                        }
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "wrong password",
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
