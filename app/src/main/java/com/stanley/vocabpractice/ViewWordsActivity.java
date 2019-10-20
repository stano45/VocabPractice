package com.stanley.vocabpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewWordsActivity extends AppCompatActivity {

    private WordGroup group;
    private int groupId;
    private ListView list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        if (Data.wordGroupList.size() == 0) {
            Toast.makeText(GlobalApplication.getAppContext(),
                    "No word groups. Add them with the 'add words' button.",
                    Toast.LENGTH_SHORT).show();
            System.out.println("No word groups. Add them with the 'add words' button.");
            finish();
        } else {
            Intent i = new Intent(GlobalApplication.getAppContext(), GroupSelectActivity.class);
            i.putExtra("requestId", 2);
            startActivityForResult(i, 2);
        }
    }

    private void createList() {
        TextView groupName = findViewById(R.id.groupNameText);
        groupName.setText(group.groupName);
        adapter = new ArrayAdapter<>(this, R.layout.text_layout_words);

        if (group.wordList != null) {
            for (Word w : group.wordList) {
                adapter.add((group.wordList.indexOf(w) + 1) + ". " + w.article + " " + w.base + "- " +
                        w.plural + " (" + w.translation + ")");
            }
        }

        list = findViewById(R.id.wordViewer);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                group.wordList.remove(position);
                Data.wordGroupList.set(groupId, group);
                createList();
                return false;
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
                    groupId = result;
                    group = Data.wordGroupList.get(groupId);
                    createList();
                }
            } else {
                if (Data.wordGroupList.size() != 0) {
                    System.out.println("Choice unsuccessful. Try Again.");
                }
                finish();
            }
        }

    }
}
