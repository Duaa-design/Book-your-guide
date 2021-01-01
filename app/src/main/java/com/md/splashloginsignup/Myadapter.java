package com.md.splashloginsignup;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
//The Adapter class is the interface between the View menu widget and the firebase, configuring it as required as a list item.
public class Myadapter extends FirebaseRecyclerAdapter<search_model,Myadapter.SearchViewHolder>{
//an adapter guides the way the information are shown in the UI like here we want to display it in recyclerview so we extend a class called RecyclerView.Adapter passing our class that implements the ViewHolder pattern
    public Myadapter(@NonNull FirebaseRecyclerOptions<search_model> options) {

        super(options);




    }


    //to update the RecyclerView.ViewHolder contents with the item at the given position , and This method will map the data came from firebase to the Card View.
    protected void onBindViewHolder(@NonNull SearchViewHolder holder, final int position, @NonNull final search_model model) {

        // - get element from our dataset at this position
        // - replace the contents of the view with that element that came from our dataset

        holder.fullName.setText(model.getFullName());

        holder.Sign_up_as.setText(model.getSign_up_as());

        holder.Languages.setText(model.getLanguages());


        holder.City.setText(model.getCity());


        if(!model.getPayment().equals("")){
            holder.salary.setText(model.getPayment()+""+"$/Day");

        }
        Glide.with(holder.profile_images.getContext()).load(model.getProfile_images()).into(holder.profile_images);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String visit_user_id = getRef(position).getKey();

                Context context = v.getContext();

                Intent profileintent = new Intent(context, tourguide_request.class);

                profileintent.putExtra("visit_user_id", visit_user_id);//tourist

                context.startActivity(profileintent);


            }
        });
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        //LayoutInflater is object Responsible for converting the XML file into a View object



        return new SearchViewHolder(view);


    }


    class SearchViewHolder extends RecyclerView.ViewHolder {
//ViewHolder contains all our views in the layout for the item.
        String signUpas;
        CircleImageView profile_images;
        TextView fullName,Languages, Sign_up_as,salary,City;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);


            profile_images=(CircleImageView)itemView.findViewById(R.id.profile_images);
            fullName=(TextView)itemView.findViewById(R.id.fullName);
            Languages=(TextView)itemView.findViewById(R.id.Languages);
            Sign_up_as=(TextView)itemView.findViewById(R.id.Sign_up_as);
            City=(TextView)itemView.findViewById(R.id.City);
            salary=(TextView)itemView.findViewById(R.id.salary);
            signUpas = Sign_up_as.getText().toString();







        }}}