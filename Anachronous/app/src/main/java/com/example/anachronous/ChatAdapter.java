package com.example.anachronous;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;
    List<Chat> mChats;

    public ChatAdapter(List<Chat> mChats){
        this.mChats = mChats;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(TextUtils.equals(mChats.get(position).senderUid, FirebaseAuth.getInstance().getCurrentUser().getUid())){
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        }else{
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(final MyChatViewHolder myChatViewHolder, int position){
        Chat chat = mChats.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String date = sdf.format(new Date(chat.timestamp).getTime());
        myChatViewHolder.senderMsgTime.setText(date);
    }

    private void configureOtherChatViewHolder(final OtherChatViewHolder otherChatViewHolder, int position){
        final Chat chat = mChats.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String date = sdf.format(new Date(chat.timestamp).getTime());
        otherChatViewHolder.receiverMsgTime.setText(date);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(TextUtils.equals(mChats.get(position).senderUid, FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return VIEW_TYPE_ME;
        }else{
            return VIEW_TYPE_OTHER;
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder{

        private TextView txtChatMessage, txtUserAlphabet;
        private TextView senderMsgTime;

        public MyChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            senderMsgTime = (TextView) itemView.findViewById(R.id.senderMsgTime);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder{

        private TextView txtChatMessage, txtUserAlphabet;
        private TextView receiverMsgTime;


        public OtherChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            receiverMsgTime = (TextView) itemView.findViewById(R.id.receiverMsgTime);
        }
    }
}
