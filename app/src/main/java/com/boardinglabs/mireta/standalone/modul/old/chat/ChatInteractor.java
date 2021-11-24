package com.boardinglabs.mireta.standalone.modul.old.chat;

import android.content.Context;

import com.boardinglabs.mireta.standalone.component.network.gson.GLogo;

import java.util.List;

import rx.Observable;

/**
 * Created by Dhimas on 1/3/18.
 */

public interface ChatInteractor {
    Observable<List<GLogo>> uploadImage(Context context, String source);
}
