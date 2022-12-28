package com.example.cmpt276assignmen3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.cmpt276assignmen3.GameLogic.Game;
import com.example.cmpt276assignmen3.GameLogic.GameManager;

/**
 * Displayed nice-looking buttons with visual effects and sound effects.
 * Allow user to navigate through all activities freely, save game configuration
 * when user exits the options screen. Default confirguation is set to 4x6 with 6 Xiaolongbaos
 *
 */

public class MainMenu extends AppCompatActivity {
    private GameManager gameManager;
    private int numRows;
    private int numCols;
    private int numBuns;
    public static Intent makeIntent(Context context){
        return new Intent(context, MainMenu.class);
    }
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        mediaPlayer = MediaPlayer.create(MainMenu.this, R.raw.menu_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        gameManager = GameManager.getInstance();
        setupButtonPlayGame();
        setupButtonOptions();
        setupButtonHelp();
    }

    private void buttonPressed(){
        final MediaPlayer mp = MediaPlayer.create(MainMenu.this, R.raw.whoosh);
        mp.start();
    }

    private void setupButtonPlayGame(){
        ImageButton btn = findViewById(R.id.btnPlayGame);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        btn.startAnimation(shake);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed();
                Intent intent = GameUI.makeIntent(MainMenu.this, numRows, numCols, numBuns);
                startActivity(intent);
            }
        });
    }

    private void setupButtonOptions() {
        ImageButton btn = findViewById(R.id.btnOptions);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        btn.startAnimation(shake);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed();
                Intent intent = GameOptions.makeIntent(MainMenu.this);
                startActivity(intent);
            }
        });
    }

    private void setupButtonHelp() {
        ImageButton btn = findViewById(R.id.btnHelp);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        btn.startAnimation(shake);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed();
                Intent intent = HelpPage.makeIntent(MainMenu.this);
                startActivity(intent);
            }
        });

    }

    private void updateUserSelections(){
        numRows = GameOptions.getRowsSelected(this);
        numCols = GameOptions.getColsSelected(this);
        numBuns = GameOptions.getBunsSelected(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        updateUserSelections();
        super.onResume();
        mediaPlayer.start();
    }
}