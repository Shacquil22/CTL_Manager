package com.example.ctl_manageer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ctl_manageer.databinding.ActivityLoginBinding;
import com.example.ctl_manageer.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp extends AppCompatActivity {

    /*Set activity binding*/
    ActivitySignUpBinding binding;

    private Button signUp;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtUsername;
    private String name;
    private String email;
    private String password;
    private String username;
    private int errorCount = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }

        getSupportActionBar().hide();

        /*Initialize edits*/
        edtName = binding.edtNameS;
        edtEmail = binding.edtEmailS;
        edtPassword = binding.edtPasswordS;
        edtUsername = binding.edtUsernameS;

        /*handle login button*/
       binding.tvAlreadySignedUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(SignUp.this, Login.class);
               startActivity(intent);
               finish();
           }
       });

        /*Onclick button*/
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                username = edtUsername.getText().toString();

                if(TextUtils.isEmpty(username)){
                    edtUsername.setError("Please enter a name");
                } else if (TextUtils.isEmpty(name)){
                    edtName.setError("Please enter a email");
                } else if (TextUtils.isEmpty(email)){
                    edtEmail.setError("Please enter a password");
                } else if (TextUtils.isEmpty(password)){
                    edtPassword.setError("Please re-enter your password");
                }else {
                    validateUser();
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

    private void registerUser() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReferenceFromUrl("https://ctl-manager-204b0-default-rtdb.firebaseio.com/");

        username = edtUsername.getText().toString();
        name = edtName.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        userDetails ud = new userDetails(username,name,email,password);

        reference.child("users").child(username).setValue(ud);

        Toast.makeText(this,"Welcome " + name,Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        finish();
    }


    private void validateUser() {

        username = edtUsername.getText().toString();
        email = edtEmail.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //userDetails userD = snapshot.getValue(userDetails.class);
                if(snapshot.child(username).exists()){
                    edtUsername.setError("Username already exists.");
                } else {
                    registerUser();
                    //Toast.makeText(SignUp.this, "Not found.", Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(SignUp.this, userD.getUsername(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        ref.child(username).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userDetails userD = snapshot.getValue(userDetails.class);
//
//                if(edtUsername.getText().toString().equals(userD.getUsername())){
//                    edtUsername.setError("Username already exists.");
//                    errorCount++;
//                } else if (email.equals(userD.getEmail())){
//                    edtEmail.setError("Email already in use.");
//                    errorCount++;
//                } else {
//                    registerUser();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }



//    private void CheckDataInDatabase() {
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
//
//        if(reference.equals(null)){
//            registerUser();
//        } else {
//            validateUser();
//        }
//    }
}