package com.stanley.vocabpractice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ViewWordsActivity extends AppCompatActivity {

    private WordGroup group;
    private int groupId;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private int position_id;

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

    private boolean createList() {
        TextView groupName = findViewById(R.id.groupNameText);
        groupName.setText(group.groupName);
        adapter = new ArrayAdapter<>(this, R.layout.text_layout_words);

        if (group.wordList != null && group.wordList.size() != 0) {
            for (Word w : group.wordList) {
                adapter.add((group.wordList.indexOf(w) + 1) + ". " + w.article + " " + w.base + " " +
                        w.plural + " (" + w.translation + ")");
            }
            return true;
        } else {
            return false;
        }
    }

    private void setRemove() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete " + adapter.getItem(position_id)
                + "?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                group.wordList.remove(position_id);
                Data.wordGroupList.set(groupId, group);
                LoadingActivity.writeWordsToFile();
                createList();
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();


        list = findViewById(R.id.wordViewer);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                position_id = position;
                alert.show();
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
                    if (!createList()) {
                        Toast.makeText(GlobalApplication.getAppContext(),
                                "Word group empty.",
                                Toast.LENGTH_SHORT).show();
                        System.out.println("Word group empty.");
                        Intent i = new Intent(GlobalApplication.getAppContext(), GroupSelectActivity.class);
                        i.putExtra("requestId", 2);
                        startActivityForResult(i, 2);
                    } else {
                        setRemove();
                    }
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
