package com.example.thong.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.thong.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final Button play = (Button) findViewById(R.id.buttonPlay);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        final Button option = (Button) findViewById(R.id.buttonOption);
        option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, OptionActivity.class);
                String message = "menu";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
        final Button exit = (Button) findViewById(R.id.buttonExit);
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

}