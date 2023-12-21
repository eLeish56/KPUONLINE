package com.example.pemilubersama;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DashBoard extends AppCompatActivity {
    private Button buttonPresiden1, buttonPresiden2, buttonPresiden3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        buttonPresiden1 = findViewById(R.id.buttonPresiden1);
        buttonPresiden2 = findViewById(R.id.buttonPresiden2);
        buttonPresiden3 = findViewById(R.id.buttonPresiden3);

        buttonPresiden1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(DashBoard.this, Presiden1Activity.class);
                 startActivity(intent);
            }
        });

        buttonPresiden2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(DashBoard.this, Presiden2Activity.class);
                 startActivity(intent);
            }
        });

        buttonPresiden3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(DashBoard.this, Presiden3Activity.class);
                 startActivity(intent);
            }
        });
    }
}
