package com.stanley.vocabpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddGroup extends AppCompatActivity {

    boolean isPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        Button addButton = findViewById(R.id.group_add_button);
        final EditText nameField = findViewById(R.id.group_name_edit);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameField.getText().toString();

                boolean isEmpty = true;
                for (char c : name.toCharArray()) {
                    if (Character.isLetterOrDigit(c)) {
                        isEmpty = false;
                    }
                }

                if (isEmpty) {
                    Toast.makeText(GlobalApplication.getAppContext(),
                            "Name must contain an alphanumerical character.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                isPresent = false;
                System.out.println(name);
                if (Data.wordGroupList != null) {
                    for (WordGroup w : Data.wordGroupList) {
                        if (w.groupName.equals(name)) {
                            Toast.makeText(GlobalApplication.getAppContext(),
                                    "Name already exists. Try another one.",
                                    Toast.LENGTH_SHORT).show();
                            isPresent = true;
                            break;
                        }
                    }
                }

                if (!isPresent) {
                    WordGroup w = new WordGroup();
                    w.groupName = name;
                    Data.wordGroupList.add(w);
                    LoadingActivity.writeWordsToFile();
                    Intent i = new Intent();
                    i.putExtra("POS_OF_GROUP", Data.wordGroupList.indexOf(w));
                    System.out.println("pos: " + Data.wordGroupList.indexOf(w));
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });

    }
}
