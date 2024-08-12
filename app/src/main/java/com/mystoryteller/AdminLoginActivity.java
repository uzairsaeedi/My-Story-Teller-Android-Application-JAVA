package com.mystoryteller;

import static com.mystoryteller.DatabaseHandler.COLUMN_PASSWORD;
import static com.mystoryteller.DatabaseHandler.COLUMN_USERNAME;
import static com.mystoryteller.DatabaseHandler.TABLE_ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText edt_username, edt_password;
    DatabaseHandler db = new DatabaseHandler(this);
    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edt_username.getText().toString();
                String password = edt_password.getText().toString();

                if (isAdmin(username, password)) {
                    Toast.makeText(AdminLoginActivity.this, "Successful Login", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Invalid admin credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean isAdmin(String username, String password) {
        SQLiteDatabase database = db.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ADMIN + " WHERE " +
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = database.rawQuery(query, new String[]{username, password});
        boolean isAdmin = cursor.moveToFirst();
        cursor.close();
        database.close();
        return isAdmin;
    }

}