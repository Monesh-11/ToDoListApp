package com.example.todolistapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;



public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btnAdd;
    List<Task> taskList;
    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList, new TaskAdapter.TaskClickListener() {
            @Override
            public void onEdit(int position) {
                showTaskDialog(position);
            }

            @Override
            public void onDelete(int position) {
                taskList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> showTaskDialog(-1));
    }

    private void showTaskDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(position == -1 ? "Add Task" : "Edit Task");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        if (position != -1) {
            input.setText(taskList.get(position).getTitle());
        }
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) return;

            if (position == -1) {
                taskList.add(new Task(text));
                adapter.notifyItemInserted(taskList.size() - 1);
            } else {
                taskList.get(position).setTitle(text);
                adapter.notifyItemChanged(position);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
