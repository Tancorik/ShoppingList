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
    private List<String> mShopListNames;

    MainActivityPresenter(Context context, IMainActivityPresenterCallback callback) {
        mFileManager = new FileManager();
        mCallback = callback;
        mHandlerThread = new HandlerThread("mainActivityPresenterThread");
        mHandlerThread.start();
        mLooper = mHandlerThread.getLooper();
    }

    public void stopPresenter() {
        mHandlerThread.quit();
    }

    public String getShoppingListName(int position) {
        return mShopListNames.get(position);
    }

    public void createNewShoppingList(final String name) {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                String content = loadListFile() + name + ",";
                mFileManager.save(NAME_LIST_FILE, content);
            }
        });
        loadShoppingList();
    }

    public void loadShoppingList() {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                mShopListNames = createList(loadListFile());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onLoadShoppingList(mShopListNames);
                    }
                });
            }
        });
    }

    private String loadListFile() {
        return mFileManager.load(NAME_LIST_FILE);
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

//    private String getUUID(String string) {
//        return UUID.nameUUIDFromBytes(string.getBytes()).toString();
//    }

    interface IMainActivityPresenterCallback {
        void onLoadShoppingList(List<String> fileList);
    }
}
