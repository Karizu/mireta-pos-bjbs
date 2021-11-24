package com.boardinglabs.mireta.standalone.modul.old.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.paging.listview.PagingListView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyFeedChatPreviewAdapter;
import com.boardinglabs.mireta.standalone.component.listener.ListActionListener;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GAgent;
import com.boardinglabs.mireta.standalone.component.network.gson.GCostumer;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransferRequestLog;
import com.boardinglabs.mireta.standalone.component.network.gson.GTransferRequestLogGroup;
import com.boardinglabs.mireta.standalone.component.util.Constant;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.feed.chats.ChatActivity;
import com.boardinglabs.mireta.standalone.modul.old.feed.chats.ChatPresenter;
import com.boardinglabs.mireta.standalone.modul.auth.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class FeedChatFragment extends Fragment implements ListActionListener, ChatPresenter.ChatView, CommonInterface {

    private PagingListView feedsList;
    private RecyFeedChatPreviewAdapter feedsAdapter;
    private SwipeRefreshLayout pullToRefresh;
    private ChatPresenter chatPresenter;
    private RelativeLayout empty;

    public FeedChatFragment() {
        // Required empty public constructor
    }

    public static FeedChatFragment newInstance() {
        FeedChatFragment fragment = new FeedChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chatPresenter != null)
            loadData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed_chat, container, false);
        chatPresenter = new ChatPresenter(this, this);
        initFeed(v);
        return v;
    }

    private void initFeed(View view) {
        empty = (RelativeLayout) view.findViewById(R.id.empty);
        feedsAdapter = new RecyFeedChatPreviewAdapter();
        GAgent agent = PreferenceManager.getAgent();
        feedsAdapter.agent = agent;
        feedsList = (PagingListView) view.findViewById(R.id.feed_list);
        feedsList.setAdapter(feedsAdapter);
        feedsAdapter.setListener(this);
        List<GTransferRequestLogGroup> feedData = new ArrayList<>();

        feedsList.setHasMoreItems(false);
        feedsList.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {

            }
        });

        feedsAdapter.setDataList(new ArrayList<GTransferRequestLogGroup>());

        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        loadData();
    }

    private void loadData(){
        //showProgressLoading();
        chatPresenter.getLogGroups();
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
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        GCostumer customer = null;
        GTransferRequestLogGroup group = feedsAdapter.feedList.get(position);
        if (group.activity_by.equalsIgnoreCase(PreferenceManager.getAgent().id)){
            customer = group.activity_to_customer;
        }
        else{
            customer = group.activity_by_customer;
        }
        String to_customer_id = customer.id;
        String name = customer.name;
        String avatarbase64 = customer.avatar_base64;
        intent.putExtra("to_customer_id", to_customer_id);
        intent.putExtra("name", name);
        intent.putExtra("avatar_base64", avatarbase64);
        startActivity(intent);
    }

    @Override
    public void itemDeleted(int position) {

    }


    @Override
    public void showProgressLoading() {
        ((BaseActivity) getActivity()).progressBar.show(getActivity(), "", false, null);
    }

    @Override
    public void hideProgresLoading() {
        ((BaseActivity) getActivity()).progressBar.getDialog().dismiss();
    }

    @Override
    public NetworkService getService() {
        return NetworkManager.getInstance();
    }

    @Override
    public void onFailureRequest(String msg) {
//        MethodUtil.showCustomToast(getActivity(), msg, R.drawable.ic_error_login);
        empty.setVisibility(View.VISIBLE);
        feedsList.setVisibility(View.GONE);
        feedsList.setIsLoading(false);
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
    public void successGetTransferRequestLogGroups(List<GTransferRequestLogGroup> logGroups) {
        feedsAdapter.setDataList(logGroups);
        feedsList.setIsLoading(false);
        if (logGroups.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            feedsList.setVisibility(View.GONE);
        }else{
            empty.setVisibility(View.GONE);
            feedsList.setVisibility(View.VISIBLE);
        }
        pullToRefresh.setRefreshing(false);
    }

    @Override
    public void successGetTransferRequestLogs(List<GTransferRequestLog> logs, GCostumer customer, GCostumer to_customer) {
        feedsList.setIsLoading(false);
    }

    @Override
    public void onFailureGetTransferRequestLogGroups(String message) {
        pullToRefresh.setRefreshing(false);
    }


}
