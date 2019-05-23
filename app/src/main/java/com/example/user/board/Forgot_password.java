package com.example.user.board;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class Forgot_password extends AppCompatActivity {

    private EditText et_email;

    private Button btn_submit;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();
        btn_submit = findViewById(R.id.btn_submit);

        et_email = findViewById(R.id.et_email);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirminputs(view);
            }
        });
    }




    public void confirminputs(View view) {

        String email = et_email.getText().toString().trim();

        if (email != null) {
            firebaseAuth.sendPasswordResetEmail(et_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(Forgot_password.this, "Reset link sent to Email", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Forgot_password.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please enter an Email",Toast.LENGTH_LONG).show();
        }
    }
}












