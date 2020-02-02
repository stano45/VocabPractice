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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class GroupSelectActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView list;
    private ArrayAdapter<String> adapter;
    private TextView selectText;
    int rqid;
    int position_id;
    boolean longClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);

        //rqid 1: from addActivity, 2: from viewWords, 3: from viewTestResults, 4:from selectTest
        rqid = getIntent().getIntExtra("requestId", -200);
        System.out.println("rqid: " + rqid);
        selectText = findViewById(R.id.selectText);

        adapter = new ArrayAdapter<>(this, R.layout.text_layout_groups);
        setAdapter(rqid);


        if (rqid == 2 || rqid == 3) {
            setRemove(rqid);
        }

    }

    private void setAdapter(int rqid) {
        adapter.clear();
        if (rqid == 1 || rqid == 2 || rqid == 4) {
            String sText = "Select a group: ";
            selectText.setText(sText);
            if (Data.wordGroupList != null) {
                for (WordGroup w : Data.wordGroupList) {
                    if (w.groupName != null) {
                        adapter.add(w.groupName);
                    }
                }
            }

            if (rqid == 1) {
                adapter.add("Add new...");
            } else {
                if (adapter.getCount() == 0) {
                    Toast.makeText(GlobalApplication.getAppContext(),
                            "No word groups. Add them with the 'add words' button.",
                            Toast.LENGTH_SHORT).show();
                    System.out.println("No word groups. Add them with the 'add words' button.");
                    finish();
                }
            }
        } else {
            String sText = "Select a test: ";
            selectText.setText(sText);
            if (Data.testList != null) {
                for (Test t : Data.testList) {
                    adapter.add(t.wordGroup.groupName + " " + t.pointsCount + "/" + t.maxPoints);
                }
            }
            if (adapter.getCount() == 0) {
                Toast.makeText(GlobalApplication.getAppContext(),
                        "No finished tests. Start a test by pressing 'Start a test'.",
                        Toast.LENGTH_SHORT).show();
                System.out.println("No finished tests. Start a test by pressing 'Start a test'.");
                finish();
            }
        }

        list = findViewById(R.id.groupSelector);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    private void setRemove(final int rqid) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete " + adapter.getItem(position_id)
                + "?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                if (rqid == 2) {
                    Data.wordGroupList.remove(position_id);
                } else {
                    Data.testList.remove(position_id);
                }
                LoadingActivity.writeWordsToFile();
                setAdapter(rqid);
                dialog.dismiss();
                longClicked = false;
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


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClicked = true;
                position_id = position;
                alert.show();
                return false;
            }
        });


    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {

        if (longClicked) return;
        System.out.println("pressed:" + position);

        List<?> list;
        if (rqid == 3) {
            list = Data.testList;
        } else {
            list = Data.wordGroupList;
        }

        Intent intent = new Intent();
        if (list == null) {
            intent.putExtra("RESULT", -1);
        } else {
            if (position >= list.size()) {
                intent.putExtra("RESULT", -1);
            } else {
                intent.putExtra("RESULT", position);
            }
        }

        setResult(RESULT_OK, intent);
        finish();


    }
}
