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

//activity to customize tests
public class SelectTestActivity extends AppCompatActivity {

    //test to be customized
    private Test currentTest = new Test();

    //TextView to select groups
    private TextView chooseGroup;

    //EditText to select number of questions
    private EditText numQuestionsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_test);

        //if no groups are present, then return
        if (Data.wordGroupList.size() == 0) {
            Toast.makeText(GlobalApplication.getAppContext(),
                    "No word groups. Add them with the 'add words' button.",
                    Toast.LENGTH_SHORT).show();
            System.out.println("No word groups. Add them with the 'add words' button.");
            finish();
        }

        //chooseGroup text links to activity where user picks groups
        chooseGroup = findViewById(R.id.groupChoice);
        chooseGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GlobalApplication.getAppContext(), GroupSelectActivity.class);
                i.putExtra("requestId", 4);
                startActivityForResult(i, 4);
            }
        });

        //button for testing from mistakes
        Button testMistakes = findViewById(R.id.testMistakes);
        testMistakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if group has not been selected, notify the user
                if (currentTest.wordGroup == null) {
                    Toast.makeText(GlobalApplication.getAppContext(),
                            "Select a group.", Toast.LENGTH_SHORT)
                            .show();
                    System.out.println("Select a group.");
                } else {
                    //find last test with mistakes for testing
                    boolean found = false;
                    Test last = null;
                    for (Test t : Data.testList) {
                        if (t.wordGroup.groupName.equals(currentTest.wordGroup.groupName)) {
                            if (t.pointsCount < t.maxPoints) {
                                found = true;
                                last = t;
                            }
                        }
                    }

                    //if not found, inform user
                    if (!found) {
                        Toast.makeText(GlobalApplication.getAppContext(),
                                "No sufficient tests from group to determine mistakes",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                    //if found, start test
                    else {
                        currentTest.fromMistakes = true;
                        currentTest.lastTest = last;
                        Intent i = new Intent(GlobalApplication.getAppContext(),
                                TestActivity.class);
                        i.putExtra("CURRENT_TEST", currentTest);
                        startActivity(i);
                        finish();
                    }
                }

            }
        });

        numQuestionsEdit = findViewById(R.id.numQuestions);
        Button startTest = findViewById(R.id.startTestButton);
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the desired number of questions from field
                int numQuestions;
                if (numQuestionsEdit.getText().toString().length() != 0) {
                    numQuestions = Integer.parseInt(numQuestionsEdit.getText().toString());
                } else {
                    numQuestions = -1;
                }


                //check if a group has been selected
                if (currentTest.wordGroup == null) {
                    Toast.makeText(GlobalApplication.getAppContext(),
                            "Select a group.", Toast.LENGTH_SHORT)
                            .show();
                    System.out.println("Select a group.");
                    return;
                }

                //if number is lower than zero, inform user
                if (numQuestions <= 0) {
                    Toast.makeText(GlobalApplication.getAppContext(),
                            "Enter a question number greater than zero.", Toast.LENGTH_SHORT)
                            .show();
                    System.out.println("Enter a question number greater than zero.");
                    return;
                }
                //else if number is greater than number of words in group, inform user
                else if (numQuestions > currentTest.wordGroup.wordList.size()) {
                    Toast.makeText(GlobalApplication.getAppContext(),
                            "Number of questions is greater than words in group. (" +
                                    currentTest.wordGroup.wordList.size() + ")",
                            Toast.LENGTH_SHORT).show();
                    System.out.println("Number of questions is greater than words in group.");
                    return;
                }


                //each word consists of 2 points -  - 0.5 article, 1 base, 0.5 plural
                int pointsPerWord = 2;
                //set max points according to the grading scale
                currentTest.maxPoints = numQuestions * pointsPerWord;
                //set the num of questions
                currentTest.numQuestions = numQuestions;

                //continue on to the TestActivity with all data
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

        //rqCode 4 - return from group choice
        if (requestCode == 4) {
            int result;
            //check if choice ran correctly
            if (resultCode == RESULT_OK) {
                try {
                    //attempt to get result
                    result = data.getIntExtra("RESULT", -1);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    result = -1;
                }

                //if result is in right format (data extracted correctly)
                if (result > -1) {
                    //set the current Test's word group to the selected one
                    currentTest.wordGroup = Data.wordGroupList.get(result);
                    chooseGroup.setText(currentTest.wordGroup.groupName);
                    //set the num of questions field to the num of questions in word group
                    String q = Integer.toString(currentTest.wordGroup.wordList.size());
                    //this is here to set the cursor at the end of the string in field
                    numQuestionsEdit.setText("");
                    numQuestionsEdit.append(q);
                }
            }
        }
    }
}
