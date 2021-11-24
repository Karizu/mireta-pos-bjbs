package com.boardinglabs.mireta.standalone.modul.old.feed.posts;

/**
 * Created by Dhimas on 11/29/17.
 */

public interface PostPresenter {
    void fetchFeed(int page);

    void getPostDetail(String post_id);

    void addComment(String post_id, String text);

    void deleteComment(String comment_id);

    void deletePost(String post_id);

    void likeDislikePost(String post_id);

    void createPost(String text);
}
