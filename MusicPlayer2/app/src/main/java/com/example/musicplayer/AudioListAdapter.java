package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AudioListAdapter extends RecyclerView.Adapter {

    private List audioDataSet;
    private LayoutInflater mInflater;
    Context mContext;

    public AudioListAdapter(Context context, List audioModelList){

        mInflater = LayoutInflater.from(context);
        mContext = context;
        audioDataSet = audioModelList;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = mInflater.inflate(R.layout.audio_list_row, viewGroup, false);

        AudioViewHolder vh = new AudioViewHolder(mContext,audioDataSet,v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder audioViewHolder, int i) {
        audioViewHolder.mTextView.setText(audioDataSet.get(i).getaName());
    }


    @Override
    public int getItemCount() {
        return audioDataSet.size();
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Context nContext;
        List audioList;
        public TextView mTextView;

        public AudioViewHolder(Context context,List audioModelList, View v) {
            super(v);
            nContext = context;
            audioList = audioModelList;
            mTextView = v.findViewById(R.id.audioName);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){

            int itemPosition = getAdapterPosition();

            Intent intent = new Intent(nContext,MusicPlayerActivity.class);
            intent.putExtra("audio",audioList.get(itemPosition));

            nContext.startActivity(intent);

        }
    }
}
