package com.example.user.board;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class register extends AppCompatActivity implements View.OnClickListener {

    Button btn_register;
    Spinner et_faculty;
    EditText et_adm;
    EditText et_email;
    EditText et_password;
    EditText et_username;

    private Spinner spinner;

    //ProgressBar pbar;

    //ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        //progressBar= new ProgressBar(this);
        btn_register = findViewById(R.id.btn_register);
        //pbar = findViewById(R.id.pbar);

        et_username = findViewById(R.id.et_username);
        et_adm  = findViewById(R.id.et_adm);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        spinner = findViewById(R.id.spinner);
        btn_register.setOnClickListener(this);

        //Spinner
        spinner=findViewById(R.id.spinner);
        List<String>dpt=new ArrayList<>();
        dpt.add(0,"");
        dpt.add("Fass");
        dpt.add("Science");
        dpt.add("Education");
        dpt.add("Law");

        //Style and populate spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, dpt);

        //Dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals(""))
                {
                    //do nothing
                }
                else
                {
                    //on selecting a spinner item
                    String item = parent.getItemAtPosition(position).toString();

                    //
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(getApplicationContext(),"Select a Faculty",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
    @Override public void onClick (View view){

        if (view == btn_register) {
            registerUser();
        }
    }

    private void registerUser() {
        final String admission = et_adm.getText().toString().trim();
        final String email = et_email.getText().toString().trim();
        final String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();


        Integer num_admission = Integer.valueOf(admission);

        //Data empty validation
        if (num_admission > 0 && num_admission <= 1500) {
            final String user_type = "lecturer";

            if (TextUtils.isEmpty(email)) {
                et_email.requestFocus();
                Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                et_password.requestFocus();
                Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(admission)) {
                et_adm.requestFocus();
                Toast.makeText(getApplicationContext(), "Please enter your Student Number", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(username)) {
                et_username.requestFocus();
                Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_LONG).show();
                return;
            }


            //if(admission ==)
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        User user = new User(admission, email, user_type,username);
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), login.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else if (num_admission >1010000 && num_admission <1050000)
        {
            final String user_type = "student";

            if(TextUtils.isEmpty(email))
            {
                et_email.requestFocus();
                Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(password))
            {
                et_password.requestFocus();
                Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(admission))
            {
                et_adm.requestFocus();
                Toast.makeText(getApplicationContext(), "Please enter your Number", Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(username))
            {
                et_username.requestFocus();
                Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_LONG).show();
                return;
            }
            //if(admission ==)
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful())
                    {
                        User user = new User(
                                admission,
                                email,
                                user_type,
                                username
                        );
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful())
                                {
                                    finish();
                                    Toast.makeText(getApplicationContext(),"Registration successful",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(),login.class));
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Unsuccessful",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(register.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
}
