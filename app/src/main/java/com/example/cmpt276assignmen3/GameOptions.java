package com.example.cmpt276assignmen3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cmpt276assignmen3.GameLogic.GameManager;

/**
 * Allow user to configure the gameboard size and
 * number of Xiaolongbaos on the board at once.
 * Save the user selection through SharedPreferences
 * in order to populate the gameboard accordingly.
 * Added two buttons to allow user to reset high-score
 * and number of games started.
 */

public class GameOptions extends AppCompatActivity {

    private GameManager gameManager;
    private static final String APPPREFS = "App Prefs";
    private static final String NUM_ROWS = "Number of rows selected";
    private static final String NUM_COLS = "Number of cols selected";
    private static final String NUM_BUNS = "Number of buns selected";
    private MediaPlayer mediaPlayer;

    public static Intent makeIntent(Context context){
        return new Intent(context, GameOptions.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_options);
        gameManager = GameManager.getInstance();

        setupButtonClearHighScore();
        setupButtonClearNumGames();


        mediaPlayer = MediaPlayer.create(GameOptions.this, R.raw.game_music);
        mediaPlayer.start();
        createRadioButtons();
    }



    private void setupButtonClearHighScore() {
        Button btn = findViewById(R.id.btnResetHighScore);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.highScores.clear();
                Toast.makeText(GameOptions.this, "Cleared All High Scores", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupButtonClearNumGames() {
        Button btn = findViewById(R.id.btnResetNumGames);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.numGamesStarted = 0;
                Toast.makeText(GameOptions.this, "Cleared Number of Games Started", Toast.LENGTH_SHORT).show();
            }
        });

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
        super.onResume();
        mediaPlayer.start();
    }

    private void createRadioButtons() {
        RadioGroup sizeGroup = (RadioGroup) findViewById(R.id.radioOptions);
        RadioGroup numBunsGroup = findViewById(R.id.radioNumBuns);
        int textColor = Color.parseColor("#FF706E6E");

        int[] numRows = getResources().getIntArray(R.array.num_rows);
        int[] numCols = getResources().getIntArray(R.array.num_cols);
        int[] numBuns = getResources().getIntArray(R.array.num_buns);

        //create the buttons
        for (int i = 0; i < numRows.length; i++) {
            final int numRowSelected = numRows[i];
            final int numColSelected = numCols[i];

            RadioButton button = new RadioButton(this);

            button.setButtonTintList(ColorStateList.valueOf(textColor));
            button.setTextColor(ColorStateList.valueOf(textColor));
            button.setText(getString(R.string.grid_size, numRowSelected, numColSelected));

            //Set on-click callbacks
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveGridSize(numRowSelected, numColSelected);
                }
            });
            //Add to radio group:
            sizeGroup.addView(button);

            //Select default button:
            if (numRowSelected == getRowsSelected(this) && numColSelected == getColsSelected(this)){
                button.setChecked(true);
            }

        }
        //create the buttons for number of xiaolongbaos
        for (int i=0; i<numBuns.length; i++){
            final int numBunsSelected = numBuns[i];

            RadioButton bunsButton = new RadioButton(this);
            bunsButton.setButtonTintList(ColorStateList.valueOf(textColor));
            bunsButton.setTextColor(ColorStateList.valueOf(textColor));
            bunsButton.setText(getString(R.string.num_buns, numBunsSelected));

            bunsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveNumBuns(numBunsSelected);
                }
            });
            //Add to radio group:
            numBunsGroup.addView(bunsButton);
            if (numBunsSelected == getBunsSelected(this)){
                bunsButton.setChecked(true);
            }
        }
    }

    private void saveNumBuns(int numBunsSelected) {
        SharedPreferences prefs = this.getSharedPreferences(APPPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_BUNS, numBunsSelected);
        editor.apply();
    }

    private void saveGridSize(int numRow, int numCol) {
        SharedPreferences prefs = this.getSharedPreferences(APPPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_ROWS, numRow);
        editor.putInt(NUM_COLS, numCol);
        editor.apply();
    }

    static public int getRowsSelected(Context context){
        SharedPreferences prefs = context.getSharedPreferences(APPPREFS, MODE_PRIVATE);
        int defaultValue = context.getResources().getInteger(R.integer.default_num_rows);
        return prefs.getInt(NUM_ROWS, defaultValue);
    }
    static public int getColsSelected(Context context){
        SharedPreferences prefs = context.getSharedPreferences(APPPREFS, MODE_PRIVATE);
        int defaultValue = context.getResources().getInteger(R.integer.default_num_cols);
        return prefs.getInt(NUM_COLS, defaultValue);
    }

    static public int getBunsSelected(Context context){
        SharedPreferences prefs = context.getSharedPreferences(APPPREFS, MODE_PRIVATE);
        int defaultValue = context.getResources().getInteger(R.integer.default_num_buns);
        return prefs.getInt(NUM_BUNS, defaultValue);
    }

}