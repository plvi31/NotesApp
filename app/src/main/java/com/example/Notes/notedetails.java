package com.example.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class notedetails extends AppCompatActivity {
    private TextView mContentOfNoteDetail, mTitleOfNoteDetail;
    private ImageView mGoToEditNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        // Enable edge-to-edge display if needed
        // EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mTitleOfNoteDetail = findViewById(R.id.titleofnotedetail);
        mContentOfNoteDetail = findViewById(R.id.contentofnoteDetail);
        mGoToEditNote = findViewById(R.id.gotoeditnote);

        Toolbar toolbar = findViewById(R.id.toolbarofnoteDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();
        //if (data != null) {
            mContentOfNoteDetail.setText(data.getStringExtra("content"));
            mTitleOfNoteDetail.setText(data.getStringExtra("title"));

            mGoToEditNote.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), editNotesActivity.class);
                intent.putExtra("title", data.getStringExtra("title"));
                intent.putExtra("content", data.getStringExtra("content"));
                intent.putExtra("noteId", data.getStringExtra("noteId"));
                v.getContext().startActivity(intent);
            });
      //  }
    }
    private void navigateToNotesActivity() {
        Intent intent = new Intent(notedetails.this, NotesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        super.onBackPressed();
        navigateToNotesActivity();
    }
}
