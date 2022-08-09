package com.example.noticeboardproject;
import android.content.Context;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public  class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    private Context mContext;
    private ArrayList<Notice> mNotices;
    private NoticeClickListener mListener;

    public RecyclerAdapter(Context mContext, ArrayList<Notice> mNotices, NoticeClickListener listener) {
        this.mContext = mContext;
        this.mNotices = mNotices;
        this.mListener = listener;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_model, parent, false);
        return new RecyclerViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Notice currentTeacher = mNotices.get(position);
        holder.bind(currentTeacher);
    }

    @Override
    public int getItemCount() {
        return mNotices.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView,descriptionTextView,dateTextView;
        public ImageView teacherImageView;
        public NoticeClickListener listener;

        public RecyclerViewHolder(@NonNull View itemView, NoticeClickListener listener) {
            super(itemView);
            nameTextView =itemView.findViewById ( R.id.titleTextView );
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            teacherImageView = itemView.findViewById(R.id.teacherImageView);

            this.listener = listener;
        }

        public void bind(Notice notice) {
            nameTextView.setText(notice.getTitle());
            descriptionTextView.setText(notice.getDescription());
            dateTextView.setText(getDateToday());
            Glide.with(mContext)
                    .load(notice.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(teacherImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNoticeClick(notice);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onNoticeLongClick(notice);
                    return false;
                }
            });
        }

//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.setHeaderTitle("Select Action");
//            MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
//            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");
//
//            showItem.setOnMenuItemClickListener(this);
//            deleteItem.setOnMenuItemClickListener(this);
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            if (mListener != null) {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//
//                    switch (item.getItemId()) {
//                        case 1:
//                            mListener.onShowItemClick(position);
//                            return true;
//                        case 2:
//                            mListener.onDeleteItemClick(position);
//                            return true;
//                    }
//                }
//            }
//            return false;
//        }
    }

    private String getDateToday(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String today= dateFormat.format(date);
        return today;
    }
}