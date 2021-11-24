package com.boardinglabs.mireta.standalone.component.network.oldresponse;

import com.boardinglabs.mireta.standalone.component.network.gson.GPost;

import java.util.List;

public class PostsResponse {
    public boolean success;
    public String message;
    public int total_post;
    public List<GPost> posts;
}
