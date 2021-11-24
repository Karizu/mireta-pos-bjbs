package com.boardinglabs.mireta.standalone.modul.old.feed.posts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.paging.listview.PagingListView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyFeedCommentAdapter;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.gson.GPost;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.PostsResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.DateHelper;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends BaseActivity implements ListActionListener, CommonInterface, PostView {

    private PagingListView feedsList;
    private RecyFeedCommentAdapter feedsAdapter;
    private SwipeRefreshLayout pullToRefresh;
    private TextView nameTV;
    private TextView usernameTV;
    private TextView postContentTV;
    private TextView postLikesTV;
    private TextView postCommentsTV;
    private EditText commentTextArea;
    private CircleImageView avatarImageView;
    private Button postCommentButton;
    private PostPresenter mPresenter;
    private String postID;
    private GPost post;
    private ImageView icLikeActive;
    private ImageView icLike;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.post_detail_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("POST");
        initPost();
    }

    @Override
    protected void onCreateAtChild() {
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    private void initPost() {
        mPresenter = new PostPresenterImpl(this, this);
        postID = getIntent().getStringExtra("post_id");
        icLike = (ImageView) findViewById(R.id.icon_like);
        icLikeActive = (ImageView) findViewById(R.id.icon_like_active);
        nameTV = (TextView) findViewById(R.id.name_tv);
        usernameTV = (TextView) findViewById(R.id.username_tv);
        postContentTV = (TextView) findViewById(R.id.post_text_tv);
        postLikesTV = (TextView) findViewById(R.id.likes_count_tv);
        postCommentsTV = (TextView) findViewById(R.id.comments_count_tv);
        commentTextArea = (EditText) findViewById(R.id.comment_textarea);
        postCommentButton = (Button) findViewById(R.id.submit_comment_btn);
        avatarImageView = (CircleImageView) findViewById(R.id.profile_iv);
        ImageView shareButton = (ImageView) findViewById(R.id.share_button_detail);

        feedsAdapter = new RecyFeedCommentAdapter();
        feedsAdapter.setListener(this);

        feedsList = (PagingListView) findViewById(R.id.comment_list);
        feedsList.setAdapter(feedsAdapter);

        feedsList.setHasMoreItems(true);
        feedsList.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                    }
                }, 3000);
            }
        });

        mPresenter.getPostDetail(postID);
        postLikesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.likeDislikePost(postID);
            }
        });

        postCommentButton.setVisibility(View.GONE);
        commentTextArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                postCommentButton.setVisibility((charSequence.toString().length()>0)?View.VISIBLE:View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressLoading();
                mPresenter.addComment(postID, commentTextArea.getText().toString());

            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePost();
            }
        });

    }

//    private List<GComment> loadMoreItems(){
//        List<GComment> feedData = new ArrayList<>();
//        for(int i=0;i<5;i++){
//            feedData.add(new GComment("Name " + i,"Dummy Username","Lorem Ipsum"));
//        }
//        return feedData;
//    }

    @Override
    public void itemClicked(int position) {
    }

    @Override
    public void itemDeleted(int position) {
        mPresenter.deleteComment(feedsAdapter.getCommentList().get(position).id);
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

    public void refreshView(GPost post){
        nameTV.setText(post.customer.name);
        String date = post.created_at;
        String timelineDate = DateHelper.formatDelay(date);
        if (timelineDate == null && timelineDate.equals("")){
            timelineDate = "Sekarang";
        }
        usernameTV.setText(timelineDate);
        postLikesTV.setText((post.likes_count != null ? " " + post.likes_count : ""));
        if(post.is_like) {
            icLike.setVisibility(View.GONE);
            icLikeActive.setVisibility(View.VISIBLE);
        }
        else{
            icLike.setVisibility(View.VISIBLE);
            icLikeActive.setVisibility(View.GONE);
        }
        postCommentsTV.setText((post.comments_count != null ? " " + post.comments_count : ""));

        postContentTV.setText(post.text);
        feedsAdapter.setDataList(post.comments);
        feedsList.onFinishLoading(false, null);
        this.post = post;
        if (post.customer != null && post.customer.avatar_base64 != null && !post.customer.avatar_base64.equals("")){
            byte[] decodedString = Base64.decode(post.customer.avatar_base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatarImageView.setImageBitmap(decodedByte);
        }
    }

    @Override
    public void successGetPostDetail(GPost post) {
        refreshView(post);
    }

    @Override
    public void successAddComment(GPost post) {
        refreshView(post);
        commentTextArea.setText("");
        hideProgresLoading();
    }

    @Override
    public void successDeleteComment(GPost post) {
        refreshView(post);
    }

    @Override
    public void successDeletePost(String post_id) {

    }

    @Override
    public void successLikeDislikePost(GPost post) {
        refreshView(post);
    }

    @Override
    public void successCreatePost(GPost post) {

    }

    public void sharePost() {
        if (post != null && post.customer != null && post.customer.name != null && post.text != null){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Doomo");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, post.customer.name + " via Doomo: "+post.text);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }
}