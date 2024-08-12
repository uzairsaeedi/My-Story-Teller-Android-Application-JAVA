package com.mystoryteller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Locale;

public class StoryDetails extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private DatabaseHandler db;
    private Story story;
    private TextToSpeech tts;
    private Button btn_speak, btn_stop;
    ImageView btn_back;
    TextView tv_title,tv_author,tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);

        db = new DatabaseHandler(this);
        tts = new TextToSpeech(this, this);
        btn_speak = findViewById(R.id.btn_speak);
        btn_stop = findViewById(R.id.btn_stop);
        btn_back = findViewById(R.id.btn_back);

        int storyId = getIntent().getIntExtra("story_id", -1);

        if (storyId != -1) {
            story = db.getStoryById(storyId);

             tv_title = findViewById(R.id.tv_title);
             tv_author = findViewById(R.id.tv_author);
             tv_content = findViewById(R.id.tv_content);

            tv_author.setText(story.getTitle());
            tv_author.setText(story.getAuthor());
            tv_content.setText(story.getContent());
        }
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.stop();
            }
        });
        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToSpeak = story.getContent();

                if (tts != null) {
                    tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
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

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
