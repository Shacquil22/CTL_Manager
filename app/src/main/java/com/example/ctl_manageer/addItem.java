package com.example.ctl_manageer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ctl_manageer.databinding.ActivityAddItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class addItem extends AppCompatActivity {

    ActivityAddItemBinding binding;

    private EditText edtName;
    private EditText edtDetails;
    private EditText edtDate;
    private ImageView imgPicture;
    private ImageView openCamera;
    private String itemName;
    private String itemDetails;
    private String itemDate;
    private Calendar calendar;
    private Button btnSave;

    private Uri imageUri;

    private StorageReference storageReference;
    private String username;
    private String collectionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }

        getSupportActionBar().hide();

        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        edtName = binding.edtItemName;
        edtDetails = binding.edtItemDetails;
        btnSave = binding.btnSave;
        imgPicture = binding.imgPhoto;
        openCamera = binding.imgCamera;

        storageReference = FirebaseStorage.getInstance().getReference();

        if (ContextCompat.checkSelfPermission(addItem.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(addItem.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        ActivityCompat.requestPermissions(addItem.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(addItem.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName = edtName.getText().toString();
                itemDetails = edtDetails.getText().toString();

                if(TextUtils.isEmpty(itemName)){
                    edtName.setError("Enter item name");
                } else if (TextUtils.isEmpty(itemDetails)) {
                    edtDetails.setError("Enter item details");
                } else {
                    Toast.makeText(addItem.this, "opening camera", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 101);
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName = edtName.getText().toString();
                itemDetails = edtDetails.getText().toString();

                if(TextUtils.isEmpty(itemName)){
                    edtName.setError("Enter item name");
                } else if (TextUtils.isEmpty(itemDetails)) {
                    edtDetails.setError("Enter item details");
                } else {
                    AddItem();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
//            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
//            imgPicture.setImageBitmap(captureImage);

            onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap captureImage = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        captureImage.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        byte bb[] = bytes.toByteArray();
        imgPicture.setImageBitmap(captureImage);

        uploadToFirebase(bb);
    }

    private void uploadToFirebase(byte[] bb) {

        Intent intent = getIntent();
        username = intent.getStringExtra("userN");
        collectionName = intent.getStringExtra("colName");

        String name = edtName.getText().toString();
        //StorageReference sr = storageReference.child("Images/" + name + ".jpg");
        StorageReference sr = storageReference.child("Images/").child(username).child(collectionName).child(name + ".jpg");
        sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(addItem.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addItem.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intD = new Intent();
        intD.putExtra("ImageName",name);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void AddItem() {

        Intent i = getIntent();
        String username = i.getStringExtra("userN");
        String collectionName = i.getStringExtra("colName");

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference refItem = rootNode.getReferenceFromUrl("https://ctl-manager-204b0-default-rtdb.firebaseio.com/");

        itemName = edtName.getText().toString();
        itemDetails = edtDetails.getText().toString();

        calendar = Calendar.getInstance();
        itemDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        AddItemDetails ad = new AddItemDetails(itemName, itemDetails, itemDate);

        refItem.child("users").child(username).child("collection").child(collectionName).child(itemName).setValue(ad);

        Toast.makeText(addItem.this, "Item added", Toast.LENGTH_SHORT).show();

        Intent intC = new Intent(addItem.this, Collection2.class);
        intC.putExtra("ItemName", itemName);
        intC.putExtra("ItemDetails", itemDetails);
        intC.putExtra("ItemDate", itemDate);
        startActivity(intC);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



}
//allows user to select from phone storage.
//    private void selectImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,100);
//    }


