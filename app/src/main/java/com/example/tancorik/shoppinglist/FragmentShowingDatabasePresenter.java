package com.example.tancorik.shoppinglist;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.example.tancorik.shoppinglist.Data.SQLData.DatabaseManager;
import com.example.tancorik.shoppinglist.Data.Subject;
import com.example.tancorik.shoppinglist.Data.SubjectCategory;

import java.util.ArrayList;
import java.util.List;

public class FragmentShowingDatabasePresenter {

    private DatabaseManager mDatabaseManager;
    private HandlerThread mHandlerThread;
    private Looper mLooper;
    private IPresenterListener mPresenterListener;
    private List<SubjectCategory> mCategoryList;

    FragmentShowingDatabasePresenter(Context context, IPresenterListener presenterListener) {
        mDatabaseManager = new DatabaseManager();
        mPresenterListener = presenterListener;
        mHandlerThread = new HandlerThread("databasePresenter");
        mHandlerThread.start();
        mLooper = mHandlerThread.getLooper();
    }

    public void closePresenter() {
        mHandlerThread.quit();
    }

    public int getCategory(int position) {
        return mCategoryList.get(position).getId();
    }

    public void loadCategoryList() {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                mDatabaseManager.openDatabase();
                mCategoryList = mDatabaseManager.getCategoryList();
                final List<String> categoryNames = getCategoryStringList();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mPresenterListener.onLoadCategory(categoryNames);
                    }
                });
                mDatabaseManager.closeDatabase();
            }
        });

    }

    public void loadSubjectList(final String string, final int categoryPosition) {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                mDatabaseManager.openDatabase();
                final List<Subject> subjectList = mDatabaseManager.getSubjectList(string, mCategoryList.get(categoryPosition).getId());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mPresenterListener.onLoadSubjects(subjectList);
                    }
                });
                mDatabaseManager.closeDatabase();
            }
        });
    }

    private List<String> getCategoryStringList() {
        List<String> strings = new ArrayList<>();
        for (SubjectCategory sc: mCategoryList) {
            strings.add(sc.getName());
        }
        return strings;
    }

    interface IPresenterListener {
        void onLoadSubjects(List<Subject> subjectList);
        void onLoadCategory(List<String> categoryList);
    }

}
