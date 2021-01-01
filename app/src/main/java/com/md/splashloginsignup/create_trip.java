package com.md.splashloginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class create_trip extends AppCompatActivity {
    private TextView selectdateto,selectdateForm,ItemSelected;

    Spinner selectnumber;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener1;
    private FirebaseAuth mAuth1;
    String date1;
    String date;
    String valueFromSpinner;
    private Button request_button,decline_button;
    String current_state,tourguideId;
    private String mcurrentUser;
    private DatabaseReference mDatabaseReference,chatrequestreff,request;
    private String reciever_id;
ImageButton buttonnn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        selectdateto = (TextView) findViewById(R.id.selectdateto);
        selectdateForm = (TextView) findViewById(R.id.selectdateForm);
        ItemSelected = (TextView) findViewById(R.id.ItemSelected);
        buttonnn=(ImageButton) findViewById(R.id.buttonnn);

        mAuth1=FirebaseAuth.getInstance();
        mcurrentUser=mAuth1.getCurrentUser().getUid();
        request_button=findViewById(R.id.send_message_request_button);
        decline_button=findViewById(R.id.decline_message_request_button);

        current_state="new";


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        reciever_id=getIntent().getStringExtra("visitt_user_id");// from tourguide_request class

        request= FirebaseDatabase.getInstance().getReference().child("Chats");


        chatrequestreff=FirebaseDatabase.getInstance().getReference().child("Chat Requests");


        buttonnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

   //*********** send request *************
        chatrequestreff.child(mcurrentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(reciever_id))
                {
                    String reuest_type=dataSnapshot.child(reciever_id).child("request_type").getValue().toString();

                    if(reuest_type.equals("sent"))//this will check if the tourist already clicked in send request to tour guide in his profile or not , so when he close his profile
                    //will save the result"cancel chat request" until the tour guide response
                    {
                        current_state="request_sent";

                        request_button.setText("  Cancel Chat Request  ");

                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_button.setEnabled(false);

                if(current_state.equals("new"))
                {
                    SendChatRequest();
                }
                if(current_state.equals("request_sent"))
                {
                    CancelChatRequest();
                }

            }
        });















        selectdateForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(create_trip.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;//month start from 1 , jan
                Log.d("TAG", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                 date1 = month + "/" + day + "/" + year;
             selectdateForm.setText(date1);
            }
        };


        selectdateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(create_trip.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener1, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("TAG", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                 date = month + "/" + day + "/" + year;
                selectdateto.setText(date);





            }
        };


        selectnumber = (Spinner) findViewById(R.id.selectnumber);
        selectnumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getId() == R.id.selectnumber) {
                    valueFromSpinner = adapterView.getItemAtPosition(i).toString();
                    ItemSelected.setText(valueFromSpinner);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String[] textSizes = getResources().getStringArray(R.array.numbers_item);
        //ArrayAdapter used to supply the spinner with the array that we pre-determined in string file
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, textSizes);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectnumber.setAdapter(adapter);

    }

    private void CancelChatRequest() {// when the tourist want to cancel the request and click cancel , chat request will be deleted in


        chatrequestreff.child(mcurrentUser).child(reciever_id)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            chatrequestreff.child(reciever_id).child(mcurrentUser)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                mDatabaseReference.child(mcurrentUser).child(reciever_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        request_button.setEnabled(true);
                                                        request_button.setText("  Send Request  ");
                                                        current_state="new";

                                                        decline_button.setVisibility(View.INVISIBLE);
                                                        decline_button.setEnabled(false);
                                                    }
                                                });

                                            }
                                        }
                                    });
                        }
                    }
                });


    }


    private void SendChatRequest() {

        chatrequestreff.child(mcurrentUser).child(reciever_id).child("request_type")
                .setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            chatrequestreff.child(reciever_id).child(mcurrentUser)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                request_button.setEnabled(true);
                                                current_state="request_sent";
                                                request_button.setText("  Cancel Chat Request  ");

                                                Map<String, String> user = new HashMap<>();

                                                user.put("date_from",date1);
                                                user.put("date_to",date);
                                                user.put("number_of_people",valueFromSpinner);


                                                mDatabaseReference.child(mcurrentUser).child(reciever_id).child("request_details").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Toast.makeText(create_trip.this, "Data saved", Toast.LENGTH_LONG).show();
                                                        }else{

                                                        }

                                                    }
                                                });


                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(create_trip.this,"Failed sending Request",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}



