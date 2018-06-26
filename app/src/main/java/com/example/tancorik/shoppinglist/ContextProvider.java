package com.example.tancorik.shoppinglist;

import android.annotation.SuppressLint;
import android.content.Context;

public class ContextProvider {

    private Context mContext;

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static final ContextProvider HOLDER_INSTANCE = new ContextProvider();
    }

    public static ContextProvider getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public void setContext(Context context) {
        if (mContext != null) {
            throw new IllegalStateException("контекст уже задан");
        }
        else {
            mContext = context;
        }
    }

    public Context getContext() {
        if (mContext == null) {
            throw new IllegalStateException("контекст не задан");
        }
        return mContext;
    }
}
