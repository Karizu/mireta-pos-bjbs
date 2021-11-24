package com.boardinglabs.mireta.standalone.modul.old.onboard;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.itsronald.widget.ViewPagerIndicator;
import com.jakewharton.rxbinding.view.RxView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.boardinglabs.mireta.standalone.R;


import rx.functions.Action1;

public class OnBoardActivity extends AppCompatActivity {
    ViewPager onBoardPager;
    Button leftButton;
    Button rightButton;
    ViewPagerIndicator pagerIndicator;


    int[] resource = {R.drawable.img_onboard_1,R.drawable.img_onboard_2,R.drawable.img_onboard_3,R.drawable.img_onboard_4};
    String[] title = {"PPOB","PAYMENT","SCAN QR","FEEDS"};
    String[] description = {
            "Beli kebutuhan bulananmu disini.",
            "Transaksi jadi lebih cepat tanpa dompet.",
            "Cukup SCAN QR transaksi lebih praktis.",
            "Update status dengan gaya baru."
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        initComponent();
    }

    private void initComponent(){
        onBoardPager =  (ViewPager) findViewById(R.id.on_board_pager);
        leftButton = (Button) findViewById(R.id.leftButton);
        RxView.clicks(leftButton).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //endOnBoard();
            }
        });
        rightButton = (Button) findViewById(R.id.rightButton);
        rightButton.setVisibility(View.GONE);
        pagerIndicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
        pagerIndicator.setVisibility(View.VISIBLE);

        RxView.clicks(rightButton).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                endOnBoard();
            }
        });
        initPager();
    }

    private void endOnBoard(){
        super.finish();
    }

    private void initPager(){
        FragmentPagerItems f = new FragmentPagerItems(this);

        for(int i=0;i<resource.length;i++){
            f.add(i,FragmentPagerItem.of(String.valueOf(i),OnBoardPagerFragment.class));
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), f);

        onBoardPager.setAdapter(adapter);

        onBoardPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==3){
                    rightButton.setVisibility(View.VISIBLE);
                    pagerIndicator.setVisibility(View.GONE);
                }else{
                    rightButton.setVisibility(View.GONE);
                    pagerIndicator.setVisibility(View.VISIBLE);
                }

                ((OnBoardPagerFragment)((FragmentPagerItemAdapter)onBoardPager.getAdapter()).getPage(position)).setContent(resource[position],title[position],description[position],position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



}
