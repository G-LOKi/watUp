package com.sdpd.watup;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdpd.watup.Fragments.Settings;
import com.sdpd.watup.Fragments.TankMeter;
import com.sdpd.watup.Fragments.WaterControl;
import com.sdpd.watup.Fragments.WaterLevelGraph;

public class HomeActivity extends FragmentActivity {

    private FirebaseUser mUser;

    private Settings mSettings;
    private TankMeter mTankMeter;
    private WaterLevelGraph mWaterLevelGraph;
    private WaterControl mWaterControl;
    private DatabaseReference databaseReference;
    public static final String Default_channel = "default";
    NotificationChannel defaultChannel = null;
    private NotificationManager manager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_tank_meter:
                    fragmentTransaction.replace(R.id.main, mTankMeter);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_water_level_graph:
                    fragmentTransaction.replace(R.id.main, mWaterLevelGraph);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_water_control:
                    fragmentTransaction.replace(R.id.main, mWaterControl);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_settings:
                    fragmentTransaction.replace(R.id.main, mSettings);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            defaultChannel = new NotificationChannel(Default_channel, "Default", NotificationManager.IMPORTANCE_DEFAULT);
            defaultChannel.setLightColor(Color.GREEN);
            defaultChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(defaultChannel);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("devices").child("device1");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mSettings = new Settings();
        mTankMeter = new TankMeter();
        mWaterControl = new WaterControl();
        mWaterLevelGraph = new WaterLevelGraph();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.main, mTankMeter);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Log.i("pppp", "null");
                        Log.i("pppp", dataSnapshot.child("tankHeight").getValue().toString());

                        Log.i("pppp", dataSnapshot.child("currentHeight").getValue().toString());

                        Double current, total;

                        current = Double.valueOf(dataSnapshot.child("currentHeight").getValue().toString());
                        total = Double.valueOf(dataSnapshot.child("tankHeight").getValue().toString());

                        double pop = total - current;
                        Log.i("pppp", String.valueOf(pop));
                        if (pop < 2) {
                            Log.i("pppp", "full");
                            buildNotif(true);
                        } else if (pop > 8) {
                            buildNotif(false);
                            Log.i("pppp", "empty");
                        }
                    }catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        launchRelevantActivitiesIfNeeded();


    }

    public void buildNotif(boolean isFull){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,Default_channel);

        if(isFull) {
            mBuilder.setColor(Color.GREEN);
            mBuilder.setSmallIcon(R.drawable.ic_water_white_24dp);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentText("Your water tank is full");
        }else{
            mBuilder.setColor(Color.RED);
            mBuilder.setSmallIcon(R.drawable.ic_water_white_24dp);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentText("Water tank is empty");
        }
        mBuilder.setContentTitle("WATUP | Water Tank Alert");

        Intent intent = new Intent(this, HomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void launchRelevantActivitiesIfNeeded() {

        if(mUser==null){
            Intent i = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

}

