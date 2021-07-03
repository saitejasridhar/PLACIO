package com.example.placio;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EventsAdapter extends FirestoreRecyclerAdapter<Event,EventsAdapter.EventsHolder> {
    private EventsAdapter.OnItemClickListener listener;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<String> myList;

    public EventsAdapter(@NonNull FirestoreRecyclerOptions<Event> options,String Applied) {
        super(options);
        String replace = Applied.replace("[","");
        String replace1 = replace.replace("]","");
      myList = new ArrayList<String>(Arrays.asList(replace1.split(", ")));
    }

    @Override
    protected void onBindViewHolder(@NonNull EventsAdapter.EventsHolder holder, int position, @NonNull Event model) {

        if(myList.contains(model.getCompanyid())){
            holder.Name.setText(model.getType());
            holder.Date.setText("On "+model.getDate());
            holder.Desc.setText(model.getDescription());
            holder.Company.setText(model.getCompanyname());
            holder.Time.setText("At "+model.getTime());
        }
        else{
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @NonNull
    @Override
    public EventsAdapter.EventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.events_item,parent,false);
        return new EventsAdapter.EventsHolder(v);
    }

    class EventsHolder extends RecyclerView.ViewHolder {

        TextView Name;
        TextView Desc;
        TextView Date;
        TextView Company;
        TextView Time;

        public EventsHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
            Desc = itemView.findViewById(R.id.desc);
            Date =itemView.findViewById(R.id.date);
            Company=itemView.findViewById(R.id.company);
            Time=itemView.findViewById(R.id.time);

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

    public void setOnItemClickListener(EventsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}