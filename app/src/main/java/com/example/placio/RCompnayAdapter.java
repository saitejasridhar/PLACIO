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

public class RCompnayAdapter extends FirestoreRecyclerAdapter<VCompany,RCompnayAdapter.RCompanyHolder> {
    private RCompnayAdapter.OnItemClickListener listener;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public RCompnayAdapter(@NonNull FirestoreRecyclerOptions<VCompany> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RCompnayAdapter.RCompanyHolder holder, int position, @NonNull VCompany model) {
        if (model.getRejected().contains(uid)) {
            String str = Arrays.toString(model.getRoles().toArray());
            str = str.substring(1, str.length() - 1);
            holder.Name.setText(model.getName());
            holder.Offer.setText(model.getOffer());
            holder.Category.setText(model.getTier().toString().toUpperCase());
        } else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

    }

    @NonNull
    @Override
    public RCompnayAdapter.RCompanyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.rejected_company_item,parent,false);
        return new RCompnayAdapter.RCompanyHolder(v);
    }



    class RCompanyHolder extends RecyclerView.ViewHolder {

        TextView Name;
        TextView Offer;
        TextView Category;
        ConstraintLayout outmost;

        public RCompanyHolder(@NonNull View itemView) {
            super(itemView);
            outmost = itemView.findViewById(R.id.outmost);
            Name = itemView.findViewById(R.id.companyName);
            Offer = itemView.findViewById(R.id.offering);
            Category = itemView.findViewById(R.id.category);

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

    public void setOnItemClickListener(RCompnayAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    }