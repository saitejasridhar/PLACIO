package com.example.placio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class tab1 extends Fragment {
    String usid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference companyRef = db.collection("Companys");
    private VCompanyAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//        String cgpa =preferences.getString("CGPA", "");
//        String m10th =preferences.getString("10thMarks", "");
//        String m12th =preferences.getString("PreUniMarks", "");
//        String clarr =preferences.getString("ClearArr", "");
//        String curarr =preferences.getString("CurArr", "");
//        String bran =preferences.getString("Branch", "");
//        String bat =preferences.getString("Batch", "");

//        Log.d("testsd",cgpa);

        View view= inflater.inflate(R.layout.tab1,container,false);
        Query query = companyRef.orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<VCompany> options = new FirestoreRecyclerOptions.Builder<VCompany>()
                .setQuery(query, VCompany.class)
                .build();
        adapter = new VCompanyAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.visiting);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
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
}
