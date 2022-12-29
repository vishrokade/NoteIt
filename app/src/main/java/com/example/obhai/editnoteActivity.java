package com.example.obhai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteActivity extends AppCompatActivity {


    private FloatingActionButton saveEditNote;
    private EditText noteEditTitle, noteEditContent;
    private Toolbar toolbarEdit;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        saveEditNote = findViewById(R.id.saveEditNote);
        noteEditTitle = findViewById(R.id.edittitlenote);
        noteEditContent = findViewById(R.id.editcontentnote);

        toolbarEdit = findViewById(R.id.toolBarForEditNote);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();

        //firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        noteEditTitle.setText(data.getStringExtra("Title"));
        noteEditContent.setText(data.getStringExtra("Content"));

        saveEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = noteEditTitle.getText().toString();
                String newContent = noteEditContent.getText().toString();
                
                if (newTitle.isEmpty()){
                    noteEditTitle.setError("Required");
                    noteEditTitle.requestFocus();
                }
                else if (newContent.isEmpty()){
                    Toast.makeText(editnoteActivity.this, "Enter Content", Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference documentReference = firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").document(data.getStringExtra("noteId"));
                    Map<String,Object> note = new HashMap<>();
                    note.put("Title",newTitle);
                    note.put("Content",newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(editnoteActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(editnoteActivity.this,first.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editnoteActivity.this,"Failed to Update",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}