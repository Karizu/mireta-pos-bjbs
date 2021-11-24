package com.boardinglabs.mireta.standalone.component.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.gson.GPostComment;
import com.boardinglabs.mireta.standalone.component.util.DateHelper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dhimas on 12/20/17.
 */

public class RecyFeedCommentAdapter extends BaseSwipeAdapter {
    private List<GPostComment> commentList;

    private ListActionListener itemActionListener;

    public RecyFeedCommentAdapter() {
        commentList = new ArrayList<>();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.cmt_feed_row_swipe_layout;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.setSwipeEnabled(false);
        swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemActionListener != null) itemActionListener.itemClicked(position);
            }
        });

        v.findViewById(R.id.row_feed_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemActionListener != null) itemActionListener.itemClicked(position);
            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView commentName = (TextView) convertView.findViewById(R.id.comment_name);
        TextView commentTime = (TextView) convertView.findViewById(R.id.comment_time);
        TextView commentContent = (TextView) convertView.findViewById(R.id.comment_content);
        CircleImageView avatar_img = (CircleImageView) convertView.findViewById(R.id.avatar_img);


        GPostComment comment = commentList.get(position);
        String date = comment.created_at;
        String timelineDate = DateHelper.formatDelay(date);
        if (timelineDate == null && timelineDate.equals("")){
            timelineDate = "Sekarang";
        }
        commentName.setText(comment.customer.name);
        commentTime.setText(((timelineDate != null && !timelineDate.equals(""))?timelineDate:"Sekarang"));
        commentContent.setText(comment.text);

        if (comment.customer != null && comment.customer.avatar_base64 != null && !comment.customer.avatar_base64.equals("")){
            byte[] decodedString = Base64.decode(comment.customer.avatar_base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatar_img.setImageBitmap(decodedByte);
        }
    }

    public void setListener(ListActionListener listClicked) {
        this.itemActionListener = listClicked;
    }

    public void setDataList(List<GPostComment> feeds) {
        commentList = feeds;
        notifyDataSetChanged();
    }

    public void addDataList(List<GPostComment> feeds) {
        if (commentList == null){
            commentList = new ArrayList<>();
        }
        commentList.addAll(feeds);
        notifyDataSetChanged();
    }

    public List<GPostComment> getCommentList(){
        return commentList;
    }

    @Override
    public int getCount() {
        if (commentList == null){
            commentList = new ArrayList<>();
        }
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
