package com.example.tancorik.shoppinglist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class StringsRecyclerAdapter extends RecyclerView.Adapter<StringsRecyclerAdapter.ViewHolder> {

    private List<String> mStringList = new ArrayList<>();
    private IStringRecyclerAdapterCallback mCallback;

    class ViewHolder extends RecyclerView.ViewHolder {
        Button mButton;
        ViewHolder(View itemView) {
            super(itemView);
            mButton = itemView.findViewById(R.id.item_button);
        }
    }

    StringsRecyclerAdapter(IStringRecyclerAdapterCallback callback) {
        mCallback = callback;
    }

    public void setList(List<String> newList) {
        mStringList = newList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mButton.setText(mStringList.get(position));
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRecyclerItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStringList.size();
    }

    interface IStringRecyclerAdapterCallback {
        void onRecyclerItemClick(int position);
    }
}
