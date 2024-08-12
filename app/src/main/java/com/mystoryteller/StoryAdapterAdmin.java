package com.mystoryteller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StoryAdapterAdmin extends RecyclerView.Adapter<StoryAdapterAdmin.ViewHolder> {
    private List<Story> stories;

    private LayoutInflater inflater;
    DatabaseHandler db;
    Context context;

    public StoryAdapterAdmin(Context context, List<Story> stories) {
        this.inflater = LayoutInflater.from(context);
        this.stories = stories;
        this.context = context;
        this.db = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.story_layou_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.titleTextView.setText(story.getTitle());
        holder.authorTextView.setText(story.getAuthor());

        holder.btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoryDetailsAdmin.class);
                intent.putExtra("story_id", story.getId());
                context.startActivity(intent);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog(story.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }
    public void updateData(List<Story> updatedStories) {
        this.stories = updatedStories;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        Button btn_read;
        ImageView btn_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_title);
            authorTextView = itemView.findViewById(R.id.tv_author);
            btn_read = itemView.findViewById(R.id.btn_read);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }


    }
    private void showDeleteConfirmationDialog(int storyId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this story?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteStory(storyId);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void deleteStory(int storyId) {
        db.deleteStory(storyId);
        updateData(db.getAllStories());
        Toast.makeText(context, "Story deleted successfully", Toast.LENGTH_SHORT).show();
    }

}
