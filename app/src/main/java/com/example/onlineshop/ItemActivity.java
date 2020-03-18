package com.example.onlineshop;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ItemActivity extends AppCompatActivity {

    TextView title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);
        Item item = (Item) getIntent().getSerializableExtra("item");
        title = (TextView) findViewById(R.id.title);
        title.setText(item.getName());
    }

}
