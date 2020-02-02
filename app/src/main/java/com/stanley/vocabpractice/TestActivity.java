package com.stanley.vocabpractice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class TestActivity extends AppCompatActivity {

    private Test currentTest;
    private int currentQuestion;
    private boolean testDone = false;
    private EditText articleEdit, baseEdit, pluralEdit;
    private TextView translationText, inputWordText, rightWordText, pointsText, questionNumberText;
    private Button nextButton;
    private AlertDialog alert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        currentTest = (Test) getIntent().getSerializableExtra("CURRENT_TEST");

        onBack();

        getRandomWords(currentTest.fromMistakes);


        currentQuestion = 0;
        articleEdit = findViewById(R.id.testArticleEdit);
        baseEdit = findViewById(R.id.testBaseEdit);
        pluralEdit = findViewById(R.id.testPluralEdit);

        articleEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (baseEdit.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        articleEdit.clearFocus();
                    }
                    handled = true;
                }
                return handled;
            }
        });

        baseEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (pluralEdit.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        baseEdit.clearFocus();
                    }
                    handled = true;
                }
                return handled;
            }
        });

        pluralEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (nextButton.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        pluralEdit.clearFocus();
                    }
                    handled = true;
                }
                return handled;
            }
        });

        inputWordText = findViewById(R.id.inputWordText);
        rightWordText = findViewById(R.id.rightWordText);
        pointsText = findViewById(R.id.pointsText);
        translationText = findViewById(R.id.wordToTranslateText);
        questionNumberText = findViewById(R.id.questionNumberText);

        String questionNumString = "Question: " + (currentQuestion + 1) + " out of " +
                currentTest.numQuestions;
        questionNumberText.setText(questionNumString);
        translationText.setText(currentTest.wordsUsed.get(currentQuestion).translation);

        nextButton = findViewById(R.id.testNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


    }

    private void onBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to close the test?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        alert = builder.create();


    }

    @Override
    public void onBackPressed() {
        alert.show();
    }

    private void check() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        if (testDone) {
            Data.testList.add(currentTest);
            LoadingActivity.writeTestsToFile();
            int testID = Data.testList.indexOf(currentTest);
            Intent i = new Intent(GlobalApplication.getAppContext(),
                    TestResultActivity.class);
            i.putExtra("TEST_ID", testID);
            startActivity(i);
            finish();
        } else {
            Word inputWord = new Word();
            inputWord.article = articleEdit.getText().toString().replace(" ",
                    "");
            inputWord.base = baseEdit.getText().toString();
            inputWord.plural = pluralEdit.getText().toString().replace(" ",
                    "");
            currentTest.wordsInput.add(inputWord);
            currentTest.points.add(checkWord(inputWord, currentTest.wordsUsed
                    .get(currentQuestion)));
            currentTest.pointsCount += checkWord(inputWord, currentTest.wordsUsed
                    .get(currentQuestion));

            articleEdit.setText("");
            baseEdit.setText("");
            pluralEdit.setText("");

            String translationString = currentTest.wordsUsed.get(currentQuestion).translation;
            translationText.setText(translationString);
            String inputWordString = "Answer: " + inputWord.article + " " + inputWord.base
                    + " " + inputWord.plural;
            inputWordText.setText(inputWordString);
            String rightWordString = "Right answer: " +
                    currentTest.wordsUsed.get(currentQuestion).article +
                    " " + currentTest.wordsUsed.get(currentQuestion).base + " " +
                    currentTest.wordsUsed.get(currentQuestion).plural;
            rightWordText.setText(rightWordString);
            String pointsString = "Points: " + currentTest.points.get(currentQuestion) +
                    "/2";
            pointsText.setText(pointsString);

            if ((currentQuestion + 1) >= currentTest.numQuestions) {
                System.out.println(currentTest.numQuestions + " " + currentQuestion);
                nextButton.setText(getResources().getString(R.string.test_finish));
                testDone = true;
                articleEdit.setEnabled(false);
                baseEdit.setEnabled(false);
                pluralEdit.setEnabled(false);
            } else {
                currentQuestion += 1;
                String questionNumString = "Question: " + (currentQuestion + 1) + " out of " +
                        currentTest.numQuestions;
                questionNumberText.setText(questionNumString);
                translationText.setText(currentTest.wordsUsed.get(currentQuestion).translation);

            }
        }
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    private int checkWord(Word inputWord, Word ogWord) {
        float result = 0;

        if (inputWord.article.equals("") && inputWord.base.equals("")
                && inputWord.plural.equals("")) {
            return 0;
        }

        if (ogWord.article.toLowerCase().equals(inputWord.article.toLowerCase())) {
            result += 0.5;
        } else {
            if (ogWord.phrase && !inputWord.base.equals("")) {
                result += 0.5;
            }
            System.out.println("wrong article");
        }
        if (ogWord.base.toLowerCase().equals(inputWord.base.toLowerCase())) {
            result += 1;

        } else {
            System.out.println("wrong base");
        }
        if (ogWord.plural.toLowerCase().equals(inputWord.plural.toLowerCase())) {
            result += 0.5;
        } else {
            if (ogWord.phrase && !inputWord.base.equals("")) {
                result += 0.5;
            }
            System.out.println("wrong plural");
        }
        return Math.round(result);
    }

    private void getRandomWords(boolean fromMistakes) {

        if (fromMistakes) {
            for (int i = 0; i < currentTest.lastTest.numQuestions; i++) {
                if (currentTest.lastTest.points.get(i) < 2) {
                    currentTest.wordsUsed.add(currentTest.lastTest.wordsUsed.get(i));
                    currentTest.numQuestions++;
                }
            }

            currentTest.maxPoints = currentTest.numQuestions * 2;
        }

        for (int i = 0; i < currentTest.numQuestions; i++) {
            Random rand = new Random();
            Word randomWord = currentTest.wordGroup.wordList.get(rand.nextInt(
                    currentTest.wordGroup.wordList.size()));
            while (currentTest.wordsUsed.indexOf(randomWord) != -1) {
                randomWord = currentTest.wordGroup.wordList.get(rand.nextInt(
                        currentTest.wordGroup.wordList.size()));
            }
            currentTest.wordsUsed.add(randomWord);
        }
    }
}
