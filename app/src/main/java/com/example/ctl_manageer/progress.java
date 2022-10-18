package com.example.ctl_manageer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ctl_manageer.databinding.ActivityProgressBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class progress extends AppCompatActivity {

    ActivityProgressBinding binding;

    BarChart barChart;
    EditText edtRoom;
    Button btnGoal;
    String roomName;
    TextView limit;
    Button btnProgress;
    private String userN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        binding = ActivityProgressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnGoal = binding.btnGetGoal;
        btnProgress = binding.btnProgress;

        btnProgress.setVisibility(View.INVISIBLE);

        btnGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();

                btnProgress.setVisibility(View.VISIBLE);
            }
        });

        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pb = new Intent(progress.this,itemProgress.class);
                pb.putExtra("userN",userN);
                pb.putExtra("roomName",roomName);
                startActivity(pb);
            }
        });

    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    private int countItems = 0;


    private void loadData(){

        edtRoom = binding.edtRoomNameGoal;
        roomName = edtRoom.getText().toString();
        limit = binding.tvLimit;

        Intent i = getIntent();
        userN = i.getStringExtra("userN");

        ref = database.getReference("users").child(userN).child("collection");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(roomName).exists()){

                    countItems = (int) snapshot.child(roomName).getChildrenCount();

                    GraphData gd = snapshot.child(roomName).getValue(GraphData.class);

                    barChart = binding.barchart;

                    int Goal = Integer.parseInt(gd.getListGoal());

                    if(countItems != Goal){
                        limit.setText("Goal is: " + Integer.toString(Goal));
                    } else {
                        limit.setText("Goal Reached.");
                    }

                    ArrayList<BarEntry> entries = new ArrayList<>();
                    entries.add(new BarEntry(countItems - 2, 0));

                    BarDataSet bardataset = new BarDataSet(entries, "Goal");

                    ArrayList<String> labels = new ArrayList<String>();
                    labels.add(gd.getListName());

                    BarData data = new BarData(labels, bardataset);
                    barChart.setData(data); // set the data and list of labels into chart
                    //barChart.setDescription("Set Bar Chart Description Here");  // set the description
                    bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    barChart.animateY(3000);
                    barChart.showContextMenu(20f,20f);

                } else {
                    edtRoom.setError("Room not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}