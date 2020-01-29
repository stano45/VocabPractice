package com.stanley.vocabpractice;

import android.app.Activity;
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

public class AddActivity extends Activity {

    private WordGroup wordGroup = new WordGroup();
    private int wordGroupId;
    private EditText article, base, plural, translation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent i = new Intent(GlobalApplication.getAppContext(), GroupSelectActivity.class);
        i.putExtra("requestId", 1);
        startActivityForResult(i, 1);

        article = findViewById(R.id.article_edit);
        base = findViewById(R.id.base_edit);
        plural = findViewById(R.id.plural_edit);
        translation = findViewById(R.id.translation_edit);

        article.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (base.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        plural.clearFocus();
                    }
                    handled = true;
                }
                return handled;
            }
        });

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

        final Button next = findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addRoutine();

            }
        });

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

    private void addRoutine() {
        if (translation.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "No translation added!",
                    Toast.LENGTH_SHORT).show();
        } else if (base.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "No word added!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Word w = new Word();
            w.article = article.getText().toString().replace(" ", "");
            if (w.article.equals("")) {
                w.article = "-";
                w.phrase = true;
            }
            w.base = base.getText().toString();
            w.plural = plural.getText().toString().replace(" ", "");
            if (w.plural.equals("")) {
                w.plural = "-";
            }
            w.translation = translation.getText().toString();
            wordGroup.wordList.add(w);
            LoadingActivity.writeWordsToFile();
            article.setText("");
            base.setText("");
            plural.setText("");
            translation.setText("");
        }
    }

    private void startAdding() {
        TextView groupName = findViewById(R.id.group_name);
        groupName.setText(wordGroup.groupName);
    }

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
                    startAdding();
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
                    startAdding();
                }

            } else {
                System.out.println("Group creation unsuccessful. Try Again.");
            }
        }
    }

}
