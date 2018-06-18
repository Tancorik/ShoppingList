package com.example.tancorik.shoppinglist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tancorik.shoppinglist.Data.Subject;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private List<Subject> mSubjectList = new ArrayList<>();
    private IMainRecyclerCallback mMainRecyclerCallback;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Button mButton;
        public ViewHolder(View itemView) {
            super(itemView);
            mButton = itemView.findViewById(R.id.item_button);
        }
    }

    public MainRecyclerAdapter(IMainRecyclerCallback mainRecyclerCallback) {
        mMainRecyclerCallback = mainRecyclerCallback;
    }

    public void updateList(List<Subject> subjects) {
        mSubjectList = subjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mButton.setText(mSubjectList.get(position).toString());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainRecyclerCallback.onSubjectClick(mSubjectList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubjectList.size();
    }

    interface IMainRecyclerCallback {
        void onSubjectClick(Subject subject);
    }
}
