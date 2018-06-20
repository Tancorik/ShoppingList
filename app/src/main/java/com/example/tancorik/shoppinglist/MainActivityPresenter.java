package com.example.tancorik.shoppinglist;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.example.tancorik.shoppinglist.Data.FileData.FileManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPresenter {

    public static final String NAME_LIST_FILE = "listOfList";
    private FileManager mFileManager;
    private HandlerThread mHandlerThread;
    private Looper mLooper;
    private IMainActivityPresenterCallback mCallback;

    MainActivityPresenter(Context context, IMainActivityPresenterCallback callback) {
        mFileManager = new FileManager(context);
        mCallback = callback;
        mHandlerThread = new HandlerThread("mainActivityPresenterThread");

    }

    public void startPresenter() {
        if (!mHandlerThread.isAlive()) {
            mHandlerThread.start();
            mLooper = mHandlerThread.getLooper();
        }
    }

    public void stopPresenter() {
        mHandlerThread.quit();
    }

    public void createNewShoppingList(final String name) {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                String content = loadListFile() + name + ",";
                mFileManager.doWork(NAME_LIST_FILE, content, FileManager.FILE_MANAGER_ACTION_SAVE);
            }
        });
        loadShoppingList();
    }

    public void loadShoppingList() {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                final List<String> list = createList(loadListFile());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onLoadShoppingList(list);
                    }
                });
            }
        });
    }

    private String loadListFile() {
        return mFileManager.doWork(NAME_LIST_FILE, null, FileManager.FILE_MANAGER_ACTION_LOAD);
    }

    private List<String> createList(String string) {
        List<String> list = new ArrayList<>();
        while (!string.isEmpty()) {
            int position = string.indexOf(",");
            if (position<0) break;
            String substring = string.substring(0, position);
            list.add(substring);
            string = string.replaceFirst(substring + ",", "");
        }
        return list;
    }

    interface IMainActivityPresenterCallback {
        void onLoadShoppingList(List<String> fileList);
    }
}
