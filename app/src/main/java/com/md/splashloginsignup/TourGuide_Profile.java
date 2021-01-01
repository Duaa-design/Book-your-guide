package com.md.splashloginsignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
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

public class TourGuide_Profile extends Fragment {
    EditText fall_name_tourgP;
    EditText email_tourgP;
    EditText phone_tourgP;
    EditText status;
    EditText salary;
    TextView Languages1;
    TextView tourg_full_name;
    Button updatebutton;
    ImageButton signoutTourg;
    DatabaseReference refrence;
    private FirebaseAuth mAuth1;
    private CircleImageView profile_image ;
    private static final int PICK_IMAGE = 1;
    String user_name,user_email,user_number, languages,user_status;
     FirebaseUser user;
     Uri imageUri;

    String Uid;

    StorageReference mStorageReference;
    String salary1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//creates and returns the view


        View view = inflater.inflate(R.layout.activity_tour_guide__profile, container, false);


       Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//getInstance() Gets the default FirebaseDatabase instance
        //getReference Gets a DatabaseReference for the database root node.
        mStorageReference = FirebaseStorage.getInstance().getReference().child("Profile images").child(Uid+".jpeg");


        refrence= FirebaseDatabase.getInstance().getReference("Users");



        fall_name_tourgP = (EditText) view.findViewById(R.id.fall_name_tourgP);
        email_tourgP = (EditText) view.findViewById(R.id.email_tourgP);
        phone_tourgP = (EditText) view.findViewById(R.id.phone_tourgP);
        Languages1= (TextView) view.findViewById(R.id.Languages);
        tourg_full_name= (TextView) view.findViewById(R.id.tourg_full_name);
        updatebutton=(Button)view.findViewById(R.id.updatebutton);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        status = (EditText) view.findViewById(R.id.status);
        salary =(EditText)view.findViewById(R.id.salary);




        user =FirebaseAuth.getInstance().getCurrentUser();

//get the photo url from firebase to load it into profile images
        if(user.getPhotoUrl()!= null){
            Glide.with(TourGuide_Profile.this).load(user.getPhotoUrl()).into(profile_image);

        }



        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        refrence = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


      refrence.addValueEventListener(new ValueEventListener(){


          @Override
          // onDataChange to read a static snapshot of the contents at a given path
          public void onDataChange(@NonNull DataSnapshot snapshot) {

              user_email = snapshot.child("email").getValue(String.class);
             user_name = snapshot.child("fullName").getValue(String.class);
              user_number = snapshot.child("phoneNumber").getValue(String.class);
              languages = snapshot.child("Languages").getValue(String.class);
              user_status=snapshot.child("status").getValue(String.class);
              salary1=snapshot.child("payment").getValue(String.class);

              fall_name_tourgP.setText(user_name);
              tourg_full_name.setText(user_name);
              email_tourgP.setText(user_email);
              phone_tourgP.setText(user_number);
              Languages1.setText(languages);
              status.setText(user_status);
              salary.setText(salary1);
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
              //request that the user select a file such as a document or photo and return a reference to your app
              gallery.setAction(Intent.ACTION_GET_CONTENT);

 //PICK_IMAGE is an integer number that identifies our request, When receiving the result in the onActivityResult() callback, the same request code is provided so that our app can properly identify the result and determine how to handle it.
startActivityForResult(Intent.createChooser(gallery,"select picture"),PICK_IMAGE);



        }
          });






    updatebutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

     if(isEmailChange() || isNameChange() || isPhoneNumberChange() || isLanguageChange() || isStatusChange() || isPaymentChange()){
        Toast.makeText(getActivity(), "Data has been Updated", Toast.LENGTH_LONG).show();

      }else{
         Toast.makeText(getActivity(), "Data is the same and can not be updated", Toast.LENGTH_LONG).show();
      }
   }});








        signoutTourg=(ImageButton)view.findViewById(R.id.signoutTourg);


        signoutTourg.setOnClickListener(new View.OnClickListener() {
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
                            startActivity(new Intent(getActivity(), Logintourguide.class));
                            Toast.makeText(getActivity(), "logged out successfully", Toast.LENGTH_SHORT).show();


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

        if(!user_name.equals(fall_name_tourgP.getText().toString())){//to check if the user entered same data twice and prevent that from happend
//this is the user name for specific user that he current sign in and display his data now and here we want to update his name
            refrence.child("fullName").setValue(fall_name_tourgP.getText().toString());//the value could be anything so anything user entered , update it
           // user_name=fall_name_tourgP.getText().toString();//pass the new email that entered to the database and updated
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
  if(!user_email.equals(email_tourgP.getText().toString())){
//this is the user name for specific user that he current sign in and display his data now and here we want to update his name
  refrence.child("email").setValue(email_tourgP.getText().toString());//the value could be anything so anything user entered , update it
    user_email=email_tourgP.getText().toString();//pass the new email that entered to the database and updated

    ruser.updateEmail(user_email).addOnSuccessListener(new OnSuccessListener<Void>() {
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


        mAuth1 = FirebaseAuth.getInstance();

        FirebaseUser ruser = mAuth1.getCurrentUser();

        String usersId1 = ruser.getUid();
        if(!user_number.equals(phone_tourgP.getText().toString())){
//this is the user name for specific user that he current sign in and display his data now and here we want to update his name
            refrence.child("phoneNumber").setValue(phone_tourgP.getText().toString());//the value could be anything so anything user entered , update it
            user_number=phone_tourgP.getText().toString();//pass the new email that entered to the database and updated

            return true;

        }else{
            return false;
        }
    }


    private boolean isLanguageChange() {
        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth1 = FirebaseAuth.getInstance();

        FirebaseUser ruser = mAuth1.getCurrentUser();

        String usersId1 = ruser.getUid();
        if(!languages.equals(Languages1.getText().toString())){
//this is the user name for specific user that he current sign in and display his data now and here we want to update his name
            refrence.child("Languages").setValue(Languages1.getText().toString());//the value could be anything so anything user entered , update it
            languages=Languages1.getText().toString();//pass the new languages that entered to the database and updated

            return true;

        }else{
            return false;
        }
    }


    private boolean isStatusChange() {
        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth1 = FirebaseAuth.getInstance();

        FirebaseUser ruser = mAuth1.getCurrentUser();

        String usersId1 = ruser.getUid();
        if(!user_status.equals(status.getText().toString())){
//this is the user name for specific user that he current sign in and display his data now and here we want to update his name
            refrence.child("status").setValue(status.getText().toString());//the value could be anything so anything user entered , update it
            user_status=status.getText().toString();//pass the new email that entered to the database and updated

            return true;

        }else{
            return false;
        }
    }

    private boolean isPaymentChange() {



     mAuth1 = FirebaseAuth.getInstance();

        FirebaseUser ruser = mAuth1.getCurrentUser();
        final String usersId1 = ruser.getUid();


       if(!salary1.equals(salary.getText().toString())){
           refrence.child("payment").setValue(salary.getText().toString());
           salary1=salary.getText().toString();
           return true;
       }




            return false;


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode ==getActivity().RESULT_OK){
            imageUri=data.getData();
            try {
                //bitmap is specific type of Drawable which is image, like: PNG, JPEG or so
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







