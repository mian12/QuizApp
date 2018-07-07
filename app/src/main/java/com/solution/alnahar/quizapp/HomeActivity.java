package com.solution.alnahar.quizapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.solution.alnahar.quizapp.common.Common;
import com.solution.alnahar.quizapp.fragments.CategoryFragment;
import com.solution.alnahar.quizapp.fragments.RankingFragment;

import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_home);

        registrationNotification();


        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                android.support.v4.app.Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_category:

                        selectedFragment = CategoryFragment.newInstance();

                        break;

                    case R.id.nav_ranking:

                        selectedFragment = RankingFragment.newInstance();

                        break;
                }

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, selectedFragment);
                fragmentTransaction.commit();


                return true;

            }
        });

        setDefaultFragment();
    }

    private void registrationNotification() {
        mBroadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equalsIgnoreCase(Common.PUSH_NOTIFICATION));
                {
                   String message= intent.getStringExtra("message");
                   
                   showNotification("shahbaz idrees",message);
                }
            }
        };
    }

    private void showNotification(String title, String message) {

        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
       PendingIntent pendingIntent= PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alaramSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext())
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(message)
                .setSound(alaramSound)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000,1000,1000,1000,1000});

        NotificationManager notificationManager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(new Random().nextInt(),builder.build());

    }

    private void setDefaultFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
        fragmentTransaction.commit();
    }
}
