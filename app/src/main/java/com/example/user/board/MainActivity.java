package com.example.user.board;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class MainActivity extends Activity {


    public FirebaseAuth firebaseAuth;
    public EditText et_content,et_fineprint;
    public TextView et_username;
    public Button btn_news;
    public String et_faculty= "science";
    public Button btn_add, btn_list, btn_choose, btn_logout;
    public ImageView img_poster;
    private Uri imageuri;
    private DatabaseReference databaseReference;
    FirebaseStorage storage;
    private StorageReference storageReference;
    final int REQUEST_CODE_GALLERY = 999;
    public String dating;
    private StorageTask uploadtask;
    private ProgressBar progressBar;
    public static Sqlitehelper sqlitelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //btn_news= findViewById(R.id.btn_news);
        et_content= findViewById(R.id.et_content);
        et_fineprint= findViewById(R.id.txt_fineprint);
        btn_add= findViewById(R.id.btn_add);
        btn_choose= findViewById(R.id.btn_choose);
        btn_list= findViewById(R.id.btn_list);
        img_poster= findViewById(R.id.img_poster);

        //final Spinner spinner = findViewById(R.id.et_faculty);
        btn_list= findViewById(R.id.btn_list);
        //btn_logout= findViewById(R.id.btn_logout);
        et_username= findViewById(R.id.et_username);
        //progressBar= findViewById(R.id.pbar);

//Firebase area
        firebaseAuth= FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");
        FirebaseUser user = firebaseAuth.getCurrentUser();
//et_username.setText("Welcome" + "  " + user.getEmail());

        if (user != null) {
            et_username.setText(user.getDisplayName());

        }





        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });
//get date today
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddhh:mm");
        final String dating = format.format(today);



        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNews();
            }
        });

        btn_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNews();
            }
        });

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadtask!=null &&uploadtask.isInProgress())
                {
                    Toast.makeText(MainActivity.this,"Upload in progress",Toast.LENGTH_LONG).show();
                }
                else {
                    uploadFile();
                }
            }
        });

    }

    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY &&resultCode == RESULT_OK
                && data != null &&data.getData() != null)
        {
            imageuri= data.getData();

            Picasso.with(this).load(imageuri).into(img_poster);
        }
    }
    //File extension type either png or jpeg
    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void openNews()
    {
//startActivity(new Intent(getApplicationContext(), News_list_Activity.class));
//        startActivity(new Intent(getApplicationContext(), Post_list.class));
    }


    private void uploadFile()
    {

        StorageReference fileref = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageuri));


        uploadtask= fileref.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        },500);
                        Toast.makeText(MainActivity.this,"Upload Successful",Toast.LENGTH_LONG).show();
                        News news = new News(et_content.getText().toString().trim(),et_fineprint.getText().toString().trim(),et_faculty,dating,taskSnapshot.getStorage().
                                getDownloadUrl().toString());
                        String uploadId = databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(news);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
                    }
                });
    }
//        else
//        {
//            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//            alert.setTitle("Doctor");
//            alert.setMessage("This Post will not have a picture");
//            alert.setPositiveButton("OK",null);
//            alert.show();
//
//        }
}

