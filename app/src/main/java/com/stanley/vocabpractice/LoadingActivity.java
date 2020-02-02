package com.stanley.vocabpractice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
        this.setContentView(R.layout.activity_main);

        //forces app into night mode
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);


        //attempts to read words and tests from storage

        String toastMessage;
        if (readWordsFromFile() == -1) {

            //if it doesn't find them, notifies the user
            toastMessage = "Unable to read words from saved data.";

        } else if (readTestsFromFile() == -1) {
            toastMessage = "Unable to read tests from saved data.";

        } else {

            //if it does find them, prints them out
            toastMessage = "Data successfully read.";
            for (WordGroup w : Data.wordGroupList) {
                System.out.println(w.groupName);
            }
        }

        //shows state message
        Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);
        toast.show();

        //once everything is loaded, starts menu activity
        Intent i = new Intent(GlobalApplication.getAppContext(), MenuActivity.class);
        startActivity(i);
        finish();
    }

    //method for reading words from storage
    public static int readWordsFromFile() {
        //creates path to file
        File file = new File(context.getFilesDir(), "data.ser");

        //if file exists
        if (file.exists() && !file.isDirectory()) {
            try {

                //attempts to read from existing file
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                //loads data into memory (Data class)
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

    //reads tests from file, same as with words
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

    //method to write words into storage
    public static int writeWordsToFile() {

        //creates path to file
        File file = new File(context.getFilesDir(), "data.ser");
        try {

            //attempts to write to file
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

    //method to write tests to file, same as with words
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
