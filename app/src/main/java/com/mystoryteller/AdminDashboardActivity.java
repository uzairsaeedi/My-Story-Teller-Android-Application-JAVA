package com.mystoryteller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    RecyclerView rv_stories;
    StoryAdapterAdmin Adapter;
    DatabaseHandler db = new DatabaseHandler(this);
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        rv_stories = findViewById(R.id.rv_stories);

        long newRowId = db.insertStory("The Lost Key", "Folklore", "Once, a man lost the key to his house. He searched for it high and low but couldn't find it. Frustrated, he decided to sit on his porch and think. As he sat there, he noticed a squirrel on a nearby tree, watching him. The squirrel had the lost key in its mouth. The man couldn't help but smile at the sight. It turned out that the squirrel had found the key and brought it back to him. He realized that sometimes, solutions to our problems come from unexpected sources.");
        long newRowId1 = db.insertStory("The Talking Tree", "Fable", "In a deep forest, there stood a tree known as the \"Whispering Oak.\" It was said that if you listened carefully, you could hear the tree talk. Many people came from far and wide to listen to its wisdom. One day, a young boy asked the tree for advice on finding happiness. The tree replied, \"Happiness lies not in seeking it but in appreciating the beauty of the forest that surrounds you.\" The boy left with a newfound perspective, understanding that happiness is often found in the present moment.");
        long newRowId2 = db.insertStory("The Ant and The Grasshopper", "Aesop", "In a meadow, an ant worked tirelessly, storing food for the winter. The grasshopper, on the other hand, spent its days singing and dancing. When winter arrived, the grasshopper was cold and hungry, while the ant had more than enough to eat. The grasshopper asked the ant for help, but the ant replied, \"I worked hard and prepared for this time, while you enjoyed yourself. Now, you must learn to be responsible for your choices.\" The story teaches us the value of hard work and preparation.");
        long newRowId3 = db.insertStory("The Rainbow Bridge:", "Unknown ", "Once, a young child asked a wise old woman, \"Where do our loved ones go when they pass away?\" The old woman replied, \"They cross the Rainbow Bridge, a magical place where they are happy and free of pain. When you miss them, look up at the sky after a storm, and you might see a beautiful rainbow – that's them sending their love.\" The child found comfort in this story and looked at rainbows with a sense of wonder and connection.");
        long newRowId4 = db.insertStory("The Painter's Vision", "Unknown ", "There was a famous painter who, as he grew older, began to lose his sight. Despite this, he continued to paint. People asked him how he could still create beautiful art without being able to see well. He replied, \"I may have lost my physical sight, but I have gained a new vision from within. I see the world not with my eyes, but with my heart and imagination.\" The story teaches us that creativity can transcend physical limitations.");

        Adapter = new StoryAdapterAdmin(this, getStoriesFromDatabase());
        rv_stories.setLayoutManager(new LinearLayoutManager(this));
        rv_stories.setAdapter(Adapter);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboardActivity.this, AddStory.class);
                startActivity(intent);
            }
        });
    }

    private List<Story> getStoriesFromDatabase() {
        List<Story> stories = new ArrayList<>();
        try (SQLiteDatabase database = db.getReadableDatabase()) {
            String[] projection = {
                    DatabaseHandler.COLUMN_ID,
                    DatabaseHandler.COLUMN_TITLE,
                    DatabaseHandler.COLUMN_AUTHOR,
                    DatabaseHandler.COLUMN_CONTENT
            };

            String sortOrder = DatabaseHandler.COLUMN_TITLE + " ASC";

            Cursor cursor = db.queryStories(
                    new String[]{DatabaseHandler.COLUMN_ID, DatabaseHandler.COLUMN_TITLE, DatabaseHandler.COLUMN_AUTHOR, DatabaseHandler.COLUMN_CONTENT},
                    null,
                    null,
                    sortOrder
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_TITLE));
                    String author = cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_AUTHOR));
                    String content = cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CONTENT));

                    Story story = new Story(id, title, author, content);
                    stories.add(story);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stories;
    }

}


