package com.example.Notes;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class createNotes extends AppCompatActivity {

    private static final String TAG = "CreateNotes";

    private EditText mcreatetitleofnote, mcreatecontentofnote;
    private FirebaseAuth firebaseAuth;
    private ImageView msaveNote;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private ProgressBar mprogressbarofcreatenote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        msaveNote = findViewById(R.id.saveNote);
        mcreatetitleofnote = findViewById(R.id.createtitleofnote);
        mcreatecontentofnote = findViewById(R.id.createcontentofnote);
        mprogressbarofcreatenote = findViewById(R.id.progressBarofcreateNote);

        Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            // User is not logged in, handle this case (e.g., show a login prompt)
            return;
        }

        msaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mcreatetitleofnote.getText().toString().trim();
                String content = mcreatecontentofnote.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(createNotes.this, "Title is required", Toast.LENGTH_SHORT).show();
                    msaveNote.setEnabled(true);
                    return;
                }

                else if (TextUtils.isEmpty(content)) {
                    Toast.makeText(createNotes.this, "Content is required", Toast.LENGTH_SHORT).show();
                    msaveNote.setEnabled(true);
                    return;
                }else {
                    // ... (Inside your note creation function)

// Generate a unique note ID
                    String noteId = UUID.randomUUID().toString();

// Create a document reference
                    DocumentReference documentReference = firebaseFirestore.collection("notes")
                            .document(firebaseUser.getUid()).collection("myNotes").document(noteId);

// Create a map of note data
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content",content);

// Write the note to Firestore
                    documentReference.set(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // Note created successfully
                                    Toast.makeText(createNotes.this, "Note created successfully.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(createNotes.this, NotesActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to create note
                                    Log.e(TAG, "Failed to create note: ", e); // Print the error to the console
                                    Toast.makeText(createNotes.this, "Failed to create note.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(createNotes.this, NotesActivity.class));
                                }
                            });




                }
            }
        });



      //  mprogressbarofcreatenote.setVisibility(View.VISIBLE); // Show progress bar


//        rules_version = '2';
//
//        service cloud.firestore {
//            match /databases/{database}/documents {
//                match /users/{userId} {
//                    allow read, write: if request.auth != null && request.auth.uid == userId;
//                }
//            }
//        }




    }

    private void navigateToNotesActivity() {
        Intent intent = new Intent(createNotes.this, NotesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToNotesActivity();
    }
}
