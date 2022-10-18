package com.example.ctl_manageer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class itemData extends AppCompatActivity {

    private String username;
    private String collectionName;
    private String item;
    private TextView itemName;
    private String Date;
    private TextView itemDate;
    private ImageView itemImage;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_data);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColorIcons)));

        itemName = findViewById(R.id.itemNameTitle);
        itemDate = findViewById(R.id.itemDateTitle);
        itemImage = findViewById(R.id.itemImage);

        displayItemName();
        displayItemDate();
        displayImage();
    }

    private void displayItemName() {
        Intent i = getIntent();
        item = i.getStringExtra("item");
        username = i.getStringExtra("username");
        collectionName = i.getStringExtra("collectionName");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(username).child("collection");

        reference.child(collectionName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String iName = String.valueOf(snapshot.child(item).child("itemName").getValue());

                    itemName.setText(iName);

                } else {
                    itemName.setText("No data found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void displayItemDate() {
        Intent i = getIntent();
        item = i.getStringExtra("item");
        username = i.getStringExtra("username");
        collectionName = i.getStringExtra("collectionName");

        FirebaseDatabase databaseDate = FirebaseDatabase.getInstance();
        DatabaseReference referenceDate = databaseDate.getReference("users").child(username).child("collection");

        referenceDate.child(collectionName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String iName = String.valueOf(snapshot.child(item).child("itemDate").getValue());

                    itemDate.setText(iName);

                } else {
                    itemDate.setText("No data found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    private void displayImage(){
        Intent i = getIntent();
        item = i.getStringExtra("item");

        progressDialog = new ProgressDialog(itemData.this);
        progressDialog.setMessage("Fetching image...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //StorageReference sr = storageReference.child("Images/").child(username).child(collectionName).child(name + ".jpg");

        storageReference = FirebaseStorage.getInstance().getReference("Images/Room1.jpg");
        storageReference = FirebaseStorage.getInstance().getReference("Images/").child(username).child(collectionName).child(item + ".jpg");

        try {
            File localFile = File.createTempFile("tempfile",".jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();

                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    itemImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(itemData.this,"Failed to get image",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}