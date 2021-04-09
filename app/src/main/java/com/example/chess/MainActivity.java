package com.example.chess;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.chess.ui.home.BoardFragment;
import com.example.chess.ui.notifications.RecordingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public static BoardFragment bf;
    public static RecordingFragment rf;
    public static FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        fm = getSupportFragmentManager();





    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void boardClick(View view) {
        bf.boardClick(view);
    }

    public void undo(View view) {
        bf.undo(view);
    }

    public void resign(View view) {
        bf.resign(view);
    }

    public void draw(View view) {
        bf.draw(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ai(View view) {
        bf.ai(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void turn(View view) throws InterruptedException {
        bf.turn(view);
    }



    public void gameTitle(View view) {
        rf.gameTitle(view);
    }
    public void date(View view) {
        rf.date(view);
    }


    public void pickRecording(View view) {
        rf.pickRecording(view);
    }



}