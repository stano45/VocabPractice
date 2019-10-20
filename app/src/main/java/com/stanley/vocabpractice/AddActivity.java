package com.stanley.vocabpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private WordGroup wordGroup = new WordGroup();
    private int wordGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent i = new Intent(GlobalApplication.getAppContext(), GroupSelectActivity.class);
        i.putExtra("requestId", 1);
        startActivityForResult(i, 1);

        final EditText article = findViewById(R.id.article_edit);
        final EditText base = findViewById(R.id.base_edit);
        final EditText plural = findViewById(R.id.plural_edit);
        final EditText translation = findViewById(R.id.translation_edit);

        final Button next = findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word w = new Word();
                w.article = article.getText().toString().replace(" ", "");
                if (w.article.equals("")) {
                    w.article = "-";
                }
                w.base = base.getText().toString();
                w.plural = plural.getText().toString().replace(" ", "");
                if (w.plural.equals("")) {
                    w.plural = "-";
                }
                w.translation = translation.getText().toString().replace(" ", "");
                wordGroup.wordList.add(w);
                LoadingActivity.writeWordsToFile();
                article.setText("");
                base.setText("");
                plural.setText("");
                translation.setText("");

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
