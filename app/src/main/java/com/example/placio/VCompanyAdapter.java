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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class VCompanyAdapter extends FirestoreRecyclerAdapter<VCompany,VCompanyAdapter.VCompanyHolder> {
    private OnItemClickListener listener;
    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

    Float cgpa;
    String branch;
    Float m12th;
    Float m10th;
    int curarr;
    int clarr;
    String bat,Tiers;

    public VCompanyAdapter(@NonNull FirestoreRecyclerOptions<VCompany> options,String test1,String test2,String test3,String test4
            ,String test5,String test6,String test7,String tiers) {
        super(options);
        cgpa= Float.parseFloat(test1);
        branch=test6;
        curarr=Integer.parseInt(test5);
        clarr=Integer.parseInt(test4);
        bat=test7;
        m10th=Float.parseFloat(test2);
        m12th=Float.parseFloat(test3);
        Tiers=tiers;
    }

    @Override
    protected void onBindViewHolder(@NonNull VCompanyHolder holder, int position, @NonNull VCompany model) {
        Date todayDate = Calendar.getInstance().getTime();
        Date date1 = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        String todayString = formatter.format(todayDate);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr=model.getDate();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        String test=model.getDate()+" "+model.getTime();
        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date1=formatter6.parse(test);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(model.getCgpa() <= cgpa && model.getBranch().contains(branch) && model.getTenth() <= m10th &&
                model.getTwelfth() <= m12th && model.getCLBacklog() >= clarr && model.getBacklog() >= curarr &&
                model.getBatches().contains(bat) && !model.getAppliedStudents().contains(uid)
                && date1.compareTo(todayDate) > 0

        ){
            String[] myArray = Tiers.split(",");
            if(Tiers.length()<=7){
                if(Tiers.contains("Dream")){
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
                else if(Tiers.contains("Core")){
                    if(model.getTier().equals("Dream")){
                        String str = Arrays.toString(model.getRoles().toArray());
                        str = str.substring(1, str.length() - 1);
                        holder.Name.setText(model.getName());
                        holder.Roles.setText(str);
                        holder.Offer.setText(model.getOffer());
                        holder.LPA.setText(String.valueOf(model.getCtc()) + " LPA");
                        holder.Category.setText(model.getTier().toString().toUpperCase());
                        holder.LastDate.setText(outputDateStr);
                        holder.addedon.setText(model.getDateTime());
                        holder.time.setText(model.getTime());
                    }
                    else {
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }
                else {
                    if(model.getTier().equals("Dream") || model.getTier().equals("Core")){
                        String str = Arrays.toString(model.getRoles().toArray());
                        str = str.substring(1, str.length() - 1);
                        holder.Name.setText(model.getName());
                        holder.Roles.setText(str);
                        holder.Offer.setText(model.getOffer());
                        holder.LPA.setText(String.valueOf(model.getCtc()) + " LPA");
                        holder.Category.setText(model.getTier().toString().toUpperCase());
                        holder.LastDate.setText(outputDateStr);
                        holder.addedon.setText(model.getDateTime());
                        holder.time.setText(model.getTime());
                    }

                }

            }
            else {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }


            String str = Arrays.toString(model.getRoles().toArray());
            str = str.substring(1, str.length() - 1);
            holder.Name.setText(model.getName());
            holder.Roles.setText(str);
            holder.Offer.setText(model.getOffer());
            holder.LPA.setText(String.valueOf(model.getCtc()) + " LPA");
            holder.Category.setText(model.getTier().toString().toUpperCase());
            holder.LastDate.setText(outputDateStr);
            holder.addedon.setText(model.getDateTime());
            holder.time.setText(model.getTime());
        }
        else{
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
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
        TextView addedon;
        TextView time;
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
            addedon=itemView.findViewById(R.id.addedon);
            time=itemView.findViewById(R.id.time);

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
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}

