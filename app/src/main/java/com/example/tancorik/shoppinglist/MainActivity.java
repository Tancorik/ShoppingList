package com.example.tancorik.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;


public class MainActivity extends AppCompatActivity implements StringsRecyclerAdapter.IStringRecyclerAdapterCallback,
        MainActivityPresenter.IMainActivityPresenterCallback {

    public static final String NAME_SHOPPING_LIST_KEY = "shopping_list";

    private StringsRecyclerAdapter mAdapter;
    private boolean mCreateListFlag;
    private EditText mListNameEditText;
    private MainActivityPresenter mMainActivityPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createNewList = findViewById(R.id.add_shopping_list_button);
        createNewList.setOnClickListener(new ClickListener());

        mListNameEditText = findViewById(R.id.name_shopping_list_edit_text);
        mListNameEditText.setVisibility(View.INVISIBLE);
        mListNameEditText.setOnClickListener(new ClickListener());

        RecyclerView recyclerView = findViewById(R.id.all_shopping_list_recycler);
        mAdapter = new StringsRecyclerAdapter(this);
        recyclerView.setAdapter(mAdapter);

        mCreateListFlag = false;
        mMainActivityPresenter = new MainActivityPresenter(this, this);
//        mMainActivityPresenter.loadShoppingList();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mMainActivityPresenter.loadShoppingList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "Редактор товаров");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Intent intent = new Intent(this, ActivityForDatabaseEditing.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
//        mMainActivityPresenter.stopPresenter();
        super.onStop();
    }

    @Override
    public void onRecyclerItemClick(int position) {
        Intent intent = new Intent(this, ActivityShowingShoppingList.class);
        intent.putExtra(NAME_SHOPPING_LIST_KEY, mMainActivityPresenter.getShoppingListName(position));
        startActivity(intent);
    }

    @Override
    public void onLoadShoppingList(List<String> fileList) {
        mAdapter.setList(fileList);
        mAdapter.notifyDataSetChanged();
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_shopping_list_button :
                    if (!mCreateListFlag) {
                        mListNameEditText.setVisibility(View.VISIBLE);
                        mCreateListFlag = true;
                    }
                    else {
                        if (!mListNameEditText.getText().toString().isEmpty()) {
                            mMainActivityPresenter.createNewShoppingList(mListNameEditText.getText().toString());
                        }
                        mListNameEditText.setText("");
                        mListNameEditText.setVisibility(View.INVISIBLE);
                        mCreateListFlag = false;
                    }
                    break;
                case R.id.name_shopping_list_edit_text :
                    EditText editText = findViewById(R.id.name_shopping_list_edit_text);
                    editText.setText("");
                    editText.setVisibility(View.INVISIBLE);
                    mCreateListFlag = false;
                    break;
            }
        }
    }

}
