package com.example.placio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class tab2 extends Fragment {
    String usid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference companyRef = db.collection("Companys");
    private ACompanyAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ConstraintLayout notice;
    TextView empty;
    tab2.OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (tab2.OnDataPass) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.tab2,container,false);
        LinearLayout layout = view.findViewById(R.id.outerLL);
        empty=view.findViewById(R.id.isempty);
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
                            layout.setVisibility(View.INVISIBLE);
                            empty.setVisibility(View.INVISIBLE);
                            notice.setVisibility(View.VISIBLE);
                        }
                        else if(document.get("Tiers").toString().length()>7){
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


        Query query = companyRef.orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<VCompany> options = new FirestoreRecyclerOptions.Builder<VCompany>()
                .setQuery(query, VCompany.class)
                .build();
        adapter = new ACompanyAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.applied);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new ACompanyAdapter.OnItemClickListener() {
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