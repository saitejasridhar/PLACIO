package com.example.placio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class tab1 extends Fragment {
    String usid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference companyRef = db.collection("Companys");
    private VCompanyAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ConstraintLayout notice;
    TextView empty,message;

    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String cgpa =preferences.getString("CGPA", "");
        String m10th =preferences.getString("TenthMarks", "");
        String m12th =preferences.getString("PreUniMarks", "");
        String clarr =preferences.getString("ClearArr", "");
        String curarr =preferences.getString("CurArr", "");
        String bran =preferences.getString("Branch", "");
        String bat =preferences.getString("Batch", "");
        String tiers =preferences.getString("Tiers", "");
        View view= inflater.inflate(R.layout.tab1,container,false);
        LinearLayout layout = view.findViewById(R.id.outerLL);
        empty=view.findViewById(R.id.isempty);
        message=view.findViewById(R.id.message);
        layout.setVisibility(View.INVISIBLE);
        empty.setVisibility(View.INVISIBLE);
        notice = view.findViewById(R.id.notice);

        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docIdRef1= FirebaseFirestore.getInstance().collection("students").document(currentuser).collection("Details").document(currentuser);
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        if(document.get("Tiers").toString().contains("Dream")){
                            message.setText("Your placement procedure has been completed and you can't apply for anymore companies. Since you were placed for a dream company");
                            layout.setVisibility(View.INVISIBLE);
                            empty.setVisibility(View.INVISIBLE);
                            notice.setVisibility(View.VISIBLE);
                        }
                        else if(document.get("Tiers").toString().length()>7){
                            message.setText("Your placement procedure has been completed and you can't apply for anymore companies. Since you were placed for a two compies");
                            layout.setVisibility(View.INVISIBLE);
                            empty.setVisibility(View.INVISIBLE);
                            notice.setVisibility(View.VISIBLE);
                        }
                        else {
                            layout.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("please","help");
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });



        Query query = companyRef.orderBy("Date", Query.Direction.ASCENDING).orderBy("Time", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<VCompany> options = new FirestoreRecyclerOptions.Builder<VCompany>()
                .setQuery(query, VCompany.class)
                .build();
        adapter = new VCompanyAdapter(options,cgpa,m10th,m12th,clarr,curarr,bran,bat,tiers);
        RecyclerView recyclerView = view.findViewById(R.id.visiting);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new VCompanyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Date todayDate = Calendar.getInstance().getTime();
                Date date1 = null;
                String test=documentSnapshot.getString("Date")+" "+ documentSnapshot.getString("Time");
                SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    date1=formatter6.parse(test);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(date1.compareTo(todayDate) > 0){
                    VCompany note = documentSnapshot.toObject(VCompany.class);
                    String id = documentSnapshot.getString("Name");
                    String id1 = documentSnapshot.getId();
                    String path = documentSnapshot.getReference().getPath();
                    dataPasser.onDataPass(id1,"companydetails");
                }
                else {
                    Toast.makeText(getContext(),"Company last day to apply has expired",Toast.LENGTH_LONG).show();
                }


            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public interface OnDataPass {
        public void onDataPass(String data,String activity);
    }

}