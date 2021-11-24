package com.boardinglabs.mireta.standalone.modul.old.onboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boardinglabs.mireta.standalone.R;

public class OnBoardPagerFragment extends Fragment {
    ImageView image;
    TextView title;
    TextView description;

    public OnBoardPagerFragment() {
        // Required empty public constructor
    }

    public static OnBoardPagerFragment newInstance() {
        OnBoardPagerFragment fragment = new OnBoardPagerFragment();
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
        View v = inflater.inflate(R.layout.fragment_on_board_pager, container, false);
        image = (ImageView) v.findViewById(R.id.image);
        title = (TextView) v.findViewById(R.id.title);
        description = (TextView) v.findViewById(R.id.description);
        return v;
    }

    public void setContent(int resourceDrawableId, String title, String description,int position){
        this.image.setImageResource(resourceDrawableId);
        this.title.setText(title);
        this.description.setText(description);
        if(position==2){
            this.description.setText(Html.fromHtml("Cukup <b>SCAN QR</b> transaksi lebih praktis."));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
