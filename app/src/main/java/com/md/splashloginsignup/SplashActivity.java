package com.md.splashloginsignup;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.md.splashloginsignup.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    Button show8;
    Button show9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);


        show8= (Button)findViewById(R.id.btn_login);
        show8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), Accountpage_login.class);
                startActivity(i);
            }
        });

        show9= (Button)findViewById(R.id.btn_get_started);
        show9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j= new Intent(getApplicationContext(), Accoountpage_signup.class);
                startActivity(j);
            }
        });
    }


}
