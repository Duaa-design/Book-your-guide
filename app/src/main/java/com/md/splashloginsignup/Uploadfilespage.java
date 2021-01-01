package com.md.splashloginsignup;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.annotation.Nonnull;


public class Uploadfilespage extends AppCompatActivity {
    private long downloadId;
    Button upload_id, upload_cv, upload_criminal, signup_complete;
    Button button6, button7, button8, btn_signup;
    TextView textView2, textView3, textView4;
    EditText editText_city;
    Uri pdfUri, pdfUri1, pdfUri2;// uri are actually URLs that are meant for local storage
    String fileName, fileName1, fileName2;

    FirebaseStorage storage; //used for uploadinf files.. Ex: PDF
    FirebaseDatabase database; //used to store the files URLs of uploaded files in the database to locate actual files on point of things .
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth1;
    String usersId1;
    String Tour_Guide = "Tour Guide";
    String user_name1, user_email1, user_number1, user_pass1 , languages;
    String url, url1, url2;
    private DatabaseReference mDatabaseReference;
    Map<String, String> user;
    FirebaseUser ruser;
    String city;
    HashMap<String,String> mobileMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfilespage);


        storage = FirebaseStorage.getInstance(); // return an object of firebase storage
        database = FirebaseDatabase.getInstance(); //return an object of firebase database , it will return object of firebase database

        // find id button of select files
        upload_id = findViewById(R.id.upload_id);
        upload_cv = findViewById(R.id.upload_cv);
        upload_criminal = findViewById(R.id.upload_criminal);

        // find id button of uploads
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        btn_signup = findViewById(R.id.btn_signup);
        // find id of text view
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        editText_city=(EditText) findViewById(R.id.editText_city);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file..");
        progressDialog.setProgress(0);//set initial progress dialog to 0

        //  final String cityPattern ="[a-zA-Z]+"|


        mAuth1 = FirebaseAuth.getInstance();

        ruser = mAuth1.getCurrentUser();

        usersId1 = ruser.getUid();



        btn_signup.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick (View view){
                if(TextUtils.isEmpty(url)){
                    Toast.makeText(Uploadfilespage.this, " please upload your ID first", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(TextUtils.isEmpty(url1)){
                    Toast.makeText(Uploadfilespage.this, " please upload your CV first", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(TextUtils.isEmpty(url2)){
                    Toast.makeText(Uploadfilespage.this, " please upload your Criminal background first", Toast.LENGTH_SHORT).show();
                    return;

                }
                city = editText_city.getText().toString().trim();
                if(TextUtils.isEmpty(city)){
                    Toast.makeText(Uploadfilespage.this, " this item cannot be empty", Toast.LENGTH_SHORT).show();
                    return;

                }else if(!city.matches("[a-zA-Z]+")){
                    Toast.makeText(Uploadfilespage.this, " numbers not allowed", Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    DatabaseReference reference = database.getReference("Users");//return the path to root

                    reference.child(usersId1).child("City").setValue(city);




                    //return the path to root
                    // List<String[]> array=new ArrayList<>();
                    // String [] cityy =city.split(",");
                    // array.add(cityy);


                }







                startActivity(new Intent(getApplicationContext(), navigation_tourGuide.class));

                //now i store and insert the information in the firebase





            }
        });




        // first file of upload id
        upload_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//take permission of our app to read external storage in the mobile
                if (ContextCompat.checkSelfPermission(Uploadfilespage.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)//if it has permission will retuned a constant 'PERMISSION_GRANTED' from PackageManager class
                {
                    selectID();

                } else
                    ActivityCompat.requestPermissions(Uploadfilespage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfUri != null) { // the user has selected the file
                    progressDialog.show();
                    fileName = System.currentTimeMillis() + "";
                    StorageReference storageReference = storage.getReference(); // return root paths
                    storageReference.child("Uploads").child(fileName).putFile(pdfUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()/* it will get invoked when our file succesfully uploded to firebase*/ {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                                    url = taskSnapshot.getUploadSessionUri().toString();// return url of you uploaded file and casting to string **********

                                    DatabaseReference reference = database.getReference("Users");//return the path to root

                                    reference.child(usersId1).child(fileName).setValue(url);
                                }
                            }).addOnFailureListener(new OnFailureListener() {//will invoked when failure to upload the file
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Uploadfilespage.this, " File is not successfuly uploaded", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // track the progress of = our upload.
                            int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()); // to get current progress
                            progressDialog.setProgress(currentProgress);//set progress to progress dialog

                        }
                    });


                } else {
                    Toast.makeText(Uploadfilespage.this, "Please upload your ID ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // second file of upload cv
        upload_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Uploadfilespage.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectCV();
                } else
                    ActivityCompat.requestPermissions(Uploadfilespage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);//if the user grant request permission of this line , the aknoldgment will be done in another method 'onRequestPermissionResult'
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfUri1 != null) { // the user has selected the file
                    progressDialog.show();
                    fileName1 = System.currentTimeMillis() + "";
                    StorageReference storageReference = storage.getReference(); // return root paths
                    storageReference.child("Uploads").child(fileName1).putFile(pdfUri1)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()/* it will get invoked when our file succesfully uploded to firebase*/ {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    url1 = taskSnapshot.getUploadSessionUri().toString(); // return url of you uploaded file and casting to string **********
                                    DatabaseReference reference = database.getReference("Users");//return the path to root

                                    reference.child(usersId1).child(fileName1).setValue(url1);

                                }
                            }).addOnFailureListener(new OnFailureListener() {//will invoked when failure to upload the file
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Uploadfilespage.this, " File is not successfuly uploaded", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // track the progress of = our upload.
                            int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()); // to get current progress
                            progressDialog.setProgress(currentProgress);//set progress to progress dialog

                        }
                    });


                } else {
                    Toast.makeText(Uploadfilespage.this, "Please upload your CV ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // third file of upload criminal certificate
        upload_criminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Uploadfilespage.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)//result will be stored in ([] grantResults)
                {
                    selectCriminal();
                    progressDialog.dismiss();

                } else
                    ActivityCompat.requestPermissions(Uploadfilespage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfUri2 != null) { // the user has selected the file
                    progressDialog.show();
                    fileName2 = System.currentTimeMillis() + "";
                    StorageReference storageReference = storage.getReference(); // return root paths
                    storageReference.child("Uploads").child(fileName2).putFile(pdfUri2)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()/* it will get invoked when our file succesfully uploded to firebase*/ {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    url2 = taskSnapshot.getUploadSessionUri().toString(); // return url of you uploaded file and casting to string **********
                                    DatabaseReference reference = database.getReference("Users");//return the path to root

                                    reference.child(usersId1).child(fileName2).setValue(url2);
                                }
                            }).addOnFailureListener(new OnFailureListener() {//will invoked when failure to upload the file
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Uploadfilespage.this, " File is not successfuly uploaded", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // track the progress of = our upload.
                            int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()); // to get current progress
                            progressDialog.setProgress(currentProgress);//set progress to progress dialog

                        }
                    });

                } else {
                    Toast.makeText(Uploadfilespage.this, "Please upload your Criminal background certificate", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }





    public void onRequestPermissionResult(int requestCode, @Nonnull String[] permissions, @Nonnull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);// line above is invoked here

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

            switch (requestCode){

                case 9:
                    selectID();
                    break;
                case 10:
                    selectCV();
                    break;
                case 11:
                    selectCriminal();
                    break;
            }

        }else{
            Toast.makeText(Uploadfilespage.this, "Please provide permission", Toast.LENGTH_SHORT).show(); // message to the user "LENGTH_SHORT" will show for 1 s an long will show for 3 s
        }


        // if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        //     selectID();//if the permission accepted we will call the selectbdf() method
        //    selectCV();

        //} else
        //   Toast.makeText(Uploadfilespage.this, "Please provide permission", Toast.LENGTH_SHORT).show(); // message to the user "LENGTH_SHORT" will show for 1 s an long will show for 3 s
    }






    private void selectID() {
        // to offer user to select file using file manager
        // we will be using an Intent
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); // to fetch files that use in our application
        startActivityForResult(intent, 86);// luonch intent , when this line will be excuted the onActivityResult() and onRequestPermissionResult methods will be auto invoked by android

    }

    private void selectCV() {
        // to offer user to select file using file manager
        // we will be using an Intent
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); // to fetch files that use in our application
        startActivityForResult(intent, 90);// luonch intent , when this line will be excuted the onActivityResult() and onRequestPermissionResult methods will be auto invoked by android

    }

    private void selectCriminal() {
        // to offer user to select file using file manager
        // we will be using an Intent
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); // to fetch files that use in our application
        startActivityForResult(intent, 95);// luonch intent , when this line will be excuted the onActivityResult() and onRequestPermissionResult methods will be auto invoked by android

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //method will get invoked by android to allow us to check whether user has selected a file or not (pdf)
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            switch (requestCode){
                case 86 :
                    pdfUri = data.getData(); //return the uri of selected file
                    textView2.setText("A file is selected : " + data.getData().getLastPathSegment());
                    break;
                case 90:
                    pdfUri1 = data.getData(); //return the uri of selected file
                    textView3.setText("A file is selected : " + data.getData().getLastPathSegment());
                    break;
                case 95:
                    pdfUri2 = data.getData(); //return the uri of selected file
                    textView4.setText("A file is selected : " + data.getData().getLastPathSegment());
                    break;


            }
        }else{
            Toast.makeText(Uploadfilespage.this, "Please select a file", Toast.LENGTH_SHORT).show(); // message to user
        }



    }
}

