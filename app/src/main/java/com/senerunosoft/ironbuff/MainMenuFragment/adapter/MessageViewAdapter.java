package com.senerunosoft.ironbuff.MainMenuFragment.adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.MessageTable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MessageViewAdapter extends RecyclerView.Adapter {
    private List<MessageTable> messageList;
    LayoutInflater inflater;
    private final int SenderType = 1,receivedType=2;


    public MessageViewAdapter(Context context, List<MessageTable> messageList) {
        this.inflater = LayoutInflater.from(context);
        Collections.sort(messageList);
        this.messageList = messageList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View view;
        if (viewType == SenderType){
            view = inflater.inflate(R.layout.message_send,parent,false);
            return new SendMessageHolder(view);
        }
        else {
            view = inflater.inflate(R.layout.message_get,parent,false);
            return new ReceivedMessageHolder(view);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        MessageTable message = messageList.get(position);
        if (message.getViewType()==SenderType){
            ((SendMessageHolder)holder).bind(message);
        }else {
            ((ReceivedMessageHolder)holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        public ReceivedMessageHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.get_message_text);
            timeText = itemView.findViewById(R.id.get_message_timestamp);
        }


        void bind(MessageTable table) {
            messageText.setText(table.getMessage());
            timeText.setText(table.getMessageClock());

        }

    }

    class SendMessageHolder extends RecyclerView.ViewHolder {

        TextView messageText, timeText;

        public SendMessageHolder(View view) {
            super(view);
            messageText = view.findViewById(R.id.send_message_text);
            timeText = view.findViewById(R.id.send_message_timestamp);
        }

        void bind(MessageTable table) {

            messageText.setText(table.getMessage());
            timeText.setText(table.getMessageClock());

        }


    }
}
