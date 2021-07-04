package com.example.placio;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
        import com.firebase.ui.firestore.FirestoreRecyclerOptions;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.DocumentSnapshot;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.List;

public class PTicketsAdapter extends FirestoreRecyclerAdapter<Ticket,PTicketsAdapter.TicketsHolder> {
    private PTicketsAdapter.OnItemClickListener listener;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public PTicketsAdapter(@NonNull FirestoreRecyclerOptions<Ticket> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PTicketsAdapter.TicketsHolder holder, int position, @NonNull Ticket model) {

        if(uid.equals(model.getUID()) && model.getStatus().equals("Pending")){
            Log.d("fuc",model.getUID());
            holder.Sub.setText(model.getSubject());
            holder.Raised.setText("Rasied On: "+model.getDate());
            holder.Desc.setText(model.getDescription());
        }
        else{
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @NonNull
    @Override
    public PTicketsAdapter.TicketsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_ticket,parent,false);
        return new PTicketsAdapter.TicketsHolder(v);
    }

    class TicketsHolder extends RecyclerView.ViewHolder {

        TextView Sub;
        TextView Desc;
        TextView Raised;

        public TicketsHolder(@NonNull View itemView) {
            super(itemView);
            Sub = itemView.findViewById(R.id.Subject);
            Desc = itemView.findViewById(R.id.desc);
            Raised =itemView.findViewById(R.id.raised);

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

    public void setOnItemClickListener(PTicketsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}