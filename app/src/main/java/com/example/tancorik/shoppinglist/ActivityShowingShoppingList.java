package com.example.tancorik.shoppinglist;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tancorik.shoppinglist.Data.Subject;

import java.util.List;

import static com.example.tancorik.shoppinglist.MainActivity.NAME_SHOPPING_LIST_KEY;

public class ActivityShowingShoppingList extends AppCompatActivity implements
        FragmentShowingDatabase.FragmentShowingDatabaseListener,
        FragmentShowingShoppingList.FragmentShowingShoppingListCallback,
        ActivityShowingShoppingListPresenter.ActivityShowingShoppingListPresenterListener {

    private FragmentShowingDatabase mDatabaseFragment;
    private FragmentShowingShoppingList mFragmentBefore;
    private FragmentShowingShoppingList mFragmentAfter;
    private String mFragmentBeforeName;
    private String mFragmentAfterName;
    private ActivityShowingShoppingListPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_shopping_list);

        Intent intent = getIntent();
        String name = intent.getStringExtra(NAME_SHOPPING_LIST_KEY);

        mFragmentBeforeName = name;
        mFragmentAfterName = name + "(куплено)";

        mDatabaseFragment = FragmentShowingDatabase.newInstance();
        mDatabaseFragment.setCallback(this);
        mFragmentBefore = FragmentShowingShoppingList.newInstance(mFragmentBeforeName);
        mFragmentBefore.setCallback(this);
        mFragmentAfter = FragmentShowingShoppingList.newInstance(mFragmentAfterName);
        mFragmentAfter.setCallback(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.first_fragment_holder_showing_list, mFragmentBefore, FragmentShowingShoppingList.TAG)
                .replace(R.id.second_fragment_holder_showing_list, mFragmentAfter, FragmentShowingShoppingList.TAG)
                .commit();
        mPresenter = new ActivityShowingShoppingListPresenter(this);
        mPresenter.setFragmentNames(mFragmentBeforeName, mFragmentAfterName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Откатить все покупки");
        menu.add(0, 2, 1, "Удалить список");
        menu.add(0, 3, 2, "Редактировать");
        menu.add(0, 4, 3, "Редактор товаров");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1 :
                mPresenter.rollBack();
                break;
            case 2 :
                delete();
                break;
            case 3 :
                if (!mDatabaseFragment.isVisible()) {
                    mPresenter.onHideAfterFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.second_fragment_holder_showing_list, mDatabaseFragment, FragmentShowingDatabase.TAG)
                            .addToBackStack("")
                            .commit();

                }
                break;
            case 4 :
                Intent intent = new Intent(this, ActivityForDatabaseEditing.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStartActivity();
        if (mDatabaseFragment.isVisible()) {
            mDatabaseFragment.updateContent();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPauseActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCategoryChoose(int category) {

    }

    @Override
    public void onSubjectChoose(Subject subject) {
        mPresenter.addNewSubject(subject.getId());
    }



    private void delete() {
        mPresenter.deleteShoppingList(mFragmentBeforeName);
    }

    @Override
    public void onDeleteShopList() {
        finish();
    }

    @Override
    public void onLoadShopList(String nameFragment, List<String> list) {
        if (nameFragment.equalsIgnoreCase(mFragmentBeforeName)) {
            mFragmentBefore.updateRecycler(list);
        }
        else if (nameFragment.equalsIgnoreCase(mFragmentAfterName)) {
            mFragmentAfter.updateRecycler(list);
        }
        else {
            throw new IllegalArgumentException("не правильно указано имя фрагмента");
        }
    }


    @Override
    public void onItemClick(String fragmentName, int position) {
        mPresenter.onItemClick(fragmentName, position, mFragmentAfter.isVisible());
    }

    @Override
    public void onStartFragment(String fragmentName) {
        mPresenter.onVisibleAfterFragment();
    }
}
