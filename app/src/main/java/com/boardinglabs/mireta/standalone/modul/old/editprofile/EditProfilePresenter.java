package com.boardinglabs.mireta.standalone.modul.old.editprofile;

/**
 * Created by Dhimas on 12/23/17.
 */

public interface EditProfilePresenter {
    void updateProfile(String name, String mobile, String email, String username, String lastName, String gender, String address, String birthdate, String avatarbase64);
}
