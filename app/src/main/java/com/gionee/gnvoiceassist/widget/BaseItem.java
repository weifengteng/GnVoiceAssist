package com.gionee.gnvoiceassist.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseItem {
    public abstract View getView(LayoutInflater inflater, ViewGroup parent);
    public abstract void bindView();
    public abstract void onClick();
    public abstract void setItemBgType(int type);
}