package com.example.cmpt276assignmen3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cmpt276assignmen3.GameLogic.Game;
import com.example.cmpt276assignmen3.GameLogic.GameManager;
//import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Populate the gameboard according to user's configurations.
 * Generated random positions for Xiaolongbaos, updated total
 * number of scans and number of Xiaolongbaos left on the gameboard
 * actively.
 *
 * Display hint on number of Xiaolongbaos in the axis when user clicks
 * on an empty cell, update corresponding numbers when user find a Xiaolongbao.
 *
 * Added visual effect and sound effect for each button click, i.e different sound
 * effects for whether an empty cell is clicked or a Xiaolongbao is clicked.
 *
 * Pop up AlertDialog box when user finishes the game, guiding the user back to
 * Main Menu.
 */

public class GameUI extends AppCompatActivity {
    private GameManager gameManager;
    private static int NUM_ROWS;
    private static int NUM_COLS;
    private static int NUM_BUNS;
    private static int num_buns_found=0;
    private static int num_clicks;

    private boolean hasBeenDoubleClick = false;
    private final List<List<Integer>> posBuns = new ArrayList<>();
    private final List<Integer> posBunsRow = new ArrayList<>();
    private final List<Integer> posBunsCol = new ArrayList<>();
    private List<Integer> recordClicked = new ArrayList<>();
    private int emptyClicked[][];
    private int clickedMoreThanOnce[][];
    private List<Button> clickedButtons = new ArrayList<>();
    private static int scans[][];

    private MediaPlayer mediaPlayer;

    public static final String TAG_ROW = "Passing Rows";
    public static final String TAG_COL = "Passing Cols";
    public static final String TAG_BUN = "Passing Buns";

    Button buttons[][];


    public static Intent makeIntent(Context context, int rows, int cols, int buns){
        Intent intent = new Intent(context, GameUI.class);
        intent.putExtra(TAG_ROW, rows);
        intent.putExtra(TAG_COL, cols);
        intent.putExtra(TAG_BUN, buns);
        return intent;
    }

    private void randPosBuns() {
        Random rand = new Random();
        int rowUpperBound = NUM_ROWS;
        int colUpperBound = NUM_COLS;
        for (int i=0; i<NUM_BUNS; i++){
            List<Integer> newPos = new ArrayList<>();
            int rand_row = rand.nextInt(rowUpperBound);
            int rand_col = rand.nextInt(colUpperBound);

            newPos.add(rand_row);
            newPos.add(rand_col);
            while (posBuns.contains(newPos)){
                rand_row = rand.nextInt(rowUpperBound);
                rand_col = rand.nextInt(colUpperBound);
                newPos.set(0, rand_row);
                newPos.set(1, rand_col);
            }
            posBuns.add(newPos);
            posBunsRow.add(rand_row);
            posBunsCol.add(rand_col);
            recordClicked.add(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_ui);
        Intent intent = getIntent();
        NUM_ROWS = intent.getIntExtra(TAG_ROW, 4);
        NUM_COLS = intent.getIntExtra(TAG_COL, 6);
        NUM_BUNS = intent.getIntExtra(TAG_BUN, 6);

        mediaPlayer = MediaPlayer.create(GameUI.this, R.raw.game_start);
        mediaPlayer.start();

        scans = new int[NUM_ROWS][NUM_COLS];
        emptyClicked = new int[NUM_ROWS][NUM_COLS];
        clickedMoreThanOnce = new int[NUM_ROWS][NUM_COLS];
        randPosBuns();

        buttons = new Button[NUM_ROWS][NUM_COLS];

        gameManager = GameManager.getInstance();
        Game newGame = new Game(NUM_ROWS, NUM_COLS, NUM_BUNS);
        gameManager.add(newGame);

        TextView highScore = findViewById(R.id.txtHighScore);
        highScore.setText("Your highest score for the current mode: "+gameManager.retriveHighScore(NUM_ROWS, NUM_BUNS));
        gameManager.numGamesStarted += 1;
        TextView numGamesStarted = findViewById(R.id.txtNumGames);
        numGamesStarted.setText("You have started: " + gameManager.numGamesStarted + " games");

        populateButtons();
        getTotalScans();

    }


    private void getTotalScans() {
        for (int i=0; i<NUM_ROWS; i++){
            for (int j=0; j<NUM_COLS; j++){
                scans[i][j] = findNumBunsInAxis(i, j);
                emptyClicked[i][j] = 0;
                clickedMoreThanOnce[i][j] = 0;
            }
        }

    }

    private void populateButtons() {
        num_clicks = 0;
        num_buns_found = 0;
        TableLayout table = findViewById(R.id.tableForButtons);
        TextView numClicks = findViewById(R.id.txtClickCounter);
        TextView numBunsFound = findViewById(R.id.txtBunsFound);
        numBunsFound.setText("Found " + num_buns_found + " of " + NUM_BUNS + " Xiaolongbaos");
        for (int row = 0; row < NUM_ROWS; row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);
            for (int col = 0; col < NUM_COLS; col++){
                int FINAL_ROW = row;
                int FINAL_COL = col;

                Button newButton = new Button(this);
                newButton.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));

                newButton.setPadding(0, 0, 0, 0);

                List<Integer> userClick = new ArrayList<>();
                userClick.add(FINAL_ROW);
                userClick.add(FINAL_COL);


                if (posBuns.contains(userClick)){
                    int index = posBuns.indexOf(userClick);

                    newButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (recordClicked.get(index) == 1){
                                num_clicks += 1;
                                bounceAnimation(FINAL_ROW, FINAL_COL);
                                mediaPlayer = MediaPlayer.create(GameUI.this, R.raw.boing);
                                mediaPlayer.start();
                                numClicks.setText("Number of scans used: " + num_clicks);
                                newButton.setText("" + scans[FINAL_ROW][FINAL_COL]);

                            }else if (recordClicked.get(index) == 0) {
                                mediaPlayer = MediaPlayer.create(GameUI.this, R.raw.wow);
                                mediaPlayer.start();
                                num_buns_found += 1;
                                numBunsFound.setText("Found " + num_buns_found + " of " + NUM_BUNS + " Xiaolongbaos");
                                if (num_buns_found == NUM_BUNS){
                                    mediaPlayer = MediaPlayer.create(GameUI.this, R.raw.yay);
                                    mediaPlayer.start();

                                    List<Integer> newScore = new ArrayList<>();
                                    newScore.add(NUM_ROWS);
                                    newScore.add(NUM_BUNS);
                                    newScore.add(num_clicks);
                                    gameManager.highScores.add(newScore);

                                    num_buns_found= 0;
                                    num_clicks = 0;
                                    FragmentManager manager = getSupportFragmentManager();
                                    MessageFragment dialog = new MessageFragment();
                                    dialog.setCancelable(false);
                                    dialog.show(manager, "MessageDialog");

                                }
                                emptyClicked[FINAL_ROW][FINAL_COL] = 1;
                                updateScanNum(FINAL_ROW, FINAL_COL);
                                updateClickedButtonHint();
                                gridButtonClicked(FINAL_ROW, FINAL_COL, R.drawable.full);
                            }else{
                                //do nothing
                            }
                            recordClicked.set(index, recordClicked.get(index)+1);
                        }
                    });

                } else{
                    newButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mediaPlayer = MediaPlayer.create(GameUI.this, R.raw.boing);
                            mediaPlayer.start();
                            clickedMoreThanOnce[FINAL_ROW][FINAL_COL] += 1;
                            if (clickedMoreThanOnce[FINAL_ROW][FINAL_COL] > 1){
                                num_clicks = num_clicks;
                            }else{
                                num_clicks += 1;
                            }
                            bounceAnimation(FINAL_ROW, FINAL_COL);
                            numClicks.setText("Number of scans used: " + num_clicks);
                            gridButtonClicked(FINAL_ROW, FINAL_COL, R.drawable.empty);
                            emptyClicked[FINAL_ROW][FINAL_COL] = 1;
                            newButton.setText("" + scans[FINAL_ROW][FINAL_COL]);

                        }
                    });

                }

                tableRow.addView(newButton);
                buttons[row][col] = newButton;
                //
            }
        }
        //
    }

    private void bounceAnimation(int row_pos, int col_pos){
        final Animation animation = AnimationUtils.loadAnimation(GameUI.this, R.anim.bounce);
        for (int col=0; col<NUM_COLS; col++){
            buttons[row_pos][col].startAnimation(animation);
        }
        for (int row=0; row<NUM_ROWS; row++){
            buttons[row][col_pos].startAnimation(animation);
        }
    }

    private void updateClickedButtonHint() {
        for (int row=0; row<NUM_ROWS; row++){
            for (int col=0; col<NUM_COLS; col++){
                List<Integer> userClick = new ArrayList<>();
                userClick.add(row);
                userClick.add(col);
                int index;
                if (posBuns.contains(userClick)) {
                    index = posBuns.indexOf(userClick);

                    if (emptyClicked[row][col] == 1 && recordClicked.get(index) != 1) {
                        buttons[row][col].setText("" + scans[row][col]);
                    }
                }else{
                    if (emptyClicked[row][col] == 1){
                        buttons[row][col].setText("" + scans[row][col]);
                    }
                }
            }
        }

    }

    private int findNumBunsInAxis(int row, int col) {
        int counter = 0;
        for (int i=0; i<posBunsRow.size(); i++){
            if (posBunsRow.get(i) == row && posBunsCol.get(i) == col){
                counter = counter;
            }else if(posBunsRow.get(i) == row){
                counter += 1;
            }else if(posBunsCol.get(i) == col){
                counter += 1;
            }
        }
        return counter;
    }

    private void updateScanNum(int row_bun, int col_bun) {
        for (int col=0; col<NUM_COLS; col++){
            scans[row_bun][col]-=1;
        }
        for (int row=0; row<NUM_ROWS; row++){
            scans[row][col_bun]-=1;
        }
        scans[row_bun][col_bun]+=2;

    }

    private void gridButtonClicked(int row, int col, int resourceID) {
        Button button = buttons[row][col];

        //Lock Button Sizes:
        lockButtonSizes();

        //Scale image to button:
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), resourceID);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));

        //Change text

        //button.setText("" + col);

        int textColor;

        button.setTextScaleX(1.5f);
        if (resourceID == R.drawable.full) { //xiaolongbao found
            button.setText("");
            textColor = Color.parseColor("#000000");
        }else{ //empty
            textColor = Color.parseColor("#FFFFFF");
        }
        button.setTextColor(textColor);


    }

    private void lockButtonSizes() {
        for (int row = 0; row < NUM_ROWS; row++){
            for (int col = 0; col < NUM_COLS; col++){
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);

            }
        }
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


}