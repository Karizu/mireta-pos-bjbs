package com.boardinglabs.mireta.standalone.component.listener;

public interface ItemActionListener {
    void itemClicked(int position);
    void itemDeleted(int position);
    void itemAdd(int position);
    void itemMinus(int position);
}
