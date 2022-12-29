package com.example.obhai;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class first extends AppCompatActivity {

    private ImageView addNote;
    private FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirestoreRecyclerAdapter<NoteData,NoteviewHolder> noteAdopter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("My Notes");
        setContentView(R.layout.activity_first);

        addNote = findViewById(R.id.imageaddnote);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(first.this,createNote.class);
                startActivity(intent);
            }
        });

        Query query = firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes");

        FirestoreRecyclerOptions<NoteData> userAllNotes = new FirestoreRecyclerOptions.Builder<NoteData>().setQuery(query,NoteData.class).build();

        noteAdopter = new FirestoreRecyclerAdapter<NoteData, NoteviewHolder>(userAllNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteviewHolder holder, int position, @NonNull NoteData model) {

                int colorcode = getRandomcolor();
                holder.note.setBackgroundColor(holder.itemView.getResources().getColor(colorcode,null));
                holder.noteTitle.setText(model.getTitle());
                holder.noteContent.setText(model.getContent());

                String documentId = noteAdopter.getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(),editnoteActivity.class);

                        intent.putExtra("Title",model.getTitle());
                        intent.putExtra("Content",model.getContent());
                        intent.putExtra("noteId",documentId);

                        view.getContext().startActivity(intent);
                    }
                });

                ImageView popup = holder.itemView.findViewById(R.id.popmenuutton);
                popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                        popupMenu.setGravity(Gravity.CENTER);

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                DocumentReference documentreference = firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").document(documentId);
                                documentreference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(first.this, "Note is deleted", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(first.this, "Failed Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });

                        popupMenu.show();
                    }
                });

            }

            @NonNull
            @Override
            public NoteviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new NoteviewHolder(view);
            }
        };

        recyclerView = findViewById(R.id.noteRecycler);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(noteAdopter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(first.this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    public class  NoteviewHolder extends RecyclerView.ViewHolder{

        private TextView noteTitle;
        private TextView noteContent;
        LinearLayout note;
        public NoteviewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            note =itemView.findViewById(R.id.note);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdopter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (noteAdopter != null){
            noteAdopter.startListening();
        }
    }
    private int getRandomcolor(){
        List<Integer> colorCode = new ArrayList<>();

        colorCode.add(R.color.grey);
        colorCode.add(R.color.grey1);
        colorCode.add(R.color.green);
        colorCode.add(R.color.red);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.purple);
        colorCode.add(R.color.blue);
        colorCode.add(R.color.skyBlue);
        colorCode.add(R.color.orange);

        Random random = new Random();
        int color = random.nextInt(colorCode.size());

        return colorCode.get(color);
    }
}