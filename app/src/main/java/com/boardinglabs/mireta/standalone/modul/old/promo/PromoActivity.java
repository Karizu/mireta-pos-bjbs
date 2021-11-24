package com.boardinglabs.mireta.standalone.modul.old.promo;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.component.util.PreferenceManager;
import com.boardinglabs.mireta.standalone.modul.old.checkpasscode.CheckPasscodeActivity;
import com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpActivity;
import com.boardinglabs.mireta.standalone.modul.old.register.passcode.PasscodeActivity;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static com.boardinglabs.mireta.standalone.modul.old.register.otp.OtpActivity.IS_FORGOT;

/**
 * Created by Dhimas on 2/16/18.
 */

public class PromoActivity extends BaseActivity {
//    private RecyclerView promoList;
//    private RecyPromoAdapter mAdapter;
//    private PromoPresenter mPresenter;

    private SectionsPagerAdapter pagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.promo_activity_layout;
    }

    @Override
    protected void setContentViewOnChild() {
//        mAdapter = new RecyPromoAdapter(this);
//        promoList = (RecyclerView) findViewById(R.id.promo_list);
//        promoList.setLayoutManager(new LinearLayoutManager(this));
//        promoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        promoList.setAdapter(mAdapter);

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.all_trans_viewPager);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.all_trans_tab);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        pagerAdapter.addTabFragment(new PromoFragment().newInstance());
//        pagerAdapter.addTabFragment(new DealsFragment().newInstance());

        if (getIntent().getBooleanExtra("isDeals", false)) {
            mViewPager.setCurrentItem(1);
        }

        menuBtn.setVisibility(View.GONE);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.back_button));
        setSlidingMenu(this);
        RxView.clicks(changePassode).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(PromoActivity.this, CheckPasscodeActivity.class);
                String[] user = PreferenceManager.getUserInfo();
                intent.putExtra(OtpActivity.MOBILE, user[1]);
                startActivityForResult(intent, CHANGE_PASSCODE);
            }
        });
    }

    @Override
    protected void onCreateAtChild() {
//        mPresenter = new PromoPresenterImpl(this, this);
//        mPresenter.getListPromo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHANGE_PASSCODE) {
                Intent intent = new Intent(PromoActivity.this, PasscodeActivity.class);
                intent.putExtra(IS_FORGOT, true);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

//    @Override
//    public void onClickPromo(String promoId) {
////        Intent intent = new Intent(PromoActivity.this, PromoDetailActivity.class);
////        intent.putExtra("promoId", promoId);
////        startActivity(intent);
//    }
//
//    @Override
//    public void showProgressLoading() {
//        progressBar.show(this, "", false, null);
//    }
//
//    @Override
//    public void hideProgresLoading() {
//        progressBar.getDialog().dismiss();
//    }
//
//    @Override
//    public NetworkService getService() {
//        return NetworkManager.getInstance();
//    }
//
//    @Override
//    public void onFailureRequest(String msg) {
//        MethodUtil.showCustomToast(this, msg, R.drawable.ic_error_login);
//    }
//
//    @Override
//    public void onSuccessGetList(List<GPromo> promoList) {
//        mAdapter.setDataPromo(promoList);
//    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragment;

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.mFragment = new ArrayList<>();
        }

        private void addTabFragment(Fragment fragment) {
            mFragment.add(fragment);
            notifyDataSetChanged();
        }

        private void removeTabFragment() {
            mFragment.clear();
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return this.mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment != null ? mFragment.size() : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Promo";
                case 1:
                    return "Deals";
                default:
                    return "";
            }
        }
    }
}
