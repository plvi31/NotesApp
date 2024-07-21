package com.example.Notes;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.firebase.ui.firestore.ObservableSnapshotArray;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NotesActivity extends AppCompatActivity {
    FloatingActionButton mcreateNotesFab;
    private FirebaseAuth firebaseAuth;
    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mcreateNotesFab = findViewById(R.id.createNotefab);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbarofnotes);
        setSupportActionBar(toolbar);
        mcreateNotesFab.setOnClickListener(v -> startActivity(new Intent(NotesActivity.this, createNotes.class)));

        Query query = firebaseFirestore.collection("notes")
                .document(firebaseUser.getUid())
                .collection("myNotes")
                .orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query, firebasemodel.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {
               // noteViewHolder.bind(firebasemodel);
                ImageView popupBtn=noteViewHolder.itemView.findViewById(R.id.menupopupbutton);

                int colorCode = getRandomColor();

                noteViewHolder.noteTitle.setText(firebasemodel.getTitle());
                noteViewHolder.noteContent.setText(firebasemodel.getContent());


               // noteViewHolder.mNote.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), colorCode));
                noteViewHolder.mNote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colorCode,null));



                String docId = getSnapshots().getSnapshot(i).getId();

                noteViewHolder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), notedetails.class);
                    intent.putExtra("title", firebasemodel.getTitle());
                    intent.putExtra("content", firebasemodel.getContent());
                    intent.putExtra("noteId", docId);
                    v.getContext().startActivity(intent);

                });

                popupBtn.setOnClickListener(v -> {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v, Gravity.END);
                    popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(item -> {
                        Intent intent = new Intent(v.getContext(), editNotesActivity.class);
                        intent.putExtra("title", firebasemodel.getTitle());
                        intent.putExtra("content", firebasemodel.getContent());
                        intent.putExtra("noteId", docId);
                        v.getContext().startActivity(intent);
                        return true;
                    });

                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(item -> {
                        DocumentReference documentReference = firebaseFirestore.collection("notes")
                                .document(firebaseUser.getUid())
                                .collection("myNotes")
                                .document(docId);
                        documentReference.delete()
                                .addOnSuccessListener(aVoid -> Toast.makeText(NotesActivity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(NotesActivity.this, "Failed to delete note", Toast.LENGTH_SHORT).show());
                                return true;
                    });

                    popupMenu.show();
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NoteViewHolder(view);
            }
        };

        mrecyclerview = findViewById(R.id.recyclerView);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview.setAdapter(noteAdapter);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView noteTitle;
        private TextView noteContent;
        LinearLayout mNote;
        ImageView popupBtn;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.notetitle);
            noteContent = itemView.findViewById(R.id.notecontent);
            mNote = itemView.findViewById(R.id.note);
            popupBtn = itemView.findViewById(R.id.menupopupbutton);
        }

//        public void bind(firebasemodel firebasemodel) {
//            noteTitle.setText(firebasemodel.getTitle());
//            noteContent.setText(firebasemodel.getContent());
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(NotesActivity.this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (noteAdapter != null) {
            noteAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }

    private int getRandomColor() {
        List<Integer> colorCodeList = Arrays.asList(
                R.color.grey, R.color.pink, R.color.lightgreen,
                R.color.green, R.color.skyblue, R.color.color1,
                R.color.color2, R.color.color3, R.color.color4,
                R.color.color5);

        Random random = new Random();
        int number = random.nextInt(colorCodeList.size());
        return colorCodeList.get(number);
    }
}
