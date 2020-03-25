package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView list;
    ArrayAdapter<Item> adapter;
    ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity","create");
        createList();
    }

    void createList() {
        list = (ListView) findViewById(R.id.list);

        items = new ArrayList<>();

        items.add(new Item("food"));
        items.add(new Item("bicycle"));
        items.add(new Item("ball"));

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /*Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
                intent.putExtra("item", items.get(position));
                startActivity(intent);*/
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.putExtra("item", items.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity","restart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity","start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity","resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity","pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity","stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity","destroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstancestate) {
        super.onSaveInstanceState(savedInstancestate);
        savedInstancestate.putString("key","123456");
        Log.i("MainActivity","save instance");

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle restoreInstanceState) {
        super.onRestoreInstanceState(restoreInstanceState);
        Log.i("MainActivity", restoreInstanceState.getString("key"));
        Log.i("MainActivity","restore instance");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.login:
                DialogFragment newFragment = new LoginDialog();
                newFragment.show(getSupportFragmentManager(), "login");
                return true;
            case R.id.cart:
                return true;
            case R.id.contact:
                return true;
            case R.id.settings:
                /*getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment())
                        .commit();*/
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
