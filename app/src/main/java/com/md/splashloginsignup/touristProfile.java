package com.md.splashloginsignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class touristProfile extends Fragment {
    EditText phone_tourist , email_tourist ,fall_name_tourist;
    TextView tourist_full_name;

    Button updatebutton;
    DatabaseReference refrence;
    private FirebaseAuth mAuth1;
    private CircleImageView profile_image ;
    private static final int PICK_IMAGE = 1;
    String user_name1,user_email1,user_number1;
    FirebaseUser user;
    Uri imageUri;
    String url5;
    String Uid;
    FirebaseDatabase database;
    StorageReference mStorageReference;
ImageButton signoutTourist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_tourist_profile, container, false);


        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("Profile images").child(Uid+".jpeg");
        refrence= FirebaseDatabase.getInstance().getReference("Users");


        tourist_full_name = (TextView) view.findViewById(R.id.tourist_full_name);
        email_tourist = (EditText) view.findViewById(R.id.email_tourist);
        phone_tourist = (EditText) view.findViewById(R.id.phone_tourist);
        fall_name_tourist = (EditText) view.findViewById(R.id.fall_name_tourist);

        updatebutton=(Button)view.findViewById(R.id.updatebutton);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        signoutTourist=(ImageButton) view.findViewById(R.id.signoutTourist);



        user =FirebaseAuth.getInstance().getCurrentUser();
        if(user.getPhotoUrl()!= null){
            Glide.with(touristProfile.this).load(user.getPhotoUrl()).into(profile_image);

        }


        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        refrence = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        refrence.addValueEventListener(new ValueEventListener(){


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_name1 = snapshot.child("fullName").getValue(String.class);
                user_email1 = snapshot.child("email").getValue(String.class);
                user_number1 = snapshot.child("phoneNumber").getValue(String.class);

                tourist_full_name.setText(user_name1);
                fall_name_tourist.setText(user_name1);
                email_tourist.setText(user_email1);
                phone_tourist.setText(user_number1);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Tag", error.getMessage());
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"select picture"),PICK_IMAGE);



            }
        });

        //show all the data
        //get the data from the intent














        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isEmailChange() || isNameChange() || isPhoneNumberChange()){
                    Toast.makeText(getActivity(), "Data has been Updated", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getActivity(), "Data is the same and can not be updated", Toast.LENGTH_LONG).show();
                }
            }});




        signoutTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]{
                        "Do you want to really Logout?", "Cancel"
                };
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            refrence.child("userState").child("state").setValue("offline");

                            mAuth1 = FirebaseAuth.getInstance();
                            mAuth1.signOut();
                            // user auth state is changed - user is null
                            // launch login activity
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            Toast.makeText(getActivity(), "logged out successfully", Toast.LENGTH_SHORT).show();

                                getActivity().finish();
                        } if(which==1)
                        {
                            //for cancel do not do anything
                        }
                    }
                });
                builder.show();
            }
        });












        return view;

    }

    private boolean isNameChange() {

        //   String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(!user_name1.equals(fall_name_tourist.getText().toString())){//to check if the user entered same data twice and prevent that from happend
//this is the user name for specific user that he current sign in and display his data now and here we want to update his name
            refrence.child("fullName").setValue(fall_name_tourist.getText().toString());//the value could be anything so anything user entered , update it
            user_name1=fall_name_tourist.getText().toString();//pass the new email that entered to the database and updated
            return true;

        }else{
            return false;
        }
    }


    private boolean isEmailChange() {

        mAuth1 = FirebaseAuth.getInstance();

        FirebaseUser ruser = mAuth1.getCurrentUser();

        String usersId1 = ruser.getUid();

        //check if the user name in the tourguide profile is same that user entered in the login page
        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(!user_email1.equals(email_tourist.getText().toString())){
//this is the user name for specific user that he current sign in and display his data now and here we want to update his name
            refrence.child("email").setValue(email_tourist.getText().toString());//the value could be anything so anything user entered , update it
            user_email1=email_tourist.getText().toString();//pass the new email that entered to the database and updated
            ruser.updateEmail(user_email1).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity(), "email changed", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            return true;

        }else{
            return false;
        }
    }

    private boolean isPhoneNumberChange() {
        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if(!user_number1.equals(phone_tourist.getText().toString())){
//this is the user name for specific user that he current sign in and display his data now and here we want to update his name
            refrence.child("phoneNumber").setValue(phone_tourist.getText().toString());//the value could be anything so anything user entered , update it
            user_number1=phone_tourist.getText().toString();//pass the new email that entered to the database and updated

            return true;

        }else{
            return false;
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode ==getActivity().RESULT_OK){
            imageUri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);
                profile_image.setImageBitmap(bitmap);
                handleUpload(bitmap);

            }catch (IOException e ){
                e.printStackTrace();
            }
        }

    }
    private void handleUpload(Bitmap bitmap){
        final ByteArrayOutputStream baos =new ByteArrayOutputStream ();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100 ,baos);
        //every user upload his image based on his id as a name of the image + jpeg
        mStorageReference.putBytes(baos.toByteArray()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri imageUri) {
                        final Uri downloadUrl1 = imageUri;



                        String sUrl = downloadUrl1.toString();

                        refrence.child("profile_images").setValue(sUrl);

                        Log.d("TAG", "onSuccess"+ imageUri);
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        final UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(imageUri).build();
                        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {




                                Toast.makeText(getActivity(), "profile image updated successfully", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "profile image failed", Toast.LENGTH_LONG).show();
                            }
                        });




                    }
                });







            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG","OnFailure",e.getCause());
            }
        });
    }

}