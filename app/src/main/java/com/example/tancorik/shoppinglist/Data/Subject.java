package com.example.tancorik.shoppinglist.Data;

public class Subject {

    private int mId;
    private String mName;
    private int mPrice;
    private int mCategory;

//    public Subject() {
//
//    }

    public Subject(int id, String name, int price, int category) {
        mId = id;
        mName = name;
        mPrice = price;
        mCategory = category;
    }

//    public Subject(Subject subject) {
//        this(subject.getId(), subject.getName(), subject.getPrice(), subject.getCategory());
//    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public void setCategory(int category) {
        mCategory = category;
    }

    public String getName() {
        return mName;
    }

    public int getPrice() {
        return mPrice;
    }

    public int getCategory() {
        return mCategory;
    }

    @Override
    public String toString() {
        return String.format("%s%10d", mName, mPrice);
    }
}
