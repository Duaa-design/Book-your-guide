package com.md.splashloginsignup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class ChatsFragment extends Fragment {


    private View view;
    private RecyclerView recyclerView;
    private DatabaseReference userRef,chatRef;
    private FirebaseAuth mAuth;
    private String userID;
    private Toolbar chattoolbarr;


    public ChatsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_chats, container, false);


        mAuth=FirebaseAuth.getInstance();

        userID=mAuth.getCurrentUser().getUid();

        userRef=FirebaseDatabase.getInstance().getReference().child("Users");

        chatRef= FirebaseDatabase.getInstance().getReference().child("Chats").child(userID);

        recyclerView=view.findViewById(R.id.chats_list);//chat list contains all chats
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




        chattoolbarr=view.findViewById(R.id.chat_toolbarr);//chat tool bar

        ((AppCompatActivity)getActivity()).setSupportActionBar(chattoolbarr);


        ActionBar actionBar=  ((AppCompatActivity)getActivity()).getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater=(LayoutInflater)((AppCompatActivity)getActivity()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionbarview= layoutInflater.inflate(R.layout.custom_chat_list,null);

        actionBar.setCustomView(actionbarview);

        chattoolbarr.setTitle(null);


        return view;
    }
    public void onStart() {
        super.onStart();
//when the tour guide accept the request from tourist , the request saved in chats child and then the chat between them  will immediately open because in this query it only show all the saved id's in chat child
        FirebaseRecyclerOptions<users> options=new FirebaseRecyclerOptions.Builder<users>()
                .setQuery(chatRef,users.class).build();// if the chat child in firebase was "saved" then open chat between the both of users

//The Adapter class is the interface between the View menu widget and the firebase, configuring it as required as a list item.
        FirebaseRecyclerAdapter<users,ChatViewHolder> adapter=new FirebaseRecyclerAdapter <users, ChatViewHolder>(options) {
//an adapter guides the way the information are shown in the UI like here we want to display it in recyclerview so we extend a class called RecyclerView.Adapter passing our class that implements the ViewHolder pattern

            @Override
            //to update the RecyclerView.ViewHolder contents with the item at the given position , and This method will map the data came from firebase to the Card View.
            protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull users model) {
                // - get element from our dataset at this position
                // - replace the contents of the view with that element that came from our dataset
                final String userid=getRef(position).getKey();// يجيبلي الاي دي اللي بعد اي دي الcurrent user ،يعني
                // child(chat).child(current user).child(request_user);

                final String[] image = {"default_image"};
                userRef.child(userid).addValueEventListener(new ValueEventListener() {//get the request_user and give me his information like profile image and fullname..etc
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            if(dataSnapshot.hasChild("profile_images"))
                            {
                                image[0] =dataSnapshot.child("profile_images").getValue().toString();
                                Picasso.get().load(image[0]).placeholder(R.drawable.profile_image).into(holder.profile_image);
                            }
                            final String name=dataSnapshot.child("fullName").getValue().toString();
                            final String status=dataSnapshot.child("status").getValue().toString();

                            holder.username.setText(name);
                            holder.userstatus.setText("Last seen: "+"\n"+"Date "+" Time");

                            if(dataSnapshot.child("userState").hasChild("state"))
                            {
                                String state=dataSnapshot.child("userState").child("state").getValue().toString();
                                String date=dataSnapshot.child("userState").child("date").getValue().toString();
                                String time=dataSnapshot.child("userState").child("time").getValue().toString();

                                if(state.equals("online"))
                                {
                                    holder.userstatus.setText("online");//it will show me if he online or not
                                }
                                else if(state.equals("offline"))
                                {
                                    holder.userstatus.setText("Last seen: "+"\n"+date+" "+time);
                                }
                            }
                            else
                            {
                                holder.userstatus.setText("offline");
                            }




                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {//send his full name and status and image in another activity when click at the item card view
                                    Intent chatIntent=new Intent(getContext(),ChatActivity.class);
                                    chatIntent.putExtra("visit_user_id",userid);
                                    chatIntent.putExtra("visit_user_name",name);
                                    chatIntent.putExtra("visit_image", image[0]);
                                    startActivity(chatIntent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }







        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getContext()).inflate(R.layout.users_display_layout,parent,false);
            ChatViewHolder chatViewHolder=new ChatViewHolder(view);
            return chatViewHolder;
            //LayoutInflater is object Responsible for converting the XML file into a View object
        }
    };

        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }




public static class ChatViewHolder extends RecyclerView.ViewHolder {
    //ViewHolder contains all our views in the layout for the item.
    CircularImageView profile_image;
    TextView username,userstatus;
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        profile_image=itemView.findViewById(R.id.users_profile_image);
        username=itemView.findViewById(R.id.users_profile_name);
        userstatus=itemView.findViewById(R.id.users_status);
    }
}}
