package com.boardinglabs.mireta.standalone.modul.old.creditcard.savedcreditcard;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.adapter.RecyCreditcardAdapter;
import com.boardinglabs.mireta.standalone.component.network.NetworkManager;
import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GCard;
import com.boardinglabs.mireta.standalone.component.util.MethodUtil;
import com.boardinglabs.mireta.standalone.modul.CommonInterface;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.addcreditcard.AddCreditcardActivity;
import com.boardinglabs.mireta.standalone.modul.old.creditcard.detailcreditcard.DetailCreditcardActivity;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Dhimas on 2/6/18.
 */

public class SavedCreditcardActivity extends BaseActivity implements SavedCreditcardView, CommonInterface, RecyCreditcardAdapter.ActionAdapter{
    private RecyCreditcardAdapter mAdapter;
    private RecyclerView cardList;
    private SavedCreditcardPresenter mPresenter;
    private RecyCreditcardAdapter.SimpleViewHolder mViewHolder;
    private LinearLayout addCreditcard;
    private int mPosition;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.saved_creditcard_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Simpan Kartu");
        addCreditcard = (LinearLayout) findViewById(R.id.add_credit_card);
        mAdapter = new RecyCreditcardAdapter(this);
        cardList = (RecyclerView) findViewById(R.id.card_list);
        cardList.setLayoutManager(new LinearLayoutManager(this));
//        cardList.addItemDecoration(new DividerItemDecoration(this, ContextCompat.getDrawable(this, R.drawable.divider)));
        cardList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        cardList.setAdapter(mAdapter);

        RxView.clicks(addCreditcard).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(SavedCreditcardActivity.this, AddCreditcardActivity.class);
                if (mAdapter.getItemCount() > 0) {
                    intent.putExtra("isHaveList", true);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreateAtChild() {
        mPresenter = new SavedCreditcardPresenterImpl(this, this);
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    @Override
    public void onSuccessListCreditcard(List<GCard> cardList) {
        mAdapter.setCardList(cardList);
    }

    @Override
    public void onDeleteCard(int position, RecyCreditcardAdapter.SimpleViewHolder viewHolder) {
        String cardId = mAdapter.getCard(position).id;
        mPresenter.onDeleteCard(cardId);
        mPosition = position;
        mViewHolder = viewHolder;
    }

    @Override
    public void onShowDetail(int position, String digit) {
        GCard card = mAdapter.getCard(position);
        String expired = card.expiry_month + "/" + card.expiry_year;
        Intent intent = new Intent(this, DetailCreditcardActivity.class);
        intent.putExtra("card_number", digit);
        intent.putExtra("card_type", card.card_type);
        intent.putExtra("card_expired", expired);
        intent.putExtra("card_token", card.card_token);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onSuccessDeleteCard() {
        mAdapter.onDeleteCreditCard(mPosition, mViewHolder);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getListCard();
    }
}
