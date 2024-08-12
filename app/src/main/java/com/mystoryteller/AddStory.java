package com.mystoryteller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class AddStory extends AppCompatActivity {
    EditText edt_title,edt_content,edt_author;
    StoryAdapter Adapter;
    ImageView btn_back;
    DatabaseHandler db =new DatabaseHandler(this);;
    Button btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);
        edt_author = findViewById(R.id.edt_author);
        edt_content = findViewById(R.id.edt_content);
        edt_title = findViewById(R.id.edt_title);
        btn_add = findViewById(R.id.btn_add);
        btn_back = findViewById(R.id.btn_back);

        Adapter = new StoryAdapter(this, getStoriesFromDatabase());

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edt_title.getText().toString();
                String author = edt_author.getText().toString();
                String content = edt_content.getText().toString();

                long newRowId = insertDataIntoDatabase(title, author, content);

                if (newRowId != -1) {
                    Adapter.updateData(getStoriesFromDatabase());
                    edt_title.setText("");
                    edt_author.setText("");
                    edt_content.setText("");
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private List<Story> getStoriesFromDatabase() {
        List<Story> stories = db.getAllStories();
        return stories;
    }
    private long insertDataIntoDatabase(String title, String author, String content) {
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COLUMN_TITLE, title);
        values.put(DatabaseHandler.COLUMN_AUTHOR, author);
        values.put(DatabaseHandler.COLUMN_CONTENT, content);

        long newRowId = db.insertStory(title, author, content);

        if (newRowId != -1) {
            Toast.makeText(this, "Story Added Successfully", Toast.LENGTH_SHORT).show();
            Intent intent =  new Intent(AddStory.this,AdminDashboardActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed !", Toast.LENGTH_SHORT).show();
        }

        db.close();
        return newRowId;

    }
}