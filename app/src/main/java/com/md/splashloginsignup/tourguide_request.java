package com.md.splashloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
//here we want to send request for tour guides
public class tourguide_request extends AppCompatActivity {
    private String reciever_id;
    private CircleImageView profile_image;
    private TextView fall_name_tour,Language,status,tourist_full_name;
   Button requestt,paymentNow;
    private String mcurrentUser;
    private FirebaseAuth mAuth1;

    DatabaseReference ref,chatrequestreff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);// profile
        setContentView(R.layout.activity_tourguide_request);

        mAuth1=FirebaseAuth.getInstance();
        requestt=(Button)findViewById(R.id.requestt);



        reciever_id=getIntent().getStringExtra("visit_user_id");//tour guide_id that tourist clicked on his profile in search page

        chatrequestreff=FirebaseDatabase.getInstance().getReference().child("Chats");

        mcurrentUser=mAuth1.getCurrentUser().getUid();
        profile_image=findViewById(R.id.profile_image);

        fall_name_tour=findViewById(R.id.fall_name_tour);

        Language=findViewById(R.id.Language);
        status=findViewById(R.id.status);
        tourist_full_name=findViewById(R.id.tourist_full_name);



        ref= FirebaseDatabase.getInstance().getReference();


        paymentNow=findViewById(R.id.paymentNow);

        ref.child("Users").child(reciever_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("fullName") && dataSnapshot.hasChild("profile_images"))
                {

                    String retrieveusername=dataSnapshot.child("fullName").getValue().toString();
                    String retrieveuserlanguages=dataSnapshot.child("Languages").getValue().toString();
                    String retrieveuserimage=dataSnapshot.child("profile_images").getValue().toString();
                    String retrieveuserstatus=dataSnapshot.child("status").getValue().toString();

                    fall_name_tour.setText(retrieveusername);
                    Language.setText(retrieveuserlanguages);
                    status.setText(retrieveuserstatus);

                    Picasso.get().load(retrieveuserimage).into(profile_image);
                    tourist_full_name.setText(retrieveusername);

                }
                else if(dataSnapshot.exists() && dataSnapshot.hasChild("fullName"))// here if the tour guide does not have profile picture
                {
                    String retrieveusername=dataSnapshot.child("fullName").getValue().toString();
                    String retrieveuserlanguage=dataSnapshot.child("Languages").getValue().toString();
                    String retrieveuserstatus=dataSnapshot.child("status").getValue().toString();

                    fall_name_tour.setText(retrieveusername);
                    Language.setText(retrieveuserlanguage);
                    status.setText(retrieveuserstatus);
                    tourist_full_name.setText(retrieveusername);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        requestt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileintent1 = new Intent(tourguide_request.this, create_trip.class);
                profileintent1.putExtra("visitt_user_id", reciever_id);

                startActivity(profileintent1);









            }


        });

        ref.child("Users").child(mcurrentUser).child(reciever_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    requestt.setVisibility(View.INVISIBLE);
                    paymentNow.setVisibility(View.VISIBLE);
                    paymentNow.setEnabled(true);
                    paymentNow.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(tourguide_request.this, payment.class);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }}