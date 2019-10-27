package com.stanley.vocabpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SelectTestActivity extends AppCompatActivity {

    private Test currentTest = new Test();
    private TextView chooseGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_test);

        if (Data.wordGroupList.size() == 0) {
            Toast.makeText(GlobalApplication.getAppContext(),
                    "No word groups. Add them with the 'add words' button.",
                    Toast.LENGTH_SHORT).show();
            System.out.println("No word groups. Add them with the 'add words' button.");
            finish();
        }

        chooseGroup = findViewById(R.id.groupChoice);

        chooseGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GlobalApplication.getAppContext(), GroupSelectActivity.class);
                i.putExtra("requestId", 2);
                startActivityForResult(i, 2);
            }
        });

        final EditText numQuestionsEdit = findViewById(R.id.numQuestions);
        Button startTest = findViewById(R.id.startTestButton);
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numQuestions = Integer.parseInt(numQuestionsEdit.getText().toString());

                if (currentTest.wordGroup == null) {
                    Toast.makeText(GlobalApplication.getAppContext(),
                            "Select a group.", Toast.LENGTH_SHORT)
                            .show();
                    System.out.println("Select a group.");
                    return;
                }

                if (numQuestions <= 0) {
                    Toast.makeText(GlobalApplication.getAppContext(),
                            "Enter a question number greater than zero.", Toast.LENGTH_SHORT)
                            .show();
                    System.out.println("Enter a question number greater than zero.");
                    return;
                } else if (numQuestions > currentTest.wordGroup.wordList.size()) {
                    Toast.makeText(GlobalApplication.getAppContext(),
                            "Number of questions is greater than words in group.",
                            Toast.LENGTH_SHORT).show();
                    System.out.println("Number of questions is greater than words in group.");
                    return;
                }


                //0.5b article, 1b base, 0.5b plural
                currentTest.maxPoints = numQuestions * 2;
                currentTest.numQuestions = numQuestions;

                Intent i = new Intent(GlobalApplication.getAppContext(), TestActivity.class);
                i.putExtra("CURRENT_TEST", currentTest);
                startActivity(i);
                finish();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            int result;
            if (resultCode == RESULT_OK) {
                try {
                    result = data.getIntExtra("RESULT", -1);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    result = -1;
                }

                if (result > -1) {
                    currentTest.wordGroup = Data.wordGroupList.get(result);
                    chooseGroup.setText(currentTest.wordGroup.groupName);

                }
            } else {
                if (Data.wordGroupList.size() != 0) {
                    Toast.makeText(GlobalApplication.getAppContext(),
                            "Choice unsuccessful. Try Again.", Toast.LENGTH_SHORT).show();
                    System.out.println("Choice unsuccessful. Try Again.");
                }
                finish();
            }
        }
    }
}