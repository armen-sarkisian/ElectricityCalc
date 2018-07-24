package com.example.sarkisian.electricitycalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    Button history;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        history = (Button) findViewById(R.id.history);
        history.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, history.class);
        startActivity(intent);


    }


    public void Total(View view) {
        Intent intent = new Intent(MainActivity.this, first.class);
        startActivity(intent);
    }


}




