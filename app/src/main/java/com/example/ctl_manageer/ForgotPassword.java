package com.example.ctl_manageer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ctl_manageer.databinding.ActivityForgotPasswordBinding;
import com.example.ctl_manageer.databinding.ActivityLoginBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ForgotPassword extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;

    private EditText edtUsername;
    private EditText edtEmail;
    private String username;
    private String email;
    private Button btnRecover;
    private ImageButton back;

    private FirebaseDatabase database;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }

        getSupportActionBar().hide();

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        edtUsername = binding.edtUsernameRec;
        edtEmail = binding.edtEmailRec;
        btnRecover = binding.btnRecover;
        back = binding.btnBackF;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPassword.this,Login.class);
                startActivity(i);
            }
        });


        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString();
                email = edtEmail.getText().toString();

                if(TextUtils.isEmpty(username))
                {
                    edtUsername.setError("Please enter name");
                }
                else if(TextUtils.isEmpty(email))
                {
                    edtEmail.setError("Please enter email");
                } else {
                    Recover();

                    Intent i = new Intent(ForgotPassword.this,Login.class);
                    startActivity(i);
                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        if(getFragmentManager().getBackStackEntryCount() == 0){
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void Recover() {

        username = edtUsername.getText().toString();
        email = edtEmail.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");

        if(reference.child(username) != null)
        {
            reference.child(username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userDetails ud = snapshot.getValue(userDetails.class);

                    if(username.equals(ud.getUsername()))
                    {
                        if(email.equals(ud.getEmail()))
                        {
                            Toast.makeText(ForgotPassword.this,"Password: " + ud.getPassword(),Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(ForgotPassword.this,"Password not found",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ForgotPassword.this,"Username not found",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(ForgotPassword.this,"Username does not exists. Please register",Toast.LENGTH_SHORT).show();
        }

    }
}