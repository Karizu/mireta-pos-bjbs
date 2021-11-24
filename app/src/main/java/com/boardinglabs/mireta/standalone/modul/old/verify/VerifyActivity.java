package com.boardinglabs.mireta.standalone.modul.old.verify;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.boardinglabs.mireta.standalone.BaseActivity;
import com.boardinglabs.mireta.standalone.R;
import com.boardinglabs.mireta.standalone.modul.old.dialogactivity.DialogAttachPicture;

import rx.functions.Action1;

/**
 * Created by Dhimas on 4/2/18.
 */

public class VerifyActivity extends BaseActivity {
    private ImageView ktp;
    private ImageView selfie;
    final int PIC_CROP = 1;
    private int height;
    private int width;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.verification_layout;
    }

    @Override
    protected void setContentViewOnChild() {
        setToolbarTitle("Verifikasi ID");
        ktp = (ImageView) findViewById(R.id.ktp);
        selfie = (ImageView) findViewById(R.id.selfie);

        RxView.clicks(ktp).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivityForResult(new Intent(VerifyActivity.this, DialogAttachPicture.class), 211);
            }
        });

        RxView.clicks(selfie).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivityForResult(new Intent(VerifyActivity.this, DialogAttachPicture.class), 212);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

    @Override
    protected void onCreateAtChild() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {

            String picPath = data.getExtras().getString(DialogAttachPicture.RESULT_LOAD_IMAGE);
            Bitmap myBitmap = BitmapFactory.decodeFile(picPath);
//            Bitmap resizedbitmap1=Bitmap.createBitmap(myBitmap, 0,(height/2) - 500,width - 10, 500);
            if (requestCode == 211) {
                ktp.setImageBitmap(myBitmap);
            }

            if (requestCode == 212) {
                selfie.setImageBitmap(myBitmap);
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

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
