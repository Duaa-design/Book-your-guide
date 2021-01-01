package com.md.splashloginsignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private String messageRecieverId,getMessageRecievername,messagereceiverimage,messageSenderId;
    private TextView username,userlastseen;
    private CircularImageView userprofile;
    private Toolbar chattoolbar;
    private ImageButton sendMessageButton;
    private EditText messagesentinput;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef,Rref;

    private final List<Messages> messagesList=new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView usermessagerecyclerview;
ImageButton backk;

    private String savecurrentTime,savecurrentDate;
    private String checker="",myUrl="";
    private StorageTask uploadTask;
    private Uri fileuri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);//chat page

        mAuth=FirebaseAuth.getInstance();
        messageSenderId=mAuth.getCurrentUser().getUid();

        RootRef= FirebaseDatabase.getInstance().getReference();
        Rref= FirebaseDatabase.getInstance().getReference().child("Users").child(messageSenderId);


    //get the information from chat fragment activity
        messageRecieverId=getIntent().getExtras().get("visit_user_id").toString();
        getMessageRecievername=getIntent().getExtras().get("visit_user_name").toString();
        messagereceiverimage=getIntent().getExtras().get("visit_image").toString();



        chattoolbar=findViewById(R.id.chat_toolbar);//chat tool bar

        setSupportActionBar(chattoolbar);
        ActionBar actionBar=getSupportActionBar();


        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        View actionbarview= layoutInflater.inflate(R.layout.custom_chat_bar,null);

        actionBar.setCustomView(actionbarview);



        username=findViewById(R.id.custom_profile_name);
        userlastseen=findViewById(R.id.custom_user_last_seen);
        userprofile=findViewById(R.id.custom_profile_image);
        sendMessageButton=findViewById(R.id.send_message_btn);

        messagesentinput=findViewById(R.id.input_messages);

        messageAdapter=new MessageAdapter(messagesList);


        backk=findViewById(R.id.backk);


        usermessagerecyclerview=findViewById(R.id.private_message_list_of_users);

        linearLayoutManager=new LinearLayoutManager(this);

        usermessagerecyclerview.setLayoutManager(linearLayoutManager);

        usermessagerecyclerview.setAdapter(messageAdapter);

        usermessagerecyclerview.setHasFixedSize(true);


        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/yyyy");
        savecurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        savecurrentTime=currentTime.format(calendar.getTime());
        username.setText(getMessageRecievername);
      Picasso.get().load(messagereceiverimage).placeholder(R.drawable.profile_image).into(userprofile);
        Displaylastseen();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });


        RootRef.child("Messages").child(messageSenderId).child(messageRecieverId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages=dataSnapshot.getValue(Messages.class);
                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();
                usermessagerecyclerview.smoothScrollToPosition(usermessagerecyclerview.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    public void Displaylastseen()
    {
        RootRef.child("Users").child(messageRecieverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("userState").hasChild("state"))
                {
                    String state=dataSnapshot.child("userState").child("state").getValue().toString();
                    String date=dataSnapshot.child("userState").child("date").getValue().toString();
                    String time=dataSnapshot.child("userState").child("time").getValue().toString();

                    if(state.equals("online"))
                    {
                        userlastseen.setText("online");
                    }
                    else if(state.equals("offline"))
                    {
                        userlastseen.setText("Last seen: "+date+" "+time);
                    }
                }
                else
                {
                    userlastseen.setText("offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void SendMessage() {
        String messagetext=messagesentinput.getText().toString();
        if(TextUtils.isEmpty(messagetext))
        {
            Toast.makeText(this,"Please enter message first..",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef="Messages/"+messageSenderId+"/"+messageRecieverId;
            String messageReceiverRef="Messages/"+messageRecieverId+"/"+messageSenderId;

            DatabaseReference Usermessagekeyref=RootRef.child("Messages").child(messageSenderId).child(messageRecieverId).push();
            String messagePushID=Usermessagekeyref.getKey();

            Map<String, Object> messageTextBody=new HashMap<>();
            messageTextBody.put("message",messagetext);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderId);
            messageTextBody.put("to",messageRecieverId);
            messageTextBody.put("messageID",messagePushID);
            messageTextBody.put("time",savecurrentTime);
            messageTextBody.put("date",savecurrentDate);


            Map<String, Object> messageBodyDetails =new HashMap<>();
            messageBodyDetails.put(messageSenderRef+"/"+messagePushID,messageTextBody);
            messageBodyDetails.put(messageReceiverRef+"/"+messagePushID,messageTextBody);
            messagesentinput.setText("");//set the message input empty after click send message
            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        // Toast.makeText(ChatActivity.this,"Message sent Successfully...",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ChatActivity.this,"Error:",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }






}






