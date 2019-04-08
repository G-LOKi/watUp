package com.sdpd.watup.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdpd.watup.R;
import com.sdpd.watup.WaterLevelModel;

import java.util.ArrayList;


public class WaterLevelGraph extends Fragment {

    LineChart lineChart;

    ArrayList<String> xAxes = new ArrayList<>();
    ArrayList<Entry> yAxes = new ArrayList<>();
    ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
    private DatabaseReference databaseReference;

    public WaterLevelGraph() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_water_level_graph, container, false);
        lineChart = view.findViewById(R.id.lineChart);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("devices").child("device1").child("history");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yAxes.clear();
                xAxes.clear();
                for (DataSnapshot shot: dataSnapshot.getChildren()){

                        WaterLevelModel waterLevel = shot.getValue(WaterLevelModel.class);

                        yAxes.add(new Entry((waterLevel.getTimeStamp()%100000)/100, waterLevel.getHeight()));

                }
                xAxes.add("Monday");
                xAxes.add("Tuesday");
                xAxes.add("Wednesday");
                xAxes.add("Thursday");
                xAxes.add("Friday");

                String[] xaxes = new String[xAxes.size()];

                for(int i=0;i<xAxes.size();i++)
                {
                    xaxes[i] = xAxes.get(i).toString();
                }

                LineDataSet lineDataSet = new LineDataSet(yAxes,"values");
                lineDataSet.setDrawCircles(true);
                lineDataSet.setColor(Color.BLUE);

                lineDataSets.add(lineDataSet);
                lineChart.setData(new LineData(lineDataSets));

                lineChart.setVisibleXRangeMaximum(100f);
                lineChart.setTouchEnabled(true);
                lineChart.setDragEnabled(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

}
