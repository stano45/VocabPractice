package com.stanley.vocabpractice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//activity to display all functions of the app in form of buttons
public class MenuActivity extends AppCompatActivity {

    private Context context = GlobalApplication.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //button to add words/groups
        final Button add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddActivity.class);
                startActivity(intent);
            }
        });

        //button to start tests
        final Button start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectTestActivity.class);
                startActivity(intent);

            }
        });

        //button to view words/groups
        final Button view_button = findViewById(R.id.view_button);
        view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewWordsActivity.class);
                startActivity(intent);

            }
        });

        //button to view already completed tests
        final Button view_tests_button = findViewById(R.id.view_tests_button);
        view_tests_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewTestResultsActivity.class);
                startActivity(intent);
            }
        });


    }
}
