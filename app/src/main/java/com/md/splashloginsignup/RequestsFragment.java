

package com.md.splashloginsignup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.md.splashloginsignup.R;
import com.md.splashloginsignup.users;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class RequestsFragment extends Fragment {
    private RecyclerView mRequestList;

    private View myMainView;
    private FirebaseAuth mAuth;
    private DatabaseReference request, UsersReference,friendsDatabasereq,chatsDatabasereq;
    String online_Users_id;

    public RequestsFragment() {

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myMainView=inflater.inflate(R.layout.fragment_request, container, false);

        mRequestList = (RecyclerView)myMainView.findViewById(R.id.chat_request_recyclerview);

        mAuth=FirebaseAuth.getInstance();
        online_Users_id=mAuth.getUid();





        request=FirebaseDatabase.getInstance().getReference().child("Chat Requests").child(online_Users_id);



        friendsDatabasereq=FirebaseDatabase.getInstance().getReference().child("Chat Requests");

        chatsDatabasereq=FirebaseDatabase.getInstance().getReference().child("Chats");

        UsersReference=FirebaseDatabase.getInstance().getReference().child("Users");

        //setHasFixedSize() to true when changing the contents of the adapter does not change it's height or the width.
        mRequestList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);//this will show the new friend request at the top and the other will be at the bottom
        mRequestList.setLayoutManager(linearLayoutManager);




        return myMainView;


    }


    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<users> options =
                new FirebaseRecyclerOptions.Builder<users>().setQuery(request, users.class).build();

        FirebaseRecyclerAdapter<users, RequestViewHolder> adapter = new FirebaseRecyclerAdapter<users, RequestViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull users model) {

                final String list_users_id=getRef(position).getKey();
                DatabaseReference getType_refrence=getRef(position).child("request_type").getRef();

                getType_refrence.addValueEventListener(new ValueEventListener() {// this will appears in tour guide request only when he receives requests from tourist
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            String req_type = snapshot.getValue().toString();
                            if (req_type.equals("received")) {
                                UsersReference.child(list_users_id).addValueEventListener(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild("profile_images")) {
                                            final String requestimage = snapshot.child("profile_images").getValue().toString();
                                            Picasso.get().load(requestimage).placeholder(R.drawable.profile_image).into(holder.request_profile_image);
                                        }


                                        final String requestusername = snapshot.child("fullName").getValue().toString();
                                        final String requeststatus = snapshot.child("status").getValue().toString();


                                        if(snapshot.child(online_Users_id).child("request_details").hasChild("date_from")){
                                            final String requestdateFrom = snapshot.child(online_Users_id).child("request_details").child("date_from").getValue().toString();
                                            final String requestdateTo = snapshot.child(online_Users_id).child("request_details").child("date_to").getValue().toString();
                                            final String requestMumber_of_people = snapshot.child(online_Users_id).child("request_details").child("number_of_people").getValue().toString();
                                            holder.request_fullName.setText(requestusername);
                                            holder.request_status.setText("request for a trip");
                                            holder.date_from.setText(requestdateFrom);
                                            holder.date_to.setText(requestdateTo);
                                            holder.number_of_people.setText(requestMumber_of_people);
                                        }


                                                holder.request_accept_button.setOnClickListener(new View.OnClickListener(){

                                                    @Override
                                                    public void onClick(View view) {
                                                        chatsDatabasereq.child(online_Users_id).child(list_users_id).child("request").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    chatsDatabasereq.child(list_users_id).child(online_Users_id).child("request").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                friendsDatabasereq.child(online_Users_id).child(list_users_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            friendsDatabasereq.child(list_users_id).child(online_Users_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        Toast.makeText(getContext(), "Request sent", Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });
                                                    }

                                                });
                                                        holder.request_cancel_button.setOnClickListener(new View.OnClickListener(){


                                                            @Override
                                                            public void onClick(View view) {
                                                                friendsDatabasereq.child(online_Users_id).child(list_users_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            friendsDatabasereq.child(list_users_id).child(online_Users_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        UsersReference.child(list_users_id).child(online_Users_id).removeValue();
                                                                                        Toast.makeText(getContext(), "Request Deleted", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        });



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else if (req_type.equals("sent")) {// it will appears in request tourist page only

                                Button req_sent_button = holder.mView.findViewById(R.id.request_accept_button);
                                req_sent_button.setText("Sent");
                                holder.mView.findViewById(R.id.request_cancel_button).setVisibility(View.INVISIBLE);

                                UsersReference.child(online_Users_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild("profile_images")) {
                                            final String requestimage = snapshot.child("profile_images").getValue().toString();
                                            Picasso.get().load(requestimage).placeholder(R.drawable.profile_image).into(holder.request_profile_image);
                                        }
                                        if(snapshot.child(list_users_id).child("request_details").hasChild("date_from")){
                                        final String requestdateFrom = snapshot.child(list_users_id).child("request_details").child("date_from").getValue().toString();
                                        final String requestdateTo = snapshot.child(list_users_id).child("request_details").child("date_to").getValue().toString();
                                        final String requestMumber_of_people = snapshot.child(list_users_id).child("request_details").child("number_of_people").getValue().toString();
                                        final String requestusername = snapshot.child("fullName").getValue().toString();
                                        final String requeststatus = snapshot.child("status").getValue().toString();
                                       holder.request_fullName.setText("My Trip");
                                        holder.request_status.setText(requeststatus);
                                        holder.date_from.setText(requestdateFrom);
                                        holder.date_to.setText(requestdateTo);
                                        holder.number_of_people.setText(requestMumber_of_people);
                                        }


                                        holder.request_accept_button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                friendsDatabasereq.child(online_Users_id).child(list_users_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            friendsDatabasereq.child(list_users_id).child(online_Users_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        UsersReference.child(online_Users_id).child(list_users_id).removeValue();
                                                                        Toast.makeText(getContext(), "Request cancelled", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                            }
                                        });

                                                                }
                                                            });
                                                        }




                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






            }



            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_recycle, parent, false);
                RequestViewHolder requestViewHolder = new RequestViewHolder(view);

                return requestViewHolder;
            }





        };

        mRequestList.setAdapter(adapter);
        adapter.startListening();

    }








    public static class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView request_fullName,request_status,date_from,date_to,number_of_people;
        CircularImageView request_profile_image;
        Button request_accept_button,request_cancel_button;



        View mView;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            mView= itemView;
            request_fullName=itemView.findViewById(R.id.request_fullName);
            request_status=itemView.findViewById(R.id.request_status);
            request_profile_image=itemView.findViewById(R.id.request_profile_image);
            request_accept_button=(Button)itemView.findViewById(R.id.request_accept_button);
            request_cancel_button=(Button)itemView.findViewById(R.id.request_cancel_button);


            date_from=itemView.findViewById(R.id.date_from);
            date_to=itemView.findViewById(R.id.date_to);
            number_of_people=itemView.findViewById(R.id.number_of_people);

        }


    }




}

