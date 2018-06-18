package com.example.tancorik.shoppinglist;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.example.tancorik.shoppinglist.Data.SQLData.DatabaseAdapter;
import com.example.tancorik.shoppinglist.Data.Subject;
import com.example.tancorik.shoppinglist.Data.SubjectCategory;

public class FragmentForDatabaseEditingPresenter {

    private HandlerThread mHandlerThread;
    private Looper mLooper;
    private DatabaseAdapter mDatabaseAdapter;
    private IPresenterListener mPresenterListener;

    public FragmentForDatabaseEditingPresenter(Context context, IPresenterListener presenterListener) {
        mDatabaseAdapter = new DatabaseAdapter(context);
        mPresenterListener = presenterListener;
        mHandlerThread = new HandlerThread("editingPresenter");
        mHandlerThread.start();
        mLooper = mHandlerThread.getLooper();
    }

    public void closePresenter() {
        mHandlerThread.quit();
    }

    public void add(final boolean switchPosition, @Nullable final String name, @Nullable final String price, final int category) {

        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                if (!name.isEmpty()) {
                    mDatabaseAdapter.openDatabase();
                    final long result;
                    if (!switchPosition) {
                        result = mDatabaseAdapter.insertCategory(name);
                    } else {
                        if (!price.isEmpty()) {
                            result = mDatabaseAdapter.insertSubject(new Subject(0, name, Integer.parseInt(price), category));
                        }
                        else result = -1;
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mPresenterListener.onUpdateDatabase(result);
                        }
                    });
                    mDatabaseAdapter.closeDatabase();
                }
            }
        });
        mDatabaseAdapter.closeDatabase();
    }

    public void delete(final boolean switchPosition, final int id, @Nullable final Subject subject) {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                mDatabaseAdapter.openDatabase();
                final long result;
                if (!switchPosition){
                    result = mDatabaseAdapter.deleteCategory(id);
                }
                else if (subject != null) {
                    result = mDatabaseAdapter.deleteSubject(subject.getId());
                }
                else {
                    result = -1;
                }
                mDatabaseAdapter.closeDatabase();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mPresenterListener.onUpdateDatabase(result);
                    }
                });

            }
        });
    }

    public void update(final boolean switchPosition, final String name, final String price, final int idCategory, final int idSubject) {
        if (!name.isEmpty()) {
            new Handler(mLooper).post(new Runnable() {
                @Override
                public void run() {
                    mDatabaseAdapter.openDatabase();
                    final long result;
                    if (!switchPosition) {
                        result = mDatabaseAdapter.updateCategory(new SubjectCategory(idCategory, name));
                    } else if (!price.isEmpty() && idSubject >=0 ) {
                        result = mDatabaseAdapter.updateSubject(new Subject(idSubject, name, Integer.parseInt(price), idCategory));
                    }
                    else{
                        result = -1;
                    }
                    mDatabaseAdapter.closeDatabase();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mPresenterListener.onUpdateDatabase(result);
                        }
                    });
                }
            });
        }
    }


    interface IPresenterListener {
        void onUpdateDatabase(long result);
    }
}
