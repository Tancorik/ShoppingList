package com.example.tancorik.shoppinglist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tancorik.shoppinglist.Data.Subject;

import java.util.ArrayList;
import java.util.List;


public class FragmentShowingDatabase extends Fragment implements FragmentShowingDatabasePresenter.IPresenterListener,
        EditRecyclerAdapter.IMainRecyclerCallback {

    public static final String TAG = "fragmentShowingDatabase";

    private EditRecyclerAdapter mRecyclerAdapter;
    private ArrayAdapter<String> mSpinnerAdapter;
    private List<String> mCategoryList = new ArrayList<>();
    private FragmentShowingDatabasePresenter mPresenter;
    private FragmentShowingDatabaseListener mCallback;
    private int mPositionSpinner;

    public static FragmentShowingDatabase newInstance() {
        return new FragmentShowingDatabase();
    }

    public void setCallback(FragmentShowingDatabaseListener callback) {
        mCallback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showing_database, container, false);

        mPositionSpinner = 0;

        RecyclerView recyclerView = view.findViewById(R.id.recycler_for_showing_fragment);
        mRecyclerAdapter = new EditRecyclerAdapter(this);
        recyclerView.setAdapter(mRecyclerAdapter);

        Spinner spinner = view.findViewById(R.id.category_spinner);
        mSpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, R.id.text_for_spinner, mCategoryList);
        spinner.setAdapter(mSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.loadSubjectList(null, position);
                mCallback.onCategoryChange(mPresenter.getCategory(position));
                mPositionSpinner = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPresenter = new FragmentShowingDatabasePresenter(getContext(), this);
        mPresenter.loadCategoryList();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStop() {
        mPresenter.closePresenter();
        super.onStop();
    }

    @Override
    public void onLoadSubjects(List<Subject> subjectList) {
        mRecyclerAdapter.updateList(subjectList);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadCategory(List<String> categoryList) {
        mCategoryList.clear();
        mCategoryList.addAll(categoryList);
        mSpinnerAdapter.notifyDataSetChanged();
        if (mPositionSpinner < categoryList.size()) {
            mCallback.onCategoryChange(mPresenter.getCategory(mPositionSpinner));
            mPresenter.loadSubjectList(null, mPositionSpinner);
        }
    }

    @Override
    public void onSubjectClick(Subject subject) {
        mCallback.onSubjectChange(subject);
    }

    public void updateContent() {
        mPresenter.loadCategoryList();
    }

    public void loadSubjectListWithFilter(String string) {
        mPresenter.loadSubjectList(string, mPositionSpinner);
    }

    interface FragmentShowingDatabaseListener {
        void onCategoryChange(int category);
        void onSubjectChange(Subject subject);
    }
    
}
