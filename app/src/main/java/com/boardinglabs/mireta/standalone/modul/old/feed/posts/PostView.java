package com.boardinglabs.mireta.standalone.modul.old.feed.posts;

import com.boardinglabs.mireta.standalone.component.network.gson.GPost;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.PostsResponse;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface PostView {
    void successFetchFeed(PostsResponse postsResponse);

    void successGetPostDetail(GPost post);

    void successAddComment(GPost post);

    void successDeleteComment(GPost post);

    void successDeletePost(String post_id);

    void successLikeDislikePost(GPost post);

    void successCreatePost(GPost post);
}
