package com.example.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editNotesActivity extends AppCompatActivity {
    Intent data;
    EditText medittitleofnote, meditcontentofnote;
    ImageView msaveeditnote;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        medittitleofnote = findViewById(R.id.edittitleofnote);
        meditcontentofnote = findViewById(R.id.editcontentofnote);
        msaveeditnote = findViewById(R.id.saveEditNote);
        data = getIntent();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Toolbar toolbar = findViewById(R.id.toolbarofEditNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        msaveeditnote.setOnClickListener(v -> {
            String newTitle = medittitleofnote.getText().toString().trim();
            String newContent = meditcontentofnote.getText().toString().trim();

            if (newTitle.isEmpty() || newContent.isEmpty()) {
                Toast.makeText(editNotesActivity.this, "Title and Content are required", Toast.LENGTH_SHORT).show();
                return;
            }

            DocumentReference documentReference = firebaseFirestore.collection("notes")
                    .document(firebaseUser.getUid())
                    .collection("myNotes")
                    .document(data.getStringExtra("noteId"));

            Map<String, Object> note = new HashMap<>();
            note.put("title", newTitle);
            note.put("content", newContent);

            documentReference.set(note)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(editNotesActivity.this, "Note updated successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(editNotesActivity.this,NotesActivity.class));
                    })
                    .addOnFailureListener(e -> Toast.makeText(editNotesActivity.this, "Failed to update note.", Toast.LENGTH_SHORT).show());
        });

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");
        medittitleofnote.setText(noteTitle);
        meditcontentofnote.setText(noteContent);
    }

    private void navigateToNotesActivity() {
        Intent intent = new Intent(editNotesActivity.this, NotesActivity.class);
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
