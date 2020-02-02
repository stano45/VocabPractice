package com.stanley.vocabpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//activity to add words/groups
public class AddActivity extends AppCompatActivity {

    //Word Group to add words into
    private WordGroup wordGroup = new WordGroup();

    //id of the word group in Data.wordGroupList
    private int wordGroupId;

    //references to used widgets
    private EditText article, base, plural, translation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //request group from GroupSelectActivity - rqCode: 1
        Intent i = new Intent(GlobalApplication.getAppContext(), GroupSelectActivity.class);
        i.putExtra("requestId", 1);
        startActivityForResult(i, 1);

        //set references to individual EditText fields
        article = findViewById(R.id.article_edit);
        base = findViewById(R.id.base_edit);
        plural = findViewById(R.id.plural_edit);
        translation = findViewById(R.id.translation_edit);

        //a set of onEditorActionListeners to move from field to field by pressing enter
        article.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (base.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams
                                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        article.clearFocus();
                    }
                    handled = true;
                }
                return handled;
            }
        });

        base.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (plural.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams
                                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        base.clearFocus();
                    }
                    handled = true;
                }
                return handled;
            }
        });

        plural.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (translation.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams
                                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        plural.clearFocus();
                    }
                    handled = true;
                }
                return handled;
            }
        });

        //last Listener adds all input text to corresponding variables
        translation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    addRoutine();
                    handled = true;
                }
                return handled;
            }
        });

        //a next button to do the same as last EditText field
        final Button next = findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoutine();

            }
        });

        //done button to write all info into storage and close the activity
        final Button done = findViewById(R.id.done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.wordGroupList.set(wordGroupId, wordGroup);
                LoadingActivity.writeWordsToFile();
                finish();
            }
        });
    }

    //method to add all parts of the word to their corresponding place
    private void addRoutine() {

        //a bunch of checks to see if the formatting is correct
        if (translation.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "No translation added!",
                    Toast.LENGTH_SHORT).show();
        } else if (base.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "No word added!",
                    Toast.LENGTH_SHORT).show();
        } else {
            //if formatting is correct, add it to a new Word variable
            Word w = new Word();

            //remove spaces from article
            w.article = article.getText().toString().replace(" ", "");

            //if there is no article, assume word is a phrase (only base is checked)
            if (w.article.equals("")) {
                w.article = "-";
                w.phrase = true;
            }

            w.base = base.getText().toString();
            w.plural = plural.getText().toString().replace(" ", "");
            if (w.plural.equals("")) {
                w.plural = "-";
            }

            //add complete word to list
            w.translation = translation.getText().toString();
            wordGroup.wordList.add(w);
            LoadingActivity.writeWordsToFile();

            //reset fields
            article.setText("");
            base.setText("");
            plural.setText("");
            translation.setText("");
        }
    }

    //set TextView field to current group name
    private void setGroupName() {
        TextView groupName = findViewById(R.id.group_name);
        groupName.setText(wordGroup.groupName);
    }

    //receive data from group selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int result;
                try {
                    result = data.getIntExtra("RESULT", -2);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    result = -2;
                }

                if (result == -2) {
                    System.out.println("Choice unsuccessful.");
                    finish();
                } else if (result == -1) {
                    Intent i = new Intent(GlobalApplication.getAppContext(), AddGroup.class);
                    startActivityForResult(i, 2);
                    finish();
                } else {
                    wordGroup = Data.wordGroupList.get(result);
                    wordGroupId = result;
                    setGroupName();
                }

            } else {
                System.out.println("Choice unsuccessful. Try Again.");
                finish();
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                int result;
                try {
                    result = data.getIntExtra("POS_OF_GROUP", -1);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    result = -1;
                }

                if (result == -1) {
                    System.out.println("Group creation unsuccessful. Try Again.");
                } else {
                    wordGroup = Data.wordGroupList.get(result);
                    wordGroupId = result;
                    setGroupName();
                }

            } else {
                System.out.println("Group creation unsuccessful. Try Again.");
            }
        }
    }

}
