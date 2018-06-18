package com.example.tancorik.shoppinglist.Data;

public class SubjectCategory {

    private int mId;
    private String mName;

//    public SubjectCategory() {
//
//    }

    public SubjectCategory(int id, String name) {
        mId = id;
        mName = name;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

}
