package com.example.obhai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class createNote extends AppCompatActivity {

    private FloatingActionButton saveNote;
    private EditText noteTitle, noteContent;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    private Toolbar toolbar;

    NoteData noteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_note);

        saveNote = findViewById(R.id.saveNote);
        noteTitle = findViewById(R.id.createtitlenote);
        noteContent = findViewById(R.id.createcontentnote);

        toolbar = findViewById(R.id.toolBarForCreateNote);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = noteTitle.getText().toString();
                String Content = noteContent.getText().toString();

                if (Title.isEmpty()){
                    noteTitle.setError("Required");
                    noteTitle.requestFocus();
                }

                else if (Content.isEmpty()){
                    Toast.makeText(createNote.this, "Enter text for content", Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference documentreference = firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").document();

                    Map<String,Object> note = new HashMap<>();

                    note.put("Title",Title);
                    note.put("Content",Content);

                    documentreference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(createNote.this, "note created successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(createNote.this,first.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createNote.this, "Failed to create note", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}