package com.example.obhai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpassActivity extends AppCompatActivity {

    private EditText Femail;
    private Button forget, backLogin;
    String email;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgetpass);

        forget = findViewById(R.id.forgetBtn);
        Femail = findViewById(R.id.FEmail);

        mAuth = FirebaseAuth.getInstance();



        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        email = Femail.getText().toString();

        if (email.isEmpty()) {

            Femail.setError("Required");
            Femail.requestFocus();
        }
        else {
            forgetPassword();

        }
    }

    private void forgetPassword() {

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(forgetpassActivity.this, "Check your mailbox (spam)", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(forgetpassActivity.this,MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(forgetpassActivity.this, "Erroe"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}