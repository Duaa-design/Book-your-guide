package com.md.splashloginsignup;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
public class Accoountpage_signup extends AppCompatActivity {
    Button show1;
    Button show2;
    @Override
    //initialize your activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView means set xml layout to this java file or activity and it will render as the UI of this activity.
        setContentView(R.layout.activity_accoountpage_signup);
//R is a Class in android that are having the id’s of all the view’s.
        show2= (Button)findViewById(R.id.button5);//we can access that element using findViewById and now the variable click will point to that button in our xml whose id is button5.


        show2.setOnClickListener(new View.OnClickListener() {//here we want to handle a click event on that button
            @Override
            public void onClick(View view) {
                //object that provides runtime binding between separate components, such as two activities

                Intent i3= new Intent(getApplicationContext(), SignupActivity.class);//code to do open this class when user click the button
                startActivity(i3);
            }
        });

        show1= (Button)findViewById(R.id.button9);//when click in sign up as tour guide


        show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i4= new Intent(getApplicationContext(), activity_signup_tourguide.class);
                startActivity(i4);
            }
        });
    }
}