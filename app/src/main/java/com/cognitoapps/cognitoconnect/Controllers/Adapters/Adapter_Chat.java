package com.cognitoapps.cognitoconnect.Controllers.Adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cognitoapps.cognitoconnect.Controllers.Controller_Chat;
import com.cognitoapps.cognitoconnect.Models.Model_Chat;
import com.cognitoapps.cognitoconnect.Models.Model_Current_User;
import com.cognitoapps.cognitoconnect.R;
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




        holder.created.setText("Created on : "+model.getCreated());
        holder.identity.setText(model.getIdentity());
        holder.img.setImageResource(R.drawable.user_icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]
                        {
                                "Enter Chat",
                                "Remove This",
                                "Dismiss"
                        };
                //alert to select two options
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select an option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0)
                        {
                            ///////////////
                            Intent intent = new Intent(context, Controller_Chat.class);
                            intent.putExtra("chat_recipient", model.getIdentity());
                            intent.putExtra("chat_id", model.getChat_id());


                            //redirecting
                            context.startActivity(intent);


                            ////////////////

                        }
                       else if (i == 1) {


                           //remove value at removers
                            FirebaseDatabase.getInstance().getReference().child("Chat_log").child(Model_Current_User.usrStore.getPhone()).child(model.getIdentity()).removeValue();


                            //remove value at other party

                            FirebaseDatabase.getInstance().getReference().child("Chat_log").child(model.getIdentity()).child(Model_Current_User.usrStore.getPhone()).removeValue();


                            //remove values from chats

                            FirebaseDatabase.getInstance().getReference().child("Chats").child(model.getChat_id()).removeValue();

                            //remove values from chat data

                            FirebaseDatabase.getInstance().getReference().child("Chat_data").child(Model_Current_User.usrStore.getPhone()).child(model.getIdentity()).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("Chat_data").child(model.getIdentity()).child(Model_Current_User.usrStore.getPhone()).removeValue();



                            Toast.makeText(context, "Chat history cleared ", Toast.LENGTH_SHORT).show();

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
        TextView created,identity;
        ImageView img;


        public Chat_viewHolder(@NonNull View itemView) {
            super(itemView);

            created = itemView.findViewById(R.id.lbl_chat_created);
            identity = itemView.findViewById(R.id.lbl_identity);
            img = itemView.findViewById(R.id.img_usr);


        }
    }



//////////////////
}
