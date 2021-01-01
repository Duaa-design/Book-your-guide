package com.md.splashloginsignup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.core.view.LayoutInflaterCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;

public class Forgetpassword extends AppCompatActivity {

    ImageButton imagebuttonn3;
    Button button;
     FirebaseAuth fAuth;
     EditText tour_email_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgetpassword);

        imagebuttonn3 = findViewById(R.id.imagebuttonn);
        imagebuttonn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button = (Button) findViewById(R.id.button);
         tour_email_address = (EditText) findViewById(R.id.tour_email_address);



        fAuth= FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                final String email = tour_email_address.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Forgetpassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {//CHEAK if the status if the operation success or not

                     if(task.isSuccessful()){

                       //  FirebaseUser ruser = fAuth.getCurrentUser();

                      //   ruser.updatePassword(email);
                         Toast.makeText(Forgetpassword.this, "password sent to your email",Toast.LENGTH_LONG).show();
                     } else{
                         Toast.makeText(Forgetpassword.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                     }
                    }
                });

            }
        });
    }

}