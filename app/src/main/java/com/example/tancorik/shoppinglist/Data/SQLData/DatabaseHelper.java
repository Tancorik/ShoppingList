package com.example.tancorik.shoppinglist.Data.SQLData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CATEGORY = "category";

    public static final String COLUMN_CATEGORY_NAME = "categoryName";
    public static final String COLUMN_CATEGORY_ID = "_id";

    public static final String SUBJECT_TABLE = "products";
    public static final String CATEGORY_TABLE   = "category";


    private static final String DATABASE_NAME = "goods.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table " + CATEGORY_TABLE + "("
                    + COLUMN_CATEGORY_ID + " integer primary key autoincrement, "
                    + COLUMN_CATEGORY_NAME + " text);");

            sqLiteDatabase.execSQL("insert into " + CATEGORY_TABLE + "(" + COLUMN_CATEGORY_NAME
                    + ") values ('продукты');");

            sqLiteDatabase.execSQL("create table " + SUBJECT_TABLE + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_NAME + " text, "
                    + COLUMN_PRICE + " integer, " + COLUMN_CATEGORY + " integer, "
                    + "foreign key (" + COLUMN_CATEGORY + ") references "
                    + CATEGORY_TABLE + "(" + COLUMN_CATEGORY_ID + ") );");

            sqLiteDatabase.execSQL("insert into " + SUBJECT_TABLE + " values (1, 'хлебушек', 32, 1);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + SUBJECT_TABLE);
        sqLiteDatabase.execSQL("drop table if exists " + CATEGORY_TABLE);
        onCreate(sqLiteDatabase);
    }
}
