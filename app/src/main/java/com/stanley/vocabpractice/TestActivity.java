package com.stanley.vocabpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class TestActivity extends AppCompatActivity {

    private Test currentTest;
    private int currentQuestion;
    private boolean testDone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        currentTest = (Test) getIntent().getSerializableExtra("CURRENT_TEST");

        getRandomWords();

        currentQuestion = 0;
        final EditText articleEdit = findViewById(R.id.testArticleEdit);
        final EditText baseEdit = findViewById(R.id.testBaseEdit);
        final EditText pluralEdit = findViewById(R.id.testPluralEdit);

        final TextView inputWordText = findViewById(R.id.inputWordText);
        final TextView rightWordText = findViewById(R.id.rightWordText);
        final TextView pointsText = findViewById(R.id.pointsText);

        final TextView questionNumberText = findViewById(R.id.questionNumberText);
        final TextView translationText = findViewById(R.id.wordToTranslateText);
        String questionNumString = "Question: " + (currentQuestion + 1) + " out of " +
                currentTest.numQuestions;
        questionNumberText.setText(questionNumString);
        translationText.setText(currentTest.wordsUsed.get(currentQuestion).translation);

        final Button nextButton = findViewById(R.id.testNextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                    translationText.setText(currentTest.wordsUsed.get(currentQuestion).translation);
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


            }
        });


    }


    private int checkWord(Word inputWord, Word ogWord) {
        float result = 0;
        if (ogWord.article.toLowerCase().equals(inputWord.article.toLowerCase())) {
            result += 0.5;
        } else {
            if (ogWord.phrase) {
                result += 0.5;
            }
            System.out.println("w article");
        }
        if (ogWord.base.toLowerCase().equals(inputWord.base.toLowerCase())) {
            result += 1;

        } else {
            System.out.println("w base");
        }
        if (ogWord.plural.toLowerCase().equals(inputWord.plural.toLowerCase())) {
            result += 0.5;
        } else {
            if (ogWord.phrase) {
                result += 0.5;
            }
            System.out.println("w plural");
        }
        return Math.round(result);
    }

    private void getRandomWords() {
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
