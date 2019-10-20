package com.stanley.vocabpractice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    private static Context context = GlobalApplication.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String toastMessage;
        if (readWordsFromFile() == -1 || readTestsFromFile() == -1) {
            toastMessage = "Unable to read from saved data.";
        } else {
            toastMessage = "Data successfully read.";
            for (WordGroup w : Data.wordGroupList) {
                System.out.println(w.groupName);
            }
        }

        Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);
        toast.show();

        Intent i = new Intent(GlobalApplication.getAppContext(), MenuActivity.class);
        startActivity(i);
        finish();
    }

    public static int readWordsFromFile() {
        File file = new File(context.getFilesDir(), "data.ser");
        if (file.exists() && !file.isDirectory()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Data.wordGroupList = (List<WordGroup>) ois.readObject();
                ois.close();
                System.out.println("Words read.");
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public static int readTestsFromFile() {
        File file = new File(context.getFilesDir(), "tests.ser");
        if (file.exists() && !file.isDirectory()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Data.testList = (List<Test>) ois.readObject();
                ois.close();
                System.out.println("Tests read.");
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public static int writeWordsToFile() {
        File file = new File(context.getFilesDir(), "data.ser");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Data.wordGroupList);
            oos.close();
            System.out.println("Words written.");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int writeTestsToFile() {
        File file = new File(context.getFilesDir(), "tests.ser");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Data.testList);
            oos.close();
            System.out.println("Tests written.");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
