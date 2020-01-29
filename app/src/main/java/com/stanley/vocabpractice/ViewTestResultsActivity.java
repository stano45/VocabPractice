package com.stanley.vocabpractice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class ViewTestResultsActivity extends Activity {

    int testID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test_results);
        Intent i = new Intent(GlobalApplication.getAppContext(), GroupSelectActivity.class);
        i.putExtra("requestId", 3);
        startActivityForResult(i, 3);
    }

    private void showTest() {
        Intent i = new Intent(GlobalApplication.getAppContext(), TestResultActivity.class);
        i.putExtra("TEST_ID", testID);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
            testID = data.getIntExtra("RESULT", -1);
            if (testID != -1) {
                showTest();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }
}
