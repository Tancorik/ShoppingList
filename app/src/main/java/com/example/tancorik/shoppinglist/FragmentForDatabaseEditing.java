package com.example.tancorik.shoppinglist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.tancorik.shoppinglist.Data.Subject;

public class FragmentForDatabaseEditing extends Fragment implements FragmentForDatabaseEditingPresenter.IPresenterListener {

    public static final String TAG = "fragmentForDatabaseEditing";

    private Switch mSwitch;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private FragmentForDatabaseEditingPresenter mPresenter;
    private FragmentForDatabaseEditingCallback mCallback;
    private int mCategory;
    private Subject mSubject = null;


    public static FragmentForDatabaseEditing newInstance() {
        return new FragmentForDatabaseEditing();
    }

    public void setCallback(FragmentForDatabaseEditingCallback callback) {
        mCallback = callback;
    }

    public void setCategory(int category) {
        mCategory = category;
    }

    public void setSubject(Subject subject) {
        mSubject = subject;
        mNameEditText.setText(mSubject.getName());
        mPriceEditText.setText(String.valueOf(mSubject.getPrice()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_for_database_editing, container, false);

        Button addButton = view.findViewById(R.id.add_subject_button);
        addButton.setOnClickListener(new ButtonListener());

        Button deleteButton = view.findViewById(R.id.delete_subject_button);
        deleteButton.setOnClickListener(new ButtonListener());

        Button updateButton = view.findViewById(R.id.update_subject_button);
        updateButton.setOnClickListener(new ButtonListener());

        mNameEditText = view.findViewById(R.id.name_subject_edit_text);
        mNameEditText.setOnClickListener(new ButtonListener());
        mNameEditText.addTextChangedListener(new ChangeTextListener());
        mPriceEditText = view.findViewById(R.id.price_subject_edit_text);

        mSwitch = view.findViewById(R.id.subject_category_switch);
        mSwitch.setChecked(true);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mPriceEditText.setVisibility(View.INVISIBLE);
                }
                else {
                    mPriceEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        mPresenter = new FragmentForDatabaseEditingPresenter(getContext(), this);

        return view;
    }

    @Override
    public void onStop() {
        mPresenter.closePresenter();
        super.onStop();
    }

    @Override
    public void onUpdateDatabase(long result) {
        if (result < 0) {
            Toast.makeText(getContext(), "Не получилось внести изменения", Toast.LENGTH_SHORT).show();
        }
        else {
            mCallback.onUpdateDatabase();
            mNameEditText.setText("");
            mPriceEditText.setText("");
        }
    }

    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_subject_button :
                    mPresenter.add(mSwitch.isChecked(), mNameEditText.getText().toString(),
                            mPriceEditText.getText().toString(), mCategory);
                    break;
                case R.id.delete_subject_button :
                    mPresenter.delete(mSwitch.isChecked(), mCategory, mSubject);
                    break;
                case R.id.update_subject_button :
                    int idSubject;
                    if (mSubject != null) {
                        idSubject = mSubject.getId();
                    }
                    else {
                        idSubject = -1;
                    }
                    mPresenter.update(mSwitch.isChecked(), mNameEditText.getText().toString(),
                            mPriceEditText.getText().toString(), mCategory, idSubject);
                    break;
                case R.id.name_subject_edit_text :
                    mNameEditText.setText("");
                    break;
            }
            mSubject = null;
        }
    }

    class ChangeTextListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(final CharSequence s, int start, int before, int count) {
//            if (s.length() > 0) {
//                mCallback.updateWithFilter(s.toString());
//            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    interface FragmentForDatabaseEditingCallback {
        void onUpdateDatabase();
        void updateWithFilter(String string);
    }
}
