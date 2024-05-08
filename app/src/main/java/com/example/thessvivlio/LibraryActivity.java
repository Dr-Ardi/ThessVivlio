package com.example.thessvivlio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LibraryActivity extends AppCompatActivity {

    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        Intent lib = getIntent();

        if (lib != null && lib.hasExtra("username")) {

            user = lib.getStringExtra("username");
        }
    }

    public void close(View v){
        Intent menu = new Intent(this, MenuActivity.class);
        menu.putExtra("username", user);
        startActivity(menu);
    }
}