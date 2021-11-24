package com.boardinglabs.mireta.standalone.modul.old.feed.posts;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.PostsResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.SinglePostResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 11/29/17.
 */

public class PostInteractorImpl implements PostInteractor {
    private NetworkService mService;

    public PostInteractorImpl(NetworkService service) {
        mService = service;
    }
    
    @Override
    public Observable<PostsResponse> fetchFeed(int page){
        return mService.fetchFeed(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<SinglePostResponse> getPostDetail(String post_id){
        return mService.getPostDetail(post_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<SinglePostResponse> addComment(String post_id, String text){
        return mService.addComment(post_id, true, text).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<SinglePostResponse> deleteComment(String comment_id){
        return mService.deleteComment(comment_id, true).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<MessageResponse> deletePost(String post_id){
        return mService.deletePost(post_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<SinglePostResponse> likeDislikePost(String post_id){
        return mService.likeDislikePost(post_id, true).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<SinglePostResponse> createPost(String text){
        return mService.createPost(text).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
    
}
