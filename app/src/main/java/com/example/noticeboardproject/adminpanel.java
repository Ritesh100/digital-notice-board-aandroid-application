package com.example.noticeboardproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class adminpanel extends AppCompatActivity implements NoticeClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private Button FloatingActionButton;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private ArrayList<Notice> mNotices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpanel);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        FloatingActionButton = findViewById(R.id.floatingActionButton);
        FloatingActionButton.setOnClickListener(view -> {
            Intent i = new Intent(adminpanel.this, addNotices.class);
            startActivity(i);
        });


        mNotices = new ArrayList<>();
        mAdapter = new RecyclerAdapter(adminpanel.this, mNotices, adminpanel.this);
        mRecyclerView.setAdapter(mAdapter);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("notices_uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mNotices.clear();

                for (DataSnapshot noticeSnapshot : dataSnapshot.getChildren()) {
                    Notice upload = noticeSnapshot.getValue(Notice.class);
                    upload.setKey(noticeSnapshot.getKey());
                    mNotices.add(upload);
                }

                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(adminpanel.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public void onNoticeClick(Notice notice) {
        Intent intent = new Intent(this, DetailsNotice.class);
        intent.putExtra("NAME_KEY", notice.getTitle());
        intent.putExtra("DESCRIPTION_KEY", notice.getDescription());
        intent.putExtra("IMAGE_KEY", notice.getImageUrl());
        startActivity(intent);
    }

    @Override
    public void onNoticeLongClick(Notice notice) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this notice")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabaseRef.child(notice.getKey()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        mNotices.remove(notice);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(adminpanel.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }).show();
    }
}