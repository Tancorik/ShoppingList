package com.example.tancorik.shoppinglist;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tancorik.shoppinglist.Data.Subject;

public class ActivityForDatabaseEditing extends AppCompatActivity implements
        FragmentForDatabaseEditing.FragmentForDatabaseEditingCallback,
        FragmentShowingDatabase.FragmentShowingDatabaseListener {


    private FragmentForDatabaseEditing mEditFragment;
    private FragmentShowingDatabase mShowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_database_editing);


        FragmentManager fragmentManager = getSupportFragmentManager();

        mEditFragment = FragmentForDatabaseEditing.newInstance();
        mEditFragment.setCallback(this);

        mShowFragment = FragmentShowingDatabase.newInstance();
        mShowFragment.setCallback(this);

        fragmentManager.beginTransaction()
                .replace(R.id.first_fragment_holder_for_database_editing, mEditFragment,
                        FragmentForDatabaseEditing.TAG)
                .replace(R.id.second_fragment_holder_for_database_editing, mShowFragment,
                        FragmentShowingDatabase.TAG)

                .commit();
    }

    @Override
    public void onUpdateDatabase() {
        mShowFragment.updateContent();
    }

    @Override
    public void updateWithFilter(String string) {
        mShowFragment.loadSubjectListWithFilter(string);
    }

    @Override
    public void onCategoryChoose(int categoryId) {
        mEditFragment.setCategory(categoryId);
    }

    @Override
    public void onSubjectChoose(Subject subject) {
        mEditFragment.setSubject(subject);
    }
}
