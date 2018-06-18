package com.example.tancorik.shoppinglist;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.tancorik.shoppinglist.Data.SQLData.DatabaseAdapter;
import com.example.tancorik.shoppinglist.Data.Subject;
import com.example.tancorik.shoppinglist.Data.SubjectCategory;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity_LOG_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Intent intent = new Intent(this, ActivityForDatabaseEditing.class);
            startActivity(intent);
        }

//        DatabaseAdapter databaseAdapter = new DatabaseAdapter(getApplicationContext());
//        databaseAdapter.openDatabase();
//
////        databaseAdapter.insertCategory("Лекарства");
//
//        List<SubjectCategory> categoryList = databaseAdapter.getCategoryList();
//        for (int i=0 ; i < categoryList.size(); i++) {
//            Log.i(LOG_TAG, categoryList.get(i).getName());
//        }
//
////        databaseAdapter.insertSubject(new Subject(1, "Витаминки", 160, 3));
//        databaseAdapter.updateSubject(new Subject(1, "Батон", 23, 1));
//
//        List<Subject> subjectList = databaseAdapter.getSubjectList(null, 0);
//        for (int i=0; i < subjectList.size(); i++) {
//            Log.i(LOG_TAG, subjectList.get(i).toString());
//        }
//
//        Log.i(LOG_TAG, databaseAdapter.getSubject(1).toString());
//
//        databaseAdapter.closeDatabase();


//        Spinner spinner = findViewById(R.id.spinner_view);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.text_for_spinner);
//        spinner.setAdapter(adapter);


    }
}
