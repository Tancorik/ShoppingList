package com.example.tancorik.shoppinglist.Data;

public class SubjectForShopping {

    private int mCount;
    private int mId;

    public SubjectForShopping() {

    }

    public SubjectForShopping(int id, int count) {
        mId = id;
        mCount = count;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public int getCount() {
        return mCount;
    }

    @Override
    public String toString() {
        return String.format("%s %3d шт.", super.toString(), mCount);
    }
}
