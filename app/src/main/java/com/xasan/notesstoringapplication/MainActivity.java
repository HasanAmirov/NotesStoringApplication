package com.xasan.notesstoringapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView notesListView;
    TextView emptyTv;


    static List<String> notes;
    static ArrayAdapter adapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = this.getSharedPreferences( name: "com.xasan.notesstoringapplication", Context.MODE_PRIVATE);

        notesListView = findViewById(R.id.notes_ListView);
        emptyTv = findViewById(R.id.emptyTV);

        notes = new ArrayList<>();


        HashSet<String> noteSet = sharedPreferences.getStringSet( key: "notes" , defValues null);

        if(noteSet.isEmpty() || noteSet == null){
            emptyTv.setVisibility(View.VISIBLE);
        }else{
            emptyTv.setVisibility(View.GONE);
            notes = new ArrayList<>(noteSet);
        }

        adapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_notes_row, R.id.notesTV, notes);
        notesListView.setAdapter(adapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra( "noteID", position);
                startActivity(intent);
            }
        });

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int itemToDelete = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure?!")
                        .setMessage("Do you want to delete this note?!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()){
                    @Override
                            public void onClick(DialogInterface , int which){
                                notes.remove(itemToDelete);
                                adapter.notifyDataSetChanged();

                                HashSet<String> noteSet = new HashSet<>(notes);
                                sharedPreferences.edit().putStringSet("notes", noteSet).apply();

                                if(noteSet.isEmpty() || noteSet == null){
                                    emptyTv.setVisibility(View.VISIBLE);
                    }
                }}.setNegativeButton( "No", null)
                        .show();


                return true;
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        MenuInflater
        super.onCreate(savedInstanceState, persistentState);
    }
}