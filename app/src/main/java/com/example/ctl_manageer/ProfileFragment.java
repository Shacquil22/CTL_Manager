package com.example.ctl_manageer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private Button update;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtUsername;
    private String name;
    private String email;
    private String password;
    private String username;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        } else {
            getActivity().setTheme(R.style.Theme_Light);
        }

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        update = getActivity().findViewById(R.id.btn_updateUSer);

        edtName = getActivity().findViewById(R.id.edtItemFirstName);
        edtUsername = getActivity().findViewById(R.id.edtItemUsername);
        edtEmail = getActivity().findViewById(R.id.edtItemEmail);
        edtPassword = getActivity().findViewById(R.id.edtItemPassword);

        collectData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = edtUsername.getText().toString();
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                if(TextUtils.isEmpty(name)){
                    edtName.setError("Please enter name");
                } else if (TextUtils.isEmpty(username)) {
                    edtUsername.setError("Please enter username");
                } else if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Please enter password");
                } else if (TextUtils.isEmpty(password)) {
                    edtPassword.setError("Please enter password");
                } else {
                    updateUser();
                }
            }
        });
    }

    private void updateUser() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReferenceFromUrl("https://ctl-manager-204b0-default-rtdb.firebaseio.com/");

        username = edtUsername.getText().toString();
        name = edtName.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        userDetails ud = new userDetails(username,name,email,password);

        reference.child("users").child(username).setValue(ud);

        Toast.makeText(getActivity(),"User Information updated",Toast.LENGTH_SHORT).show();
    }

    private void collectData(){
        Intent i = getActivity().getIntent();
        String user = i.getStringExtra("usernameDt");

        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");

        if(reference.child(user) != null)
        {
            reference.child(user).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userDetails ud = snapshot.getValue(userDetails.class);

                    if(user.equals(ud.getUsername()))
                    {
                        edtName.setText(ud.getName());
                        edtUsername.setText(ud.getUsername());
                        edtEmail.setText(ud.getEmail());
                        edtPassword.setText(ud.getPassword());
                        //edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);


                        //Toast.makeText(getActivity(), "User found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(),"user not found",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(getActivity(),"Username does not exists. Please register",Toast.LENGTH_SHORT).show();
        }
    }
}

//References
//https://stackoverflow.com/questions/2586301/set-inputtype-for-an-edittext-programmatically