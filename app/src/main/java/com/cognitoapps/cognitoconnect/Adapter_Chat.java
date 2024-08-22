package com.cognitoapps.cognitoconnect;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cognitoapps.cognitoconnect.Models.Model_Chat;
import com.cognitoapps.cognitoconnect.Models.Model_Current_User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Adapter_Chat extends FirebaseRecyclerAdapter<Model_Chat, Adapter_Chat.Chat_viewHolder>  {
    /////////////
    Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
 public Adapter_Chat(@NonNull FirebaseRecyclerOptions<Model_Chat> options, Context context) {
        super(options);
        this.context = context;
 }

    @Override
    protected void onBindViewHolder(@NonNull Chat_viewHolder holder, int position, @NonNull Model_Chat model) {




        holder.status.setText(model.getStatus());
        holder.identity.setText(model.getIdentity());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]
                        {
                                "Remove This",
                                "Dismiss"
                        };
                //alert to select two options
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Do you want to remove this");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {


                        //    FirebaseDatabase.getInstance().getReference().child("Chat_log").child(Model_Current_User.usrStore.getPhone()).child(model.getIdentity()).removeValue();

                          //  FirebaseDatabase.getInstance().getReference().child("Cart_List").child("User_View").child(Current_User_Store.usrStore.getPhone()).child(model.getUid()).removeValue();

                         //   Toast.makeText(context, "Chat history cleared ", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                builder.show();
            }
        });


    }

    @NonNull
    @Override
    public Chat_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_single_chat_item, parent, false);
        return new Chat_viewHolder(view);
    }

    class Chat_viewHolder extends RecyclerView.ViewHolder {
        TextView status,identity;


        public Chat_viewHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.lbl_online_status);
            identity = itemView.findViewById(R.id.lbl_identity);


        }
    }



//////////////////
}
