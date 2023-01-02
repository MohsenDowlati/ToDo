package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addTask;
    private EditText title , desc;
    private TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        addTask = findViewById(R.id.addTaskBtn);
        addTask.setOnClickListener(this);
        title = findViewById(R.id.titleEditTxt);
        desc = findViewById(R.id.descEditText);
        warning = findViewById(R.id.warning);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(addTask)) {
            String titleStr = title.getText().toString(), descStr = desc.getText().toString();
            if (descStr.equals(""))
                descStr = " ";
            if (titleStr.equals("")) {
                warning.setVisibility(View.VISIBLE);
            } else {
                warning.setVisibility(View.GONE);
                DataBaseManager db = new DataBaseManager(this);
                Tasks task = new Tasks(-1, titleStr, descStr, false);

                boolean success = db.addOne(task);

                if (success) {
                    Toast.makeText(this, "Added Successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Something Wrong Happened, Try Again.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}