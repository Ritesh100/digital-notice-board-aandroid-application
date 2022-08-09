package com.example.noticeboardproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Userview extends AppCompatActivity implements NoticeClickListener  {
  private RecyclerView mRecyclerView;
  private RecyclerAdapter mAdapter;
    private FirebaseStorage mStorage;
  private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
  private ArrayList<Notice> mNotice;

    private void openDetailActivity(int position){
        Intent intent = new Intent(this, DetailsNotice.class);
        intent.putExtra("NAME_KEY", mNotice.get(position).getTitle());
        intent.putExtra("DESCRIPTION_KEY",mNotice.get(position).getDescription());
        intent.putExtra("IMAGE_KEY",mNotice.get(position).getImageUrl());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userview);


        mRecyclerView = findViewById(R.id.mRecyclerView1);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNotice = new ArrayList<>();
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(Userview.this);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("notices_uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                   Notice notice = postSnapshot.getValue(Notice.class);
                    notice.setKey(postSnapshot.getKey());
                   mNotice.add(notice);
                }
                mAdapter = new RecyclerAdapter(Userview.this, mNotice, Userview.this);

                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Userview.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNoticeClick(Notice notice) {
        Intent intent = new Intent(this, DetailsNotice.class);
        intent.putExtra("NAME_KEY",notice.getTitle());
        intent.putExtra("DESCRIPTION_KEY",notice.getDescription());
        intent.putExtra("IMAGE_KEY",notice.getImageUrl());
        startActivity(intent);
    }

    @Override
    public void onNoticeLongClick(Notice notice) {
    }
}