package com.boardinglabs.mireta.standalone.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boardinglabs.mireta.standalone.R;

/**
 * Created by Dhimas on 11/3/17.
 */

public class RecyDonateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_donate_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView donateImg;
        private TextView donateInfo;
        private TextView donateTitle;
        private Button donateBtn;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
