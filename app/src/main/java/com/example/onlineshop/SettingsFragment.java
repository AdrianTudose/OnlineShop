package com.example.onlineshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends PreferenceFragmentCompat {
    SharedPreferences sharedpreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getContext(),load("preferences.txt"),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        get_preferences();
    }

    public void get_preferences() {
        //folosind PreferenceFragment , preferintele sunt salvate automat cu SharedPreferences :
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean notifications = prefs.getBoolean("notifications", true);
        boolean rememberAccount = prefs.getBoolean("remember_account", true);

        // Salvam
        String text;
        text = "notifications:" + notifications + "\n" +
                "remember_account:" + rememberAccount;
        save(text,"preferences.txt");
    }

    public void save(String text,String fileName) {
        FileOutputStream stream=null;
        try {
            stream = getContext().openFileOutput(fileName, MODE_PRIVATE);
            stream.write(text.getBytes());
            Toast.makeText(getContext(),"Data saved successfully.", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(getContext(),"File not found!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getContext(),"Cannot write to file!",Toast.LENGTH_LONG).show();
        }
        finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String load(String fileName)
    {
        FileInputStream stream =null;
        try {
            stream = getContext().openFileInput(fileName);
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line= reader.readLine())!=null)
            {
                stringBuilder.append(line).append("\n");
            }

            Toast.makeText(getContext(),"Data loaded.",Toast.LENGTH_LONG).show();
            return stringBuilder.toString();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(getContext(),"File not found!", Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Toast.makeText(getContext(),"Cannot read from file!",Toast.LENGTH_LONG).show();
        }
        finally {

            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}