package com.example.placio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

public class AnnouncementsAdapter extends FirestoreRecyclerAdapter<Announcement,AnnouncementsAdapter.AnnouncementsHolder> {
    private AnnouncementsAdapter.OnItemClickListener listener;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String bran;

    public AnnouncementsAdapter(@NonNull FirestoreRecyclerOptions<Announcement> options,String Branch) {
        super(options);
        bran=Branch;
    }

    @Override
    protected void onBindViewHolder(@NonNull AnnouncementsAdapter.AnnouncementsHolder holder, int position, @NonNull Announcement model) {

        if(model.getBranch().contains(bran)){
            holder.Name.setText(model.getName());
            holder.Desc.setText(model.getDescription());
            String str = model.getDateTime();
            String[] arrOfStr = str.split(" ",2);
            holder.Date.setText(arrOfStr[0]);
            holder.Time.setText(arrOfStr[1]);
        }
        else{
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @NonNull
    @Override
    public AnnouncementsAdapter.AnnouncementsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item,parent,false);
        return new AnnouncementsAdapter.AnnouncementsHolder(v);
    }



    class AnnouncementsHolder extends RecyclerView.ViewHolder {

        TextView Name;
        TextView Desc;
        TextView Date;
        TextView Time;

        public AnnouncementsHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
            Desc = itemView.findViewById(R.id.desc);
            Date =itemView.findViewById(R.id.date);
            Time= itemView.findViewById(R.id.time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(AnnouncementsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}