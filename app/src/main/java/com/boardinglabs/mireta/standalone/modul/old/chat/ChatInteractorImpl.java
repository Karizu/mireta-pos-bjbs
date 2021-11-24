package com.boardinglabs.mireta.standalone.modul.old.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.boardinglabs.mireta.standalone.component.network.NetworkService;
import com.boardinglabs.mireta.standalone.component.network.gson.GLogo;

import java.io.File;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dhimas on 1/3/18.
 */

public class ChatInteractorImpl implements ChatInteractor {
    private NetworkService mService;

    public ChatInteractorImpl(NetworkService service) {
        mService = service;
    }

    @Override
    public Observable<List<GLogo>> uploadImage(Context context, String source) {
        File file = null;
        try {
            file = new Compressor.Builder(context)
                    .setMaxWidth(1024)
                    .setMaxHeight(1024)
                    .setQuality(65)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .build()
                    .compressToFile(new File(source));
        } catch (NullPointerException | IllegalArgumentException e) {
            file = new File(source);
        }

        if (file == null) return Observable.just(null);

        RequestBody requestFile = RequestBody.create(MediaType.parse(getFormat(file.getName())), file);
        MultipartBody.Part fbody = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
        RequestBody agents = RequestBody.create(MediaType.parse("text/plain"), "agents");
        RequestBody chat = RequestBody.create(MediaType.parse("text/plain"), "chat");

        return mService.uploadChat(agents, chat, fbody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    private String getFormat(String imageName) {

        String temp = new String(imageName);
        temp.toLowerCase();

        if(temp.endsWith(".png"))
            return "image/png";

        if(temp.endsWith(".gif"))
            return "image/gif";

        if(temp.endsWith(".jpg"))
            return "image/jpg";

        if(temp.endsWith(".jpeg"))
            return "image/jpeg";

        if(temp.endsWith(".tiff"))
            return "image/tiff";

        return "UNKNOWN";
    }
}
