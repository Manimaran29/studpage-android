package com.example.studpage;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.Smart_Home.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Main3Activity extends AppCompatActivity {
    private EditText nameField, emailField, passField, cPassField;
    private Button b3;

    private FirebaseAuth auth;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        nameField = (EditText) findViewById(R.id.signupName);
        emailField = (EditText) findViewById(R.id.signupEmail);
        passField = (EditText) findViewById(R.id.signupPass);
        cPassField = (EditText) findViewById(R.id.signUpCPass);

        b3 = (Button) findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSignUP();

            }
        });

    }

    private void startSignUP() {
        final String email = emailField.getText().toString();
        String pass = passField.getText().toString();
        String cPass = cPassField.getText().toString();
        final String name = nameField.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(cPass) || TextUtils.isEmpty(name)) {
            Toast.makeText(Main3Activity.this, "Fields can't be empty", Toast.LENGTH_LONG).show();
        }
        else if(!pass.equals(cPass)){
            Toast.makeText(Main3Activity.this, "Password Mismatch", Toast.LENGTH_LONG).show();
        }
        else {
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        User user = new User(name, email);

                        db.getReference("User")
                                .child(auth.getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()) {
                                    Toast.makeText(Main3Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(Main3Activity.this, "Signed Up Successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(Main3Activity.this, MainActivity.class));
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(Main3Activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
