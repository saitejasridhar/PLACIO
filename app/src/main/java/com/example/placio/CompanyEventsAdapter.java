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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class CompanyEventsAdapter extends FirestoreRecyclerAdapter<Event,CompanyEventsAdapter.CompanyEventsHolder> {
    private CompanyEventsAdapter.OnItemClickListener listener;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Date date6;
    Date todayDate = Calendar.getInstance().getTime();

    public CompanyEventsAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CompanyEventsAdapter.CompanyEventsHolder holder, int position, @NonNull Event model) {
        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sDate6 = model.getDate()+" "+ model.getTime();
        try {
            date6=formatter6.parse(sDate6);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date6.compareTo(todayDate) > 0) {
            holder.Name.setText(model.getType());
            holder.Date.setText(model.getDate());
            holder.Desc.setText(model.getDescription());
            holder.Company.setText(model.getCompanyname());
            holder.Time.setText(model.getTime());
        }
        else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @NonNull
    @Override
    public CompanyEventsAdapter.CompanyEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.events_item,parent,false);
        return new CompanyEventsAdapter.CompanyEventsHolder(v);
    }


    class CompanyEventsHolder extends RecyclerView.ViewHolder {

        TextView Name;
        TextView Desc;
        TextView Date;
        TextView Company;
        TextView Time;

        public CompanyEventsHolder(@NonNull View itemView) {
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

    public void setOnItemClickListener(CompanyEventsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}