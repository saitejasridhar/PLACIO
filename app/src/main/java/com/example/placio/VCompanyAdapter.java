package com.example.placio;

import android.app.Activity;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class VCompanyAdapter extends FirestoreRecyclerAdapter<VCompany,VCompanyAdapter.VCompanyHolder> {
    Float cgpa;
    String branch;
    Float m12th;
    Float m10th;
    int curarr;
    int clarr;
    int bat;

    public VCompanyAdapter(@NonNull FirestoreRecyclerOptions<VCompany> options) {
        super(options);
//        Log.d("test",test5);
//         cgpa= Float.parseFloat(test1);
//         branch=test6;
//        curarr=Integer.parseInt(test5);
//        clarr=Integer.parseInt(test4);
//        bat=Integer.parseInt(test7);
//        m10th=Float.parseFloat(test2);
//        m12th=Float.parseFloat(test3);

    }

    @Override
    protected void onBindViewHolder(@NonNull VCompanyHolder holder, int position, @NonNull VCompany model) {
//        Log.d("test",cgpa.toString());
//        if(model.getCgpa() <= cgpa && model.getBranch().contains(branch) && model.getTenth() <= m10th && model.getTwelfth() <= m12th
//        && model.getCLBacklog() <= clarr  && model.getBacklog() <= curarr && model.getBatches().contains(bat)){
            String str = Arrays.toString(model.getRoles().toArray());
            str = str.substring(1, str.length() - 1);
            holder.Name.setText(model.getName());
            holder.Roles.setText(str);
            holder.Offer.setText(model.getOffer());
            holder.LPA.setText(String.valueOf(model.getCtc()) + " LPA");
            holder.Category.setText(model.getTier().toString().toUpperCase());
            holder.LastDate.setText(String.valueOf(model.getDate()));
//        }
//        else{
//            holder.itemView.setVisibility(View.GONE);
//            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//        }

        }
    @NonNull
    @Override
    public VCompanyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.visiting_company_item,parent,false);
        return new VCompanyHolder(v);
    }


    class VCompanyHolder extends RecyclerView.ViewHolder{

        TextView Name;
        TextView LPA;
        TextView Roles;
        TextView Offer;
        TextView LastDate;
        TextView Category;
        ConstraintLayout outmost;

        public VCompanyHolder(@NonNull View itemView) {
            super(itemView);
            outmost=itemView.findViewById(R.id.outmost);
            Name= itemView.findViewById(R.id.companyName);
            LPA=itemView.findViewById(R.id.LPA);
            Roles= itemView.findViewById(R.id.jobRole);
            Offer= itemView.findViewById(R.id.offering);
            LastDate= itemView.findViewById(R.id.lastDate);
            Category= itemView.findViewById(R.id.category);
        }
    }


}
