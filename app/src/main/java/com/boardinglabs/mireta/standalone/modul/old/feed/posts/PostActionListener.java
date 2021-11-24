package com.boardinglabs.mireta.standalone.modul.old.feed.posts;

import com.boardinglabs.mireta.standalone.component.network.gson.GPost;

public interface PostActionListener {
    void likeDislikePost(GPost post);

    void sharePost(GPost post);
}
