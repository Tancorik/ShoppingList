package com.example.tancorik.shoppinglist;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.example.tancorik.shoppinglist.Data.SQLData.DatabaseAdapter;
import com.example.tancorik.shoppinglist.Data.Subject;
import com.example.tancorik.shoppinglist.Data.SubjectCategory;

import java.util.ArrayList;
import java.util.List;

public class FragmentShowingDatabasePresenter {

    private DatabaseAdapter mDatabaseAdapter;
    private HandlerThread mHandlerThread;
    private Looper mLooper;
    private IPresenterListener mPresenterListener;
    private List<SubjectCategory> mCategoryList;

    FragmentShowingDatabasePresenter(Context context, IPresenterListener presenterListener) {
        mDatabaseAdapter = new DatabaseAdapter(context);
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
                mDatabaseAdapter.openDatabase();
                mCategoryList = mDatabaseAdapter.getCategoryList();
                final List<String> categoryNames = getCategoryStringList();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mPresenterListener.onLoadCategory(categoryNames);
                    }
                });
                mDatabaseAdapter.closeDatabase();
            }
        });

    }

    public void loadSubjectList(final String string, final int categoryPosition) {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                mDatabaseAdapter.openDatabase();
                final List<Subject> subjectList = mDatabaseAdapter.getSubjectList(string, mCategoryList.get(categoryPosition).getId());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mPresenterListener.onLoadSubjects(subjectList);
                    }
                });
                mDatabaseAdapter.closeDatabase();
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
