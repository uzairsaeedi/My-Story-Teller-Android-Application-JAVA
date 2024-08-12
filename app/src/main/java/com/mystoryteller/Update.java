package com.mystoryteller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Update extends AppCompatActivity {
    EditText edt_title, edt_content, edt_author;
    Button btn_update;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        edt_title = findViewById(R.id.edt_title);
        edt_content = findViewById(R.id.edt_content);
        edt_author = findViewById(R.id.edt_author);
        btn_update = findViewById(R.id.btn_update);
        db = new DatabaseHandler(this);
        int storyId = getIntent().getIntExtra("story_id", -1);

        Story existingStory = getStoryFromDatabase(storyId);

        if (existingStory != null) {
            edt_title.setText(existingStory.getTitle());
            edt_content.setText(existingStory.getContent());
            edt_author.setText(existingStory.getAuthor());
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String modifiedTitle = edt_title.getText().toString();
                    String modifiedContent = edt_content.getText().toString();
                    String modifiedAuthor = edt_author.getText().toString();

                    SQLiteDatabase database = db.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHandler.COLUMN_TITLE, modifiedTitle);
                    values.put(DatabaseHandler.COLUMN_AUTHOR, modifiedAuthor);
                    values.put(DatabaseHandler.COLUMN_CONTENT, modifiedContent);

                    String selection = DatabaseHandler.COLUMN_ID + " = ?";
                    String[] selectionArgs = {String.valueOf(storyId)};

                    int rowsUpdated = database.update(
                            DatabaseHandler.TABLE_STORIES,
                            values,
                            selection,
                            selectionArgs
                    );

                    if (rowsUpdated > 0) {
                        Toast.makeText(Update.this, "Story updated successfully", Toast.LENGTH_SHORT).show();
                       Intent intent =new Intent(Update.this,AdminDashboardActivity.class);
                       startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Update.this, "Failed to update story", Toast.LENGTH_SHORT).show();
                    }

                    database.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private Story getStoryFromDatabase(int storyId) {

        SQLiteDatabase database = db.getReadableDatabase();

        String[] projection = {
                DatabaseHandler.COLUMN_ID,
                DatabaseHandler.COLUMN_TITLE,
                DatabaseHandler.COLUMN_AUTHOR,
                DatabaseHandler.COLUMN_CONTENT
        };

        String selection = DatabaseHandler.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(storyId)};

        Cursor cursor = database.query(
                DatabaseHandler.TABLE_STORIES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Story story = null;

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_TITLE));
            String author = cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_AUTHOR));
            String content = cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CONTENT));

            story = new Story(id, title, author, content);

            cursor.close();
        }

        database.close();

        return story;
    }
    private boolean updateStoryInDatabase(int storyId, String title, String content, String author) {
        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COLUMN_TITLE, title);
        values.put(DatabaseHandler.COLUMN_AUTHOR, author);
        values.put(DatabaseHandler.COLUMN_CONTENT, content);

        String selection = DatabaseHandler.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(storyId)};

        int rowsUpdated = database.update(
                DatabaseHandler.TABLE_STORIES,
                values,
                selection,
                selectionArgs
        );

        database.close();

        return rowsUpdated > 0;
    }

}