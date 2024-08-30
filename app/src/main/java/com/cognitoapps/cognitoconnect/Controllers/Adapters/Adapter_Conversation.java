package com.cognitoapps.cognitoconnect.Controllers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cognitoapps.cognitoconnect.Models.Model_Conversation;
import com.cognitoapps.cognitoconnect.Models.Model_Current_User;
import com.cognitoapps.cognitoconnect.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Adapter_Conversation extends FirebaseRecyclerAdapter<Model_Conversation, Adapter_Conversation.ConversationViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private Context context;

    public Adapter_Conversation(@NonNull FirebaseRecyclerOptions<Model_Conversation> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Model_Conversation conversation = getItem(position);

        // Assuming you have a method or field to determine if the message is sent by the current user
        if (conversation.getSender().equals(Model_Current_User.usrStore.getPhone())) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_single_sent_message, parent, false);
            return new ConversationViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_single_recieved_message, parent, false);
            return new ConversationViewHolder(view);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ConversationViewHolder holder, int position, @NonNull Model_Conversation model) {
        holder.messageBody.setText(model.getMessage());
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView messageBody;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.text_message_body);
        }
    }
}
