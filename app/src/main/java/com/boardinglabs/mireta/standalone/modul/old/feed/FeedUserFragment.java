package com.boardinglabs.mireta.standalone.modul.old.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paging.listview.PagingListView;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyFeedAdapter;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GPost;
import com.boardinglabs.mireta.standalone.component.network.oldresponse.PostsResponse;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.feed.posts.PostActionListener;
import com.boardinglabs.mireta.standalone.modul.old.feed.posts.PostDetailActivity;
import com.boardinglabs.mireta.standalone.modul.old.feed.posts.PostPresenter;
import com.boardinglabs.mireta.standalone.modul.old.feed.posts.PostPresenterImpl;
import com.boardinglabs.mireta.standalone.modul.old.feed.posts.PostView;
import com.boardinglabs.mireta.standalone.modul.auth.login.LoginActivity;

public class FeedUserFragment extends Fragment implements ListActionListener, CommonInterface, PostView, PostActionListener {

    private PagingListView feedsList;
    private RecyFeedAdapter feedsAdapter;
    private SwipeRefreshLayout pullToRefresh;
    private int currentPage;
    private int total_post;

    private PostPresenter mPresenter;

    public FeedUserFragment() {
        // Required empty public constructor
    }

    public static FeedUserFragment newInstance() {
        FeedUserFragment fragment = new FeedUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_feed_user, container, false);
        initFeed(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            loadPostsData();
    }

    private void initFeed(View view) {
        total_post = 0;
        currentPage = 0;
        mPresenter = new PostPresenterImpl(this, this);

        feedsAdapter = new RecyFeedAdapter();
        feedsAdapter.setListener(this);
        feedsAdapter.setActionListener(this);

        feedsList = (PagingListView) view.findViewById(R.id.feed_list);
        feedsList.setAdapter(feedsAdapter);

        feedsList.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (currentPage > 0){
                    if (feedsAdapter.getCount() < total_post){
                        loadMorePostsData();
                    }
                    else{
                        feedsList.onFinishLoading(false, null);
                    }
                }
            }
        });
        loadPostsData();

        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPostsData();
            }
        });
    }

    private void loadPostsData(){
        currentPage = 0;
        mPresenter.fetchFeed(currentPage+1);
    }

    private void loadMorePostsData(){
        mPresenter.fetchFeed(currentPage+1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void itemClicked(int position) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        String post_id = feedsAdapter.feedList.get(position).id;
        intent.putExtra("post_id", post_id);
        startActivity(intent);
    }

    @Override
    public void itemDeleted(int position) {
        mPresenter.deletePost(feedsAdapter.feedList.get(position).id);
    }


    @Override
    public void showProgressLoading() {
//        ((BaseActivity) getActivity()).progressBar.show(getActivity(), "", false, null);
    }

    @Override
    public void hideProgresLoading() {
//        ((BaseActivity) getActivity()).progressBar.getDialog().dismiss();
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
//        MethodUtil.showCustomToast(getActivity(), msg, R.drawable.ic_error_login);
        if (msg.equalsIgnoreCase(Constant.EXPIRED_SESSION) || msg.equalsIgnoreCase(Constant.EXPIRED_ACCESS_TOKEN)) {
            PreferenceManager.logOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void successFetchFeed(PostsResponse postsResponse) {
        total_post = postsResponse.total_post;
        if (postsResponse.posts.size() < total_post){
            feedsList.onFinishLoading(true, null);
        }
        else {
            feedsList.onFinishLoading(false, null);
        }
        if (currentPage == 0){
            feedsAdapter.setDataList(postsResponse.posts);
            currentPage = 1;
        }else{
            feedsAdapter.addDataList(postsResponse.posts);
            currentPage += 1;
        }
        pullToRefresh.setRefreshing(false);
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
        feedsAdapter.deleteItem(post_id);
    }

    @Override
    public void successLikeDislikePost(GPost post) {
        feedsAdapter.replaceItem(post.id, post);
    }

    @Override
    public void successCreatePost(GPost post) {

    }

    @Override
    public void likeDislikePost(GPost post) {
        mPresenter.likeDislikePost(post.id);
    }

    @Override
    public void sharePost(GPost post) {
        if (post != null && post.customer != null && post.customer.name != null && post.text != null){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Doomo");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, post.customer.name + " via Doomo: "+post.text);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }


}
