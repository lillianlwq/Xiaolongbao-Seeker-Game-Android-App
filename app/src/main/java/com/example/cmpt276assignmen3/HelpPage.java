package com.example.cmpt276assignmen3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Changed the plain text URL to clickable hyperlink on the Help Page and
 * moved Resource to scrollable part in its XML file  which allow the user to view more
 * information at once.
 */

public class HelpPage extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    public static Intent makeIntent(Context context) {
        return new Intent(context, HelpPage.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help_page);
        mediaPlayer = MediaPlayer.create(HelpPage.this, R.raw.help_page);
        mediaPlayer.start();
        setupHyperlink();
    }

    private void setupHyperlink() {
        TextView linkTextView = findViewById(R.id.txtHelpResourceContent);
        TextView linkTextView8 = findViewById(R.id.txtHelpResourceContent8);
        TextView linkTextView2 = findViewById(R.id.txtHelpResourceContent2);
        TextView linkTextView3 = findViewById(R.id.txtHelpResourceContent3);
        TextView linkTextView4 = findViewById(R.id.txtHelpResourceContent4);
        TextView linkTextView5 = findViewById(R.id.txtHelpResourceContent5);
        TextView linkTextView6 = findViewById(R.id.txtHelpResourceContent6);
        TextView linkTextView7 = findViewById(R.id.txtHelpResourceContent7);
        TextView linkTextView9 = findViewById(R.id.txtHelpAuthorContent);
        TextView linkTextView10 = findViewById(R.id.txtHelpPageMusic);


        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView2.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView3.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView4.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView5.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView6.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView7.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView8.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView9.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView10.setMovementMethod(LinkMovementMethod.getInstance());

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
