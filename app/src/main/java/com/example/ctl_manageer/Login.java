package com.example.ctl_manageer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ctl_manageer.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity {

    /*Set activity binding*/
    ActivityLoginBinding binding;

    /*Creating variables*/
    private EditText edtUsername;
    private EditText edtPassword;
    private String username;
    private String password;
    private CheckBox rememberMe;

    /*Firebase Database*/
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ctl-manager-204b0-default-rtdb.firebaseio.com/");

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        edtUsername = binding.edtUsername;
        edtPassword = binding.edtPassword;
        rememberMe = binding.cbxRememberMe;

        /*handle login button*/
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(username)){
                    edtUsername.setError("Please enter username");
                } else if (TextUtils.isEmpty(password)){
                    edtPassword.setError("Please enter password");
                } else {
                    readUser();
                }
            }
        });

        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        binding.tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void readUser() {

        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");

        if(reference.child(username) != null)
        {
            reference.child(username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userDetails ud = snapshot.getValue(userDetails.class);

                    if(username.equals(ud.getUsername()))
                    {
                        if(password.equals(ud.getPassword()))
                        {
                            Toast.makeText(Login.this,"Welcome " + username,Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(Login.this,Dashboard.class);
                            i.putExtra("usernameDt",ud.getUsername());
                            startActivity(i);
                            Remember();
                        }
                        else {
                            Toast.makeText(Login.this,"Incorrect password",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this,"Incorrect username",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            Toast.makeText(Login.this,"Username does not exists. Please register",Toast.LENGTH_SHORT).show();
        }
    }

    private void Remember(){

        if(rememberMe.isChecked()){
            SessionManager sessionManager = new SessionManager(Login.this,SessionManager.SESSION_REMEMBERME);
            if(sessionManager.checkRememberMe()){
                HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailFromSession();
                edtUsername.setText(rememberMeDetails.get(SessionManager.KEY_USERNAME));
                edtPassword.setText(rememberMeDetails.get(SessionManager.KEY_PASSWORD));
            }
        } else {
            edtUsername.setText("");
            edtPassword.setText("");
        }
    }
}