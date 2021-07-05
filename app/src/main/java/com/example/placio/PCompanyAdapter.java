package com.example.placio;

import android.util.Log;
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
import java.util.Objects;

public class PCompanyAdapter extends FirestoreRecyclerAdapter<VCompany,PCompanyAdapter.PCompanyHolder> {
    private OnItemClickListener listener;
    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


    public PCompanyAdapter(@NonNull FirestoreRecyclerOptions<VCompany> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PCompanyHolder holder, int position, @NonNull VCompany model) {
        if ( model.getPlaced().contains(uid)) {
            String str = Arrays.toString(model.getRoles().toArray());
            str = str.substring(1, str.length() - 1);
            holder.Name.setText(model.getName());
            holder.Offer.setText(model.getOffer());
            holder.Category.setText(model.getTier().toUpperCase());
            holder.positions.setText(model.getRoles().toString().substring(1, model.getRoles().toString().length() - 1));
            holder.location.setText(model.getLocation());
            holder.breakdown.setText(model.getBreakdown());
            holder.Ctc.setText(model.getCtc().toString());
        } else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @NonNull
    @Override
    public PCompanyAdapter.PCompanyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.placed_company_item,parent,false);
        return new PCompanyAdapter.PCompanyHolder(v);
    }


    class PCompanyHolder extends RecyclerView.ViewHolder {

        TextView Name,Ctc,breakdown,location,positions;
        TextView Offer;
        TextView Category;
        ConstraintLayout outmost;


        public PCompanyHolder(@NonNull View itemView) {
            super(itemView);
            outmost = itemView.findViewById(R.id.outmost);
            Name = itemView.findViewById(R.id.companyName);
            Offer = itemView.findViewById(R.id.offering);
            Category = itemView.findViewById(R.id.category);
            Ctc=itemView.findViewById(R.id.ctc);
            breakdown=itemView.findViewById(R.id.breakdown);
            location=itemView.findViewById(R.id.location);
            positions=itemView.findViewById(R.id.positions);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(PCompanyAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}