package com.example.tancorik.shoppinglist.Data.SQLData;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.example.tancorik.shoppinglist.ContextProvider;
import com.example.tancorik.shoppinglist.Data.Subject;
import com.example.tancorik.shoppinglist.Data.SubjectCategory;

import java.util.ArrayList;
import java.util.List;

import static com.example.tancorik.shoppinglist.Data.SQLData.DatabaseHelper.CATEGORY_TABLE;
import static com.example.tancorik.shoppinglist.Data.SQLData.DatabaseHelper.COLUMN_CATEGORY;
import static com.example.tancorik.shoppinglist.Data.SQLData.DatabaseHelper.COLUMN_CATEGORY_NAME;
import static com.example.tancorik.shoppinglist.Data.SQLData.DatabaseHelper.COLUMN_CATEGORY_ID;
import static com.example.tancorik.shoppinglist.Data.SQLData.DatabaseHelper.COLUMN_ID;
import static com.example.tancorik.shoppinglist.Data.SQLData.DatabaseHelper.COLUMN_NAME;
import static com.example.tancorik.shoppinglist.Data.SQLData.DatabaseHelper.COLUMN_PRICE;
import static com.example.tancorik.shoppinglist.Data.SQLData.DatabaseHelper.SUBJECT_TABLE;

public class DatabaseManager {

    private static final String LOG_TAG = "DatabaseAdapterLOG_TAG";
    private SQLiteDatabase mSQLiteDatabase;
    private DatabaseHelper mDatabaseHelper;

    public DatabaseManager() {
        ContextProvider contextProvider = ContextProvider.getInstance();
        mDatabaseHelper = new DatabaseHelper(contextProvider.getContext());
    }

    public void openDatabase() {
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mSQLiteDatabase.execSQL("pragma foreign_keys = on;");
    }

    public void closeDatabase() {
        mDatabaseHelper.close();
    }

    public Subject getSubject(int id) {
        Subject subject = null;
        String selection = COLUMN_ID + " = ?";
        String[] selectionArg = {String.valueOf(id)};
        Cursor cursor = mSQLiteDatabase.query(SUBJECT_TABLE, null, selection, selectionArg,
                null, null, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            int price = cursor.getInt(cursor.getColumnIndex(COLUMN_PRICE));
            int category = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY));
            subject = new Subject(id, name, price, category);
        }
        cursor.close();
        return subject;
    }

    public List<Subject> getSubjectList(@Nullable String forSelectionArg, int categoryId) {
        List<Subject> subjectList = new ArrayList<>();

        String selection;
        String[] selectionArg;

        if (forSelectionArg != null) {
            selection = COLUMN_NAME + " like ? and " + COLUMN_CATEGORY + " = ?";
            selectionArg = new String[]{"%" + forSelectionArg + "%", String.valueOf(categoryId)};
        }
        else{
            selection = COLUMN_CATEGORY + " = ?";
            selectionArg = new String[]{String.valueOf(categoryId)};
        }

        Cursor cursor = mSQLiteDatabase.query(SUBJECT_TABLE, null, selection,
                selectionArg, null, null, COLUMN_NAME);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                int price = cursor.getInt(cursor.getColumnIndex(COLUMN_PRICE));
                int category = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY));
                subjectList.add(new Subject(id, name, price, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return subjectList;
    }

    public List<SubjectCategory> getCategoryList() {
        List<SubjectCategory> categoryList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query(CATEGORY_TABLE, null, null,
                null, null, null, COLUMN_CATEGORY_ID);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                categoryList.add(new SubjectCategory(id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categoryList;
    }


    public long insertSubject(Subject subject) {
        ContentValues contentValues = getContentValues(subject);
        long result;
            result = mSQLiteDatabase.insert(SUBJECT_TABLE, null, contentValues);
        return  result; // eсли не получилось вставить вернет (-1)
    }

    public long insertCategory(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY_NAME, fitString(name));
        return mSQLiteDatabase.insert(CATEGORY_TABLE, null, contentValues);
    }

    public long updateSubject(Subject subject) {
        ContentValues contentValues = getContentValues(subject);
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(subject.getId())};
        return mSQLiteDatabase.update(SUBJECT_TABLE, contentValues, whereClause, whereArgs);
    }

    public long updateCategory(SubjectCategory subjectCategory) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY_NAME, subjectCategory.getName());
        String whereClause = COLUMN_CATEGORY_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(subjectCategory.getId())};
        return mSQLiteDatabase.update(CATEGORY_TABLE, contentValues, whereClause, whereArgs);
    }

    public long deleteSubject(int id) {
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        return mSQLiteDatabase.delete(SUBJECT_TABLE, whereClause, whereArgs);
    }


    public long deleteCategory(int id) {
        String whereClause = COLUMN_CATEGORY_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        long result;
        try {
            result = mSQLiteDatabase.delete(CATEGORY_TABLE, whereClause, whereArgs);
        } catch (SQLException e) {
            result = -1;
        }

        return result;
    }

    private ContentValues getContentValues(Subject subject) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, fitString(subject.getName()));
        contentValues.put(COLUMN_PRICE, subject.getPrice());
        contentValues.put(COLUMN_CATEGORY, subject.getCategory());
        return contentValues;
    }

    private String fitString(String string) {
        return string.toLowerCase().trim();
    }
}
