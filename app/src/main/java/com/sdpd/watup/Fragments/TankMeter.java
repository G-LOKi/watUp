package com.sdpd.watup.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdpd.watup.R;

public class TankMeter extends Fragment {
    private LinearLayout empty_ll,fill_ll;
    private DatabaseReference databaseReference;

    public TankMeter() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tank_meter, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("devices").child("device1");


        empty_ll = view.findViewById(R.id.empty_ll);
        fill_ll = view.findViewById(R.id.fill_ll);

        final LinearLayout.LayoutParams emptyParam = (LinearLayout.LayoutParams) empty_ll.getLayoutParams();

        final LinearLayout.LayoutParams fillParam = (LinearLayout.LayoutParams) fill_ll.getLayoutParams();



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double totalHeight = new Double(dataSnapshot.child("tankHeight").getValue().toString());
                double currentHeight = new Double(dataSnapshot.child("currentHeight").getValue().toString());

                double filledPercentage,emptyPercentage;

                filledPercentage = (double) currentHeight/(double) totalHeight;
                emptyPercentage = (double) (1-filledPercentage);

                emptyParam.weight = (float)emptyPercentage;
                empty_ll.setLayoutParams(emptyParam);

                fillParam.weight = (float) filledPercentage;
                fill_ll.setLayoutParams(fillParam);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
