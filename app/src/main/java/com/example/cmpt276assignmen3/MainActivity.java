package com.example.cmpt276assignmen3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Welcome screen animation adapted from https://www.youtube.com/watch?v=JLIFqqnSNmg
 * Created a splash-screen animation for the welcome screen.
 * Added a button to allow user to skip the animation and jumps to Main Menu,otherwise this activity
 * ends in 4 seconds and jumps to Main Menu automatically.
 *
 */
public class MainActivity extends AppCompatActivity {
    Animation topAnimation, btmAnimation;
    ImageView bunsImg;
    TextView title, authors;
    Button btnSkip;
    private static final int WELCOME_DURATION = 4000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = MainMenu.makeIntent(MainActivity.this);
                startActivity(intent);
                finish();
            }
        };

        handler.postDelayed(runnable, WELCOME_DURATION);
        btnSkip = findViewById(R.id.btnWelcomSkip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainMenu.makeIntent(MainActivity.this);
                startActivity(intent);
                handler.removeCallbacks(runnable);
                finish();
            }
        });

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        btmAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        bunsImg = findViewById(R.id.imgWelcomeBuns);
        title = findViewById(R.id.txtWelcomeTitle);
        authors = findViewById(R.id.txtWelcomeAuthors);

        bunsImg.setAnimation(topAnimation);
        title.setAnimation(btmAnimation);
        authors.setAnimation(btmAnimation);

    }

}