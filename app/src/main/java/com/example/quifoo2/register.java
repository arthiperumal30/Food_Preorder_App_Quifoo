package com.example.quifoo2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {
    TextView alreadyHaveaccount, showTerms;
    EditText inputEmail,inputPassword,inputConfirmPassword;
    Button btnRegister;
    ProgressDialog progressDialog;
    String emailPattern="[a-zA-Z0-9.-]+@[a-z]+\\.+tce.edu+";
    login loginob;
    CheckBox checkBox;
    boolean agree;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveaccount = findViewById(R.id.alreadyHaveaccount);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        showTerms = findViewById(R.id.showTerms);
        checkBox =  findViewById(R.id.checkbox);


        alreadyHaveaccount.setOnClickListener(v -> {
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);

        });

        showTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, termsandcon.class);
                startActivity(intent);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    agree = true;
                else
                    agree = false;
            }
        });

        btnRegister.setOnClickListener(view -> perforAuth());
    }




    private void perforAuth() {
        String email=inputEmail.getText().toString();
        String Password=inputPassword.getText().toString();
        String ConfirmPassword=inputConfirmPassword.getText().toString();
        if(!email.matches(emailPattern))
        {
            inputEmail.setError("Invalid Email Id");
        }
        else if(Password.isEmpty()||Password.length()<6)
        {
            inputPassword.setError("Your password must be at least 6 characters");
        }
        else if(!Password.equals(ConfirmPassword))
        {
            inputConfirmPassword.setError("Passwords do not match");
        }
        else if(!agree)
        {
            Toast.makeText(register.this,"Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setMessage("Please wait while registration is complete");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,Password).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    login.actual_email = email;
                    loginob = new login();
                    loginob.modifyEmail();
                }

                {
                    progressDialog.dismiss();
                    Toast.makeText(register.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    System.out.println(task.getException());
                }


            });


        }

    }




    private void sendUserToNextActivity() {
        Intent intent=new Intent(register.this,shopselection.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}