package com.example.tancorik.shoppinglist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FragmentShowingShoppingList extends Fragment implements StringsRecyclerAdapter.IStringRecyclerAdapterCallback {

    public static final String NAME_NEW_FRAGMENT = "new fragment name";
    public static final String TAG = "showingShopList";

    private String mName;
    private StringsRecyclerAdapter mAdapter;
    private FragmentShowingShoppingListCallback mCallback;


    public static FragmentShowingShoppingList newInstance(String name) {
        FragmentShowingShoppingList fragmentShowingShoppingList = new FragmentShowingShoppingList();
        Bundle argument = new Bundle();
        argument.putString(NAME_NEW_FRAGMENT, name);
        fragmentShowingShoppingList.setArguments(argument);
        return fragmentShowingShoppingList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        mName = getArguments().getString(NAME_NEW_FRAGMENT);
        View view = inflater.inflate(R.layout.fragment_for_shopping_list, container, false);
        TextView textView = view.findViewById(R.id.name_fragment);
        textView.setText(mName);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_for_shopping_list_fragment);
        mAdapter = new StringsRecyclerAdapter(this);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mCallback.onStartFragment(mName);
    }

    @Override
    public void onRecyclerItemClick(int position) {
        if (mCallback == null) {
            throw new IllegalArgumentException("callback не установлен");
        }
        else {
            mCallback.onItemClick(mName, position);
        }
    }

    public void setCallback(FragmentShowingShoppingListCallback callback) {
        mCallback = callback;
    }

    public void updateRecycler(List<String> list) {
        mAdapter.setList(list);
        mAdapter.notifyDataSetChanged();
    }

    interface FragmentShowingShoppingListCallback {
        void onItemClick(String fragmentName, int position);
        void onStartFragment(String fragmentName);
    }
}
