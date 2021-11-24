package com.boardinglabs.mireta.standalone.modul.old.feed.posts;

import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.PostsResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.SinglePostResponse;

import rx.Observable;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface PostInteractor {
    Observable<PostsResponse> fetchFeed(int page);

    Observable<SinglePostResponse> getPostDetail(String post_id);

    Observable<SinglePostResponse> addComment(String post_id, String text);

    Observable<SinglePostResponse> deleteComment(String comment_id);

    Observable<MessageResponse> deletePost(String post_id);

    Observable<SinglePostResponse> likeDislikePost(String post_id);

    Observable<SinglePostResponse> createPost(String text);
}
