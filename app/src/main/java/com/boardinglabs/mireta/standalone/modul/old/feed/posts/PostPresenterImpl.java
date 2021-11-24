package com.boardinglabs.mireta.standalone.modul.old.feed.posts;

import com.boardinglabs.mireta.standalone.component.network.ResponeError;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.MessageResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.PostsResponse;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.SinglePostResponse;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import rx.Subscriber;

/**
 * Created by Dhimas on 11/29/17.
 */

public class PostPresenterImpl implements PostPresenter {
    private CommonInterface cInterface;
    private PostView mView;
    private PostInteractor mInteractor;

    public PostPresenterImpl(CommonInterface commonInterface, PostView view) {
        mView = view;
        cInterface = commonInterface;
        mInteractor = new PostInteractorImpl(cInterface.getService());
    }

    @Override
    public void fetchFeed(int page){
        cInterface.showProgressLoading();
        mInteractor.fetchFeed(page).subscribe(new Subscriber<PostsResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(PostsResponse postsResponse) {
                cInterface.hideProgresLoading();
                if (postsResponse.success){
                    mView.successFetchFeed(postsResponse);
                }
                else{
                    cInterface.onFailureRequest(postsResponse.message);
                }
            }
        });
    }

    @Override
    public void getPostDetail(String post_id){
        cInterface.showProgressLoading();
        mInteractor.getPostDetail(post_id).subscribe(new Subscriber<SinglePostResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(SinglePostResponse singlePostResponse) {
                cInterface.hideProgresLoading();
                if (singlePostResponse.success){
                    mView.successGetPostDetail(singlePostResponse.post);
                }
                else{
                    cInterface.onFailureRequest(singlePostResponse.message);
                }
            }
        });
    }

    @Override
    public void addComment(String post_id, String text){
        cInterface.showProgressLoading();
        mInteractor.addComment(post_id, text).subscribe(new Subscriber<SinglePostResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(SinglePostResponse singlePostResponse) {
                cInterface.hideProgresLoading();
                if (singlePostResponse.success){
                    mView.successAddComment(singlePostResponse.post);
                }
                else{
                    cInterface.onFailureRequest(singlePostResponse.message);
                }
            }
        });
    }

    @Override
    public void deleteComment(String comment_id){
        cInterface.showProgressLoading();
        mInteractor.deleteComment(comment_id).subscribe(new Subscriber<SinglePostResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(SinglePostResponse singlePostResponse) {
                cInterface.hideProgresLoading();
                if (singlePostResponse.success){
                    mView.successDeleteComment(singlePostResponse.post);
                }
                else{
                    cInterface.onFailureRequest(singlePostResponse.message);
                }
            }
        });
    }

    @Override
    public void deletePost(String post_id){
        cInterface.showProgressLoading();
        mInteractor.deletePost(post_id).subscribe(new Subscriber<MessageResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                cInterface.hideProgresLoading();
                if (messageResponse.success){
                    mView.successDeletePost(messageResponse.obj_id);
                }
                else{
                    cInterface.onFailureRequest(messageResponse.message);
                }
            }
        });
    }

    @Override
    public void likeDislikePost(String post_id){
        cInterface.showProgressLoading();
        mInteractor.likeDislikePost(post_id).subscribe(new Subscriber<SinglePostResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(SinglePostResponse singlePostResponse) {
                cInterface.hideProgresLoading();
                if (singlePostResponse.success){
                    mView.successLikeDislikePost(singlePostResponse.post);
                }
                else{
                    cInterface.onFailureRequest(singlePostResponse.message);
                }
            }
        });
    }

    @Override
    public void createPost(String text){
        cInterface.showProgressLoading();
        mInteractor.createPost(text).subscribe(new Subscriber<SinglePostResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                cInterface.hideProgresLoading();
                cInterface.onFailureRequest(ResponeError.getErrorMessage(e));
            }

            @Override
            public void onNext(SinglePostResponse singlePostResponse) {
                cInterface.hideProgresLoading();
                if (singlePostResponse.success){
                    mView.successCreatePost(singlePostResponse.post);
                }
                else{
                    cInterface.onFailureRequest(singlePostResponse.message);
                }
            }
        });
    }
}
