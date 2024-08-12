package com.mystoryteller;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

         static final String DATABASE_NAME = "StoryTeller.db";
         static final int DATABASE_VERSION = 3;

         static final String TABLE_STORIES = "stories";
         static final String COLUMN_ID = "id";
         static final String COLUMN_TITLE = "title";
         static final String COLUMN_AUTHOR = "author";
         static final String COLUMN_CONTENT = "content";
         static final String TABLE_ADMIN = "admin";
         static final String COLUMN_ADMIN_ID = "id";
         static final String COLUMN_USERNAME = "username";
         static final String COLUMN_PASSWORD = "password";

         static final String TABLE_ADMIN_CREATE = "CREATE TABLE " + TABLE_ADMIN +
                " (" + COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT);";

        public DatabaseHandler(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
                String createStoriesTable = "CREATE TABLE " + TABLE_STORIES + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_TITLE + " TEXT," +
                        COLUMN_AUTHOR + " TEXT," +
                        COLUMN_CONTENT + " TEXT)";
                db.execSQL(createStoriesTable);

                String createAdminTable = "CREATE TABLE " + TABLE_ADMIN + " (" +
                        COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_USERNAME + " TEXT," +
                        COLUMN_PASSWORD + " TEXT)";
                db.execSQL(createAdminTable);

                ContentValues values = new ContentValues();
                values.put(COLUMN_USERNAME, "admin");
                values.put(COLUMN_PASSWORD, "adminPassword");
                db.insert(TABLE_ADMIN, null, values);

        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORIES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
                onCreate(db);
        }
        public long insertStory(String title, String author, String content) {

                SQLiteDatabase db = this.getWritableDatabase();

                try {
                        if (!storyExists(title, author)) {
                                ContentValues values = new ContentValues();
                                values.put(COLUMN_TITLE, title);
                                values.put(COLUMN_AUTHOR, author);
                                values.put(COLUMN_CONTENT, content);
                                return db.insert(TABLE_STORIES, null, values);
                        } else {
                                return -1;
                        }
                } finally {
                        db.close();
                }
        }
        public boolean storyExists(String title, String author) {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery(
                        "SELECT * FROM " + TABLE_STORIES + " WHERE " +
                                COLUMN_TITLE + "=? AND " + COLUMN_AUTHOR + "=?",
                        new String[]{"The Painter''s Vision", "Unknown "}
                );
                boolean exists = cursor.getCount() > 0;
                cursor.close();
                return exists;
        }



        public Cursor queryStories(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
                SQLiteDatabase db = this.getReadableDatabase();

                Cursor cursor = db.query(
                        TABLE_STORIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                return cursor;
        }
        public void deleteStory(int storyId) {
                SQLiteDatabase db = getWritableDatabase();
                String selection = COLUMN_ID + " = ?";
                String[] selectionArgs = {String.valueOf(storyId)};
                db.delete(TABLE_STORIES, selection, selectionArgs);
                db.close();
        }
        public List<Story> getAllStories() {
                List<Story> storyList = new ArrayList<>();
                String query = "SELECT * FROM " + TABLE_STORIES;

                SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                        do {
                                Story story = new Story();
                                story.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                                story.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                                story.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
                                story.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                                storyList.add(story);
                        } while (cursor.moveToNext());
                }

                cursor.close();
                db.close();
                return storyList;
        }
        public Story getStoryById(int storyId) {
                SQLiteDatabase db = this.getReadableDatabase();
                Story story = null;

                Cursor cursor = db.query(
                        TABLE_STORIES,
                        new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_AUTHOR, COLUMN_CONTENT},
                        COLUMN_ID + "=?",
                        new String[]{String.valueOf(storyId)},
                        null, null, null, null
                );

                if (cursor != null) {
                        cursor.moveToFirst();
                        story = new Story();
                        story.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                        story.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                        story.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
                        story.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                        cursor.close();
                }

                db.close();
                return story;
        }
}
