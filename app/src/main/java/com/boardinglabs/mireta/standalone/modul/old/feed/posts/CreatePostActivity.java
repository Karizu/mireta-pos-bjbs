package com.boardinglabs.mireta.standalone.modul.old.feed.posts;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GPost;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.PostsResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.oldhome.HomePageActivity;

public class CreatePostActivity extends BaseActivity implements CommonInterface, PostView{

    private EditText postTextArea;
    private Button postButton;
    private PostPresenter mPresenter;

    private Context mContext;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.post_create_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitleWithCloseButton("TULIS");
    }

    @Override
    protected void onCreateAtChild() {
        mContext = this;
        postTextArea = (EditText) findViewById(R.id.post_text_area);
        postButton = (Button) findViewById(R.id.post_btn);
        mPresenter = new PostPresenterImpl(this, this);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(postTextArea.getText().toString().isEmpty())){
                    if(!(postTextArea.getText().toString().length() >160)) {
                        mPresenter.createPost(postTextArea.getText().toString());
                    }else{
                        Toast.makeText(mContext,"Maximum karakter yang dapat diinput 160 karakter",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(mContext,"Post tidak boleh kosong",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    @Override
    public void showProgressLoading() {
        progressBar.show(this, "", false, null);
    }

    @Override
    public void hideProgresLoading() {
        progressBar.getDialog().dismiss();
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
        MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            goToLoginPage1(this);
        }

    }

    @Override
    public void successFetchFeed(PostsResponse postsResponse) {
    }

    @Override
    public void successGetPostDetail(GPost post) {

    }

    @Override
    public void successAddComment(GPost post) {

    }


    @Override
    public void successDeleteComment(GPost post) {

    }

    @Override
    public void successDeletePost(String post_id) {

    }

    @Override
    public void successLikeDislikePost(GPost post) {

    }

    @Override
    public void successCreatePost(GPost post) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
