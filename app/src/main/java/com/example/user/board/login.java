package com.example.user.board;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.board.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout text_input_email;
    private TextInputLayout text_input_password;

    private EditText et_email, et_password;
    private TextView et_register,txt_forgot_password;
    FirebaseAuth firebaseAuth;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        et_email = findViewById(R.id.et_email);
        txt_forgot_password = findViewById(R.id.txt_forgot_password);
        et_password = findViewById(R.id.et_password);
        et_register = findViewById(R.id.et_register);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }


        btn_login.setOnClickListener(this);
        et_register.setOnClickListener(this);
        txt_forgot_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_login) {
            confirminputs(view);
        }
        if (view == et_register) {
            startActivity(new Intent(getApplicationContext(), register.class));
        }
        if (view == txt_forgot_password)
        {
            startActivity(new Intent(getApplicationContext(), Forgot_password.class));
        }
    }


    public void confirminputs(View view) {

        if(!validateEmail() | validatePassword())
        {
            return;
        }

        userLogin();
//
//        String input ="Email:" + text_input_email.getEditText().getText().toString();
//        input += "\n";
//        input +="Password:" + text_input_password.getEditText().getText().toString();
//
//        Toast.makeText(this,input,Toast.LENGTH_LONG
//        ).show();
    }

    private boolean validateEmail() {
        String Emailinput = text_input_email.getEditText().getText().toString().trim();

        if (Emailinput.isEmpty()) {
            text_input_email.setError("Field cannot be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Emailinput).matches())
        {
            text_input_email.setError("Please Enter a valid Email address");
            return false;
        }

        else {
            text_input_email.setError(null);
            return true;
        }

    }
    // Password Data validation
    private boolean validatePassword()
    {
        String Passwordinput = text_input_password.getEditText().getText().toString().trim();

        if (Passwordinput.isEmpty())
        {
            text_input_password.setError("Field cannot be empty");
            return true;
        }
        else
        {
            text_input_password.setError(null);
            return true;
        }
    }



    private void userLogin() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

    }
}