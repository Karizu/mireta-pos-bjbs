package com.boardinglabs.mireta.standalone.component.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GPost;
import com.boardinglabs.mireta.standalone.component.util.DateHelper;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.old.feed.posts.PostActionListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dhimas on 12/20/17.
 */

public class RecyFeedAdapter extends BaseSwipeAdapter {
    public List<GPost> feedList;
    private ListActionListener itemActionListener;
    private PostActionListener postActionListener;
    private GAgent agent;
    private ImageView icLikeActive;
    private ImageView icLike;

    public RecyFeedAdapter() {

        agent = PreferenceManager.getAgent();
        feedList = new ArrayList<>();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.user_feed_row_swipe_layout;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, null);
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        final GPost post = feedList.get(position);

        return v;
    }

    @Override
    public void fillValues(final int position, View convertView) {

        final GPost post = feedList.get(position);
        final SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
        final TextView customer_name = convertView.findViewById(R.id.customer_name);
        TextView customer_time = convertView.findViewById(R.id.customer_time);
        TextView likes_count = convertView.findViewById(R.id.likes_count);
        TextView comments_count = convertView.findViewById(R.id.comments_count);
        TextView post_text = convertView.findViewById(R.id.post_text);
        CircleImageView avatar_img = convertView.findViewById(R.id.avatar_img);
        ImageView shareButton = convertView.findViewById(R.id.share_button);
        icLike = convertView.findViewById(R.id.icon_like);
        icLikeActive = convertView.findViewById(R.id.icon_like_active);

        if(post.is_like) {
            icLike.setVisibility(View.GONE);
            icLikeActive.setVisibility(View.VISIBLE);
        }
        else{
            icLike.setVisibility(View.VISIBLE);
            icLikeActive.setVisibility(View.GONE);
        }

        post_text.setText(post.text);
        customer_name.setText(post.customer.name);

        String date = post.created_at;
        String timelineDate = DateHelper.formatDelay(date);
        if (timelineDate == null && timelineDate.equals("")){
            timelineDate = "Sekarang";
        }
        customer_time.setText(timelineDate); //2018-12-27T15:44:24+07:00

        likes_count.setText((post.likes_count != null ? " " + post.likes_count : ""));
        comments_count.setText((post.comments_count != null ? " " + post.comments_count : ""));
        if (post.customer != null && post.customer.avatar_base64 != null && !post.customer.avatar_base64.equals("")){
            byte[] decodedString = Base64.decode(post.customer.avatar_base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatar_img.setImageBitmap(decodedByte);
        }
        else{
            avatar_img.setImageResource(R.drawable.ic_avatar);
        }

        likes_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postActionListener.likeDislikePost(post);
            }
        });

        icLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postActionListener.likeDislikePost(post);
            }
        });

        icLikeActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postActionListener.likeDislikePost(post);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                shareContent(post.customer.name+" on Pampassy: "+post.text);
                postActionListener.sharePost(post);
            }
        });

        if (post.customer_id.equalsIgnoreCase(agent.id)){
            swipeLayout.setSwipeEnabled(true);

        }
        else{

            swipeLayout.setSwipeEnabled(false);
        }
        swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemActionListener != null) itemActionListener.itemClicked(position);
            }
        });

        convertView.findViewById(R.id.row_feed_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemActionListener != null) itemActionListener.itemDeleted(position);
                swipeLayout.close();
            }
        });
    }

    private void shareContent(String text){
//        this.itemActionListener.itemClicked();
    }

    public void setListener(ListActionListener listClicked) {
        this.itemActionListener = listClicked;
    }

    public void setDataList(List<GPost> posts) {
        feedList = posts;
        notifyDataSetChanged();
    }

    public void deleteItem(String post_id) {
        int index = 0;
        for (GPost post : feedList) {
            if (post.id.equals(post_id)){
                break;
            }
            index++;
        }
        feedList.remove(index);
        notifyDataSetChanged();
    }

    public void replaceItem(String post_id, GPost newPost) {
        int index = 0;
        for (GPost post : feedList) {
            if (post.id.equals(post_id)){
                break;
            }
            index++;
        }
        feedList.set(index, newPost);
//        feedList.get(index).likes_count = ((newPost.likes_count != null) ? " " + newPost.likes_count : "");
//        if(Integer.parseInt(feedList.get(index).likes_count.trim())<Integer.parseInt(newPost.likes_count.trim())){
//            //this.f.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_24_active, 0, 0, 0);
//        }else{
//            //feedList.get(index).likes_count.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_24_active, 0, 0, 0);
//        }
        notifyDataSetChanged();
    }

    public void addDataList(List<GPost> posts) {
        if (feedList == null){
            feedList = new ArrayList<>();
        }
        feedList.addAll(posts);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return feedList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setActionListener(PostActionListener listClicked) {
        this.postActionListener = listClicked;
    }

}
