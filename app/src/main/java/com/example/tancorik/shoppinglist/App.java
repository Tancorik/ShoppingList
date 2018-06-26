package com.example.tancorik.shoppinglist;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ContextProvider contextProvider = ContextProvider.getInstance();
        contextProvider.setContext(this);
    }
}
