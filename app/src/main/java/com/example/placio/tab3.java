package com.example.placio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class tab3 extends Fragment {
    String usid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference companyRef = db.collection("Companys");
    private RCompnayAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    tab2.OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (tab2.OnDataPass) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.tab3,container,false);
        Query query = companyRef.orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<VCompany> options = new FirestoreRecyclerOptions.Builder<VCompany>()
                .setQuery(query, VCompany.class)
                .build();
        adapter = new RCompnayAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.rejected);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new RCompnayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                VCompany note = documentSnapshot.toObject(VCompany.class);
                String id = documentSnapshot.getString("Name");
                String id1 = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                dataPasser.onDataPass(id1,"companyevent");
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

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getContext(), ActivityToOpen));
    }

    public interface OnDataPass {
        public void onDataPass(String data, String activity);
    }
}