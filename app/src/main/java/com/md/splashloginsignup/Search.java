package com.md.splashloginsignup;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Search extends Fragment {
    RecyclerView recview;
    Myadapter adapter;

    DatabaseReference reference,reference1;
    Query query,query1;
    private Toolbar toolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Called when the LayoutManager should save its state.


        View view = inflater.inflate(R.layout.activity_serach, container, false);





        setHasOptionsMenu(true);//it necessary to put this line when we have fragment class to tell our class so that we have menu

        toolbar=view.findViewById(R.id.toolbar);


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);//it will set toolbar that contain search bar

       // mSearchView = (SearchView)view.findViewById(R.id.search_view);

        recview=(RecyclerView)view.findViewById(R.id.recview);
//LayoutManager implementation the recyclerview
        recview.setLayoutManager(new LinearLayoutManager(getActivity()));//Creates a vertical LinearLayoutManager




       reference = FirebaseDatabase.getInstance().getReference("Users");


       query = reference.orderByChild("Sign_up_as").equalTo("Tour Guide");


        FirebaseRecyclerOptions<search_model> options =
                new FirebaseRecyclerOptions.Builder<search_model>().setQuery(query, search_model.class).build();



        adapter=new Myadapter(options);


        recview.setAdapter(adapter);

        adapter.startListening();

        return view;
    }




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        menu.clear();
        inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.search, menu);

        ///here we want to create listener for our search view so we can add listener to it and filter our recycle view
        MenuItem searchItem =  menu.findItem(R.id.action_search);

        //now we want to make reference for the search it self

//widget that provides a user interface for the user to enter a search query and submit a request
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search Location..");
//The max width needs to be higher than 1000 for it to fill the view.
        searchView.setMaxWidth(Integer.MAX_VALUE);

        //show the search bar to the screen and remove app theme from toolbar
        searchView.setIconifiedByDefault(false);

//getSystemService to retrieve a SearchManager for handling searches.
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);





     searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);//THIS will replace the search button in keyboard with appropriate one


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                processsearch(s);

                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void processsearch(String s)

    {


        reference1 = FirebaseDatabase.getInstance().getReference("Users");

        query1 = reference1.orderByChild("City");

        FirebaseRecyclerOptions<search_model> options =
                new FirebaseRecyclerOptions.Builder<search_model>().setQuery(query1.startAt(s.toLowerCase()).endAt(s.toLowerCase()+"\uf8ff"), search_model.class).build();
        adapter=new Myadapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);

    }



}


















