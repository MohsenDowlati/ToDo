package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton addNewTask;
    private DataBaseManager db = null;
    private ArrayList<Tasks> tasks = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private RelativeLayout noTaskLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize UI element
        addNewTask = findViewById(R.id.btnAddEvent);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        noTaskLayout = findViewById(R.id.noTaskLayout);

        //get array
        db = new DataBaseManager(this);
        tasks = db.getAll();

        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setTasks(tasks);
        //no task label
        //TODO: needs to be debugged
        int tasksCount = adapter.getItemCount();
        if (tasksCount == 0) {
            noTaskLayout.setVisibility(View.VISIBLE);
        } else {
            noTaskLayout.setVisibility(View.GONE);
            //how many tasks have done:
            int completed = 0;
            for (Tasks t: tasks){
                if (t.isCompleteness())
                    completed++;
            }
            System.out.println(completed +" out of "+tasksCount);
        }

        //swap to remove
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swap);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        addNewTask.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.equals(addNewTask)) {
            Intent intent = new Intent(this,NewTaskActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about)
            popUp();
        return super.onOptionsItemSelected(item);
    }

    private void popUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thanks for your support");
        builder.setMessage("MOHSEN developed and designed this shit...enjoy :)"+"\n"+"dowmohsen@gmail.com");
        builder.setPositiveButton(":)",null);
        builder.create().show();
    }

    ItemTouchHelper.SimpleCallback swap = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            db = new DataBaseManager(MainActivity.this);
            int position = viewHolder.getAdapterPosition();
            db.deleteOne(tasks.get(position));
                tasks.remove(position);
                Toast.makeText(MainActivity.this, "Task Removed.", Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(position);

            }
    };
}