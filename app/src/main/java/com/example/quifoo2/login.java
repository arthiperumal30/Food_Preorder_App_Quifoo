package com.example.quifoo2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    TextView createNewAccount;
    EditText inputEmail,inputPassword;
    Button btnLogin;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    static String actual_email;
    static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        createNewAccount = findViewById(R.id.createNewAccount);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnlogin);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        createNewAccount.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, register.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }

            private void performLogin() {
                actual_email = inputEmail.getText().toString();
                String Password = inputPassword.getText().toString();

                if (actual_email.isEmpty()) {
                    inputEmail.setError("Email ID is blank");
                }
                else if(Password.isEmpty()) {
                    inputPassword.setError("Password is blank");
                }
                else {
                    progressDialog.setMessage("Please wait while login is complete");
                    progressDialog.setTitle("Login");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(actual_email, Password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            sendUserToNextActivity();
                            Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            modifyEmail();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(login.this, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }

        });
    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(login.this,shopselection.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void modifyEmail()
    {
        email = actual_email.replace('@','_');
        email = email.replace('.','_');

    }
}