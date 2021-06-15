package com.example.placio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.DocumentSnapshot;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class tab1 extends Fragment {
    String usid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference companyRef = db.collection("Companys");
    private VCompanyAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

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
        String m10th =preferences.getString("10thMarks", "");
        String m12th =preferences.getString("PreUniMarks", "");
        String clarr =preferences.getString("ClearArr", "");
        String curarr =preferences.getString("CurArr", "");
        String bran =preferences.getString("Branch", "");
        String bat =preferences.getString("Batch", "");

        View view= inflater.inflate(R.layout.tab1,container,false);
        Query query = companyRef.orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<VCompany> options = new FirestoreRecyclerOptions.Builder<VCompany>()
                .setQuery(query, VCompany.class)
                .build();
        adapter = new VCompanyAdapter(options,cgpa,m10th,m12th,clarr,curarr,bran,bat);
        RecyclerView recyclerView = view.findViewById(R.id.visiting);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);



        adapter.setOnItemClickListener(new VCompanyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                String todayString = formatter.format(todayDate);

                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                String inputDateStr=documentSnapshot.getString("Date");
                Date date = null;
                try {
                    date = inputFormat.parse(inputDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputDateStr = outputFormat.format(date);

                if(!todayString.equals(outputDateStr)){
                    VCompany note = documentSnapshot.toObject(VCompany.class);
                    String id = documentSnapshot.getString("Name");
                    String id1 = documentSnapshot.getId();
                    String path = documentSnapshot.getReference().getPath();
                    dataPasser.onDataPass(id1);
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
        public void onDataPass(String data);
    }
}


