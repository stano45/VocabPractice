package com.stanley.vocabpractice;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TestResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        //get selected test
        Test test;
        test = Data.testList.get(getIntent().getIntExtra("TEST_ID", 0));

        TextView testID = findViewById(R.id.testGroupID);
        TextView testPoints = findViewById(R.id.testPoints);

        //set name of word group
        String testIDString = "Group: " + test.wordGroup.groupName;
        testID.setText(testIDString);

        //set number of achieved points
        String testPointsString = "Points: " + test.pointsCount + "/" + test.maxPoints;
        testPoints.setText(testPointsString);

        List<String> rightWords = new ArrayList<>();
        List<String> inputWords = new ArrayList<>();
        List<String> points = new ArrayList<>();

        System.out.println(test.wordsUsed.size() + " " + test.wordsInput.size() + " " +
                +test.points.size());

        //add wrongly answered questions to list
        for (int i = 0; i < test.points.size(); i++) {
            if (test.points.get(i) < 2) {
                rightWords.add(test.wordsUsed.get(i).article + " " + test.wordsUsed.get(i).base
                        + " " + test.wordsUsed.get(i).plural);
                inputWords.add(test.wordsInput.get(i).article + " " + test.wordsInput.get(i).base
                        + " " + test.wordsInput.get(i).plural);
                points.add(test.points.get(i) + "/2");
            }
        }

        //display wrongly answered words in list
        CustomAdapter cAdpt = new CustomAdapter(GlobalApplication.getAppContext(), rightWords,
                inputWords, points);

        ListView resultList = findViewById(R.id.testList);
        resultList.setAdapter(cAdpt);
    }
}
