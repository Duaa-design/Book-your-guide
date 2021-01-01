package com.md.splashloginsignup;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
public class Accountpage_login extends AppCompatActivity {
    Button show3;
    Button show4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountpage_login);

        show3= (Button)findViewById(R.id.button2);// when click in login as a tourist
        show3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i1);
            }
        });

        show4= (Button)findViewById(R.id.button4);//when click in login as  tour guide
        show4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2= new Intent(getApplicationContext(), Logintourguide.class);
                startActivity(i2);
            }
        });
    }
}