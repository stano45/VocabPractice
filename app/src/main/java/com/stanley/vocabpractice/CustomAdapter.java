package com.stanley.vocabpractice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

//CustomAdapter for showing test results
public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<String> rightWordList;
    private List<String> inputWordList;
    private List<String> pointsList;
    private LayoutInflater inflater;

    CustomAdapter(Context applicationContext, List<String> rightWordList,
                  List<String> inputWordList, List<String> pointsList) {
        this.context = applicationContext;
        this.rightWordList = rightWordList;
        this.inputWordList = inputWordList;
        this.pointsList = pointsList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return rightWordList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.text_layout_test, null);
        TextView rightWords = view.findViewById(R.id.rightWord);
        TextView inputWords = view.findViewById(R.id.inputWord);
        TextView points = view.findViewById(R.id.points);
        String helpString = "Right answer: " + rightWordList.get(i);
        rightWords.setText(helpString);
        helpString = "Your answer: " + inputWordList.get(i);
        inputWords.setText(helpString);
        helpString = "Points: " + pointsList.get(i);
        points.setText(helpString);
        return view;
    }
}