package com.example.joinn.chatfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.joinn.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Message> messageList;
    private static final int RECEIVE_TYPE = 1; //받는 타입
    private static final int SEND_TYPE = 2; //보내는 타입

    // Interface for handling accept button clicks
    public interface OnAcceptButtonClickListener {
        void onAcceptButtonClick();
    }

    private OnAcceptButtonClickListener acceptButtonClickListener;

    public MessageAdapter(Context context, ArrayList<Message> messageList, OnAcceptButtonClickListener listener) {
        this.context = context;
        this.messageList = messageList;
        this.acceptButtonClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RECEIVE_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.receive, parent, false);
            return new ReceiveViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.send, parent, false);
            return new SendViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message currentMessage = messageList.get(position);

        if (holder.getItemViewType() == SEND_TYPE) {
            SendViewHolder viewHolder = (SendViewHolder) holder;
            viewHolder.timeSend.setText(currentMessage.getTime());
            viewHolder.sendMessage.setText(currentMessage.getMessage());
            viewHolder.sendName.setText(currentMessage.getName());

            // Set accept button visibility
            if (currentMessage.isAcceptButtonVisible()) {
                viewHolder.acceptButton.setVisibility(View.VISIBLE);
            } else {
                viewHolder.acceptButton.setVisibility(View.GONE);
            }

            // Set reject button visibility
            if (currentMessage.isRejectButtonVisible()) {
                viewHolder.rejectButton.setVisibility(View.VISIBLE);
            } else {
                viewHolder.rejectButton.setVisibility(View.GONE);
            }

            // Set accept button click listener
            viewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (acceptButtonClickListener != null) {
                        acceptButtonClickListener.onAcceptButtonClick();
                    }
                }
            });
        } else {
            ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            viewHolder.timeReceive.setText(currentMessage.getTime());
            viewHolder.receiveMessage.setText(currentMessage.getMessage());
            viewHolder.receiveName.setText(currentMessage.getName());

            // Set accept button visibility
            if (currentMessage.isAcceptButtonVisible()) {
                viewHolder.acceptButton.setVisibility(View.VISIBLE);
            } else {
                viewHolder.acceptButton.setVisibility(View.GONE);
            }

            // Set reject button visibility
            if (currentMessage.isRejectButtonVisible()) {
                viewHolder.rejectButton.setVisibility(View.VISIBLE);
            } else {
                viewHolder.rejectButton.setVisibility(View.GONE);
            }

            // Set accept button click listener
            viewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (acceptButtonClickListener != null) {
                        acceptButtonClickListener.onAcceptButtonClick();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message currentMessage = messageList.get(position);

        if (FirebaseAuth.getInstance().getCurrentUser() != null &&
                FirebaseAuth.getInstance().getCurrentUser().getUid().equals(currentMessage.getSendId())) {
            return SEND_TYPE;
        } else {
            return RECEIVE_TYPE;
        }
    }

    //보낸 쪽 ViewHolder
    public class SendViewHolder extends RecyclerView.ViewHolder {
        TextView sendMessage;
        TextView timeSend;
        TextView sendName;
        Button acceptButton;
        Button rejectButton;

        public SendViewHolder(View itemView) {
            super(itemView);
            sendMessage = itemView.findViewById(R.id.send_message_text);
            timeSend = itemView.findViewById(R.id.timeSender);
            sendName = itemView.findViewById(R.id.senderName);
            acceptButton = itemView.findViewById(R.id.accept_button_send);
            rejectButton = itemView.findViewById(R.id.reject_button_send);
        }
    }

    //받는 쪽 ViewHolder
    public class ReceiveViewHolder extends RecyclerView.ViewHolder {
        TextView receiveMessage;
        TextView timeReceive;
        TextView receiveName;
        Button acceptButton;
        Button rejectButton;

        public ReceiveViewHolder(View itemView) {
            super(itemView);
            receiveMessage = itemView.findViewById(R.id.receive_message_text);
            timeReceive = itemView.findViewById(R.id.timeReceiver);
            receiveName = itemView.findViewById(R.id.receiverName);
            acceptButton = itemView.findViewById(R.id.accept_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }
    }
}
