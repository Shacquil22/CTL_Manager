package com.example.ctl_manageer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ctl_manageer.databinding.ActivityCollection2Binding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Collection2 extends AppCompatActivity {

    ActivityCollection2Binding binding;

    private ImageButton back;
    private String username;
    private String collectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection2);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FFFFFFFF"));

        getSupportActionBar().setBackgroundDrawable(colorDrawable);



        binding = ActivityCollection2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        back = binding.btnBack;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        username = i.getStringExtra("username");
        collectionName = i.getStringExtra("CollectionName");

        CheckData();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addItemM:
                Intent in = new Intent(Collection2.this, addItem.class);
                in.putExtra("userN", username);
                in.putExtra("colName", collectionName);
                startActivity(in);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    ListView list;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> alist = new ArrayList<>();
    String[] coll;

    private void CheckData() {

//        Intent intent = getIntent();
//        String itemName = intent.getStringExtra("ItemName");
//        String itemDetails = intent.getStringExtra("ItemDetails");
//        String itemDate = intent.getStringExtra("itemDate");

//        Intent i = getIntent();
//        String username = i.getStringExtra("username");
//        String collectionName = i.getStringExtra("CollectionName");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(username).child("collection");

        reference.child(collectionName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()) {
                    String value = String.valueOf(snapshot.child("itemName").getValue());
//                    String valueDate = String.valueOf(snapshot.child("itemDate").getValue());
//                    String valueDetails = String.valueOf(snapshot.child("itemDetails").getValue());

                    ListView listView = (ListView) findViewById(R.id.lv_additemlist);

                    //String[] dt = {value + "\n" + valueDetails + "\n" + valueDate};
                    String[] dt = {value};
                    int images[] = {R.drawable.logo1};

                    for (String idt: dt) {
                        alist.add(idt);
                    }

                    ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(Collection2.this, R.layout.list_view_item_color,alist);
                    listView.setAdapter(itemAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String item = parent.getItemAtPosition(position).toString();
                            int pos = position;

                            if(position == pos)
                            {
                                Toast.makeText(getApplicationContext(),"clicked " + parent.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Collection2.this,itemData.class);
                                i.putExtra("item",item);
                                i.putExtra("username",username);
                                i.putExtra("collectionName",collectionName);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Not found",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(Collection2.this, "Data does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Toast.makeText(Collection1.this,itemDate,Toast.LENGTH_SHORT).show();
    }
}