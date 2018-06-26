package com.example.tancorik.shoppinglist;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.WorkerThread;

import com.example.tancorik.shoppinglist.Data.FileData.FileManager;
import com.example.tancorik.shoppinglist.Data.FileData.MyXMLParser;
import com.example.tancorik.shoppinglist.Data.SQLData.DatabaseManager;
import com.example.tancorik.shoppinglist.Data.Subject;
import com.example.tancorik.shoppinglist.Data.SubjectForShopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.tancorik.shoppinglist.MainActivityPresenter.NAME_LIST_FILE;

public class ActivityShowingShoppingListPresenter {

    private static final String ENTRY_KEY = "entry";
    private static final String ID_KEY = "id";
    private static final String COUNT_KEY = "count";

    private ActivityShowingShoppingListPresenterListener mListener;
    private HandlerThread mHandlerThread;
    private Looper mLooper;
    private String mFragmentBeforeName;
    private String mFragmentAfterName;
    private List<SubjectForShopping> mBeforeShoppingList;
    private List<SubjectForShopping> mAfterShoppingList;
    private boolean mDeleteFlag;

    ActivityShowingShoppingListPresenter(ActivityShowingShoppingListPresenterListener listener) {
        mHandlerThread = new HandlerThread("ActivityShowingShoppingListPresenterThread");
        mHandlerThread.start();
        mLooper = mHandlerThread.getLooper();
        mListener = listener;
        mBeforeShoppingList = new ArrayList<>();
        mAfterShoppingList = new ArrayList<>();
        mDeleteFlag = false;
    }

    public void setFragmentNames(String fragmentBeforeName, String fragmentAfterName) {
        mFragmentBeforeName = fragmentBeforeName;
        mFragmentAfterName = fragmentAfterName;
    }

    public void onStartActivity() {
        loadList(mBeforeShoppingList, mFragmentBeforeName);
        loadList(mAfterShoppingList, mFragmentAfterName);
    }

    public void onPauseActivity() {
        if (!mDeleteFlag) {
            saveList(mBeforeShoppingList, mFragmentBeforeName);
            saveList(mAfterShoppingList, mFragmentAfterName);
        }
    }

    public void onVisibleAfterFragment() {
        loadList(mAfterShoppingList, mFragmentAfterName);
    }

    public void onHideAfterFragment() {
        saveList(mAfterShoppingList, mFragmentAfterName);
    }

    public void rollBack() {
        if (mAfterShoppingList.size() > 0) {
            for (SubjectForShopping subjectForShopping : mAfterShoppingList) {
                addElement(mBeforeShoppingList, subjectForShopping.getId(), mFragmentBeforeName);
            }
            mAfterShoppingList.clear();
            onPauseActivity();
            onStartActivity();
        }
    }

    public void deleteShoppingList(final String nameList) {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                mDeleteFlag = true;
                FileManager fileManager = new FileManager();
                fileManager.deleteFile(getUUID(mFragmentBeforeName));
                fileManager.deleteFile(getUUID(mFragmentAfterName));
                String string = fileManager.load(NAME_LIST_FILE);
                string = string.replace(mFragmentBeforeName + ",", "");
                fileManager.save(NAME_LIST_FILE, string);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onDeleteShopList();
                    }
                });
            }
        });
    }

    public void onItemClick(String fragmentName, int position, boolean fragmentAfterVisible) {
        if (!fragmentAfterVisible) {
            removeElement(mBeforeShoppingList, position, fragmentName);
        }
        else {
            if (fragmentName.equalsIgnoreCase(mFragmentBeforeName)) {
                addElement(mAfterShoppingList, getId(mBeforeShoppingList, position), mFragmentAfterName);
                removeElement(mBeforeShoppingList, position, mFragmentBeforeName);
            }
            else {
                addElement(mBeforeShoppingList, getId(mAfterShoppingList, position), mFragmentBeforeName);
                removeElement(mAfterShoppingList, position, mFragmentAfterName);
            }
        }
    }

    public void addNewSubject(int subjectId) {
        addElement(mBeforeShoppingList, subjectId, mFragmentBeforeName);
    }

    private int getId(List<SubjectForShopping> list, int position) {
        return list.get(position).getId();
    }

    private void addElement(final List<SubjectForShopping> list, final int subjectId, final String fragmentName) {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                if (list.size() == 0) {
                    list.add(new SubjectForShopping(subjectId, 1));
                }
                else {
                    boolean insert = false;
                    for (int i = 0; i< list.size(); i++) {
                        if (list.get(i).getId() == subjectId) {
                            int count = list.get(i).getCount();
                            list.set(i, new SubjectForShopping(subjectId, count +1 ));
                            insert = true;
                            break;
                        }
                    }
                    if (!insert) {
                        list.add(new SubjectForShopping(subjectId, 1));
                    }
                }
                final List<String> stringList = createStringList(list);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onLoadShopList(fragmentName, stringList); }
                    });
            }
        });
    }

    private void removeElement(final List<SubjectForShopping> list, final int position, final String fragmentName) {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                SubjectForShopping subjectForShopping = list.get(position);
                int count = subjectForShopping.getCount();
                if (count > 1) {
                    subjectForShopping.setCount(count - 1);
                    list.set(position, subjectForShopping);
                }
                else {
                    list.remove(position);
                }
                final List<String> stringList = createStringList(list);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onLoadShopList(fragmentName, stringList);
                    }
                });
            }
        });
    }

    private void loadList(final List<SubjectForShopping> list, final String fragmentName) {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                FileManager fileManager = new FileManager();
                String fileContent = fileManager.load(getUUID(fragmentName));
                if (!fileContent.isEmpty()) {
                    MyXMLParser myXMLParser = new MyXMLParser();
                    List<Map<String, String>> mapList = myXMLParser.parseString(fileContent, ENTRY_KEY, ID_KEY, COUNT_KEY);
                    list.clear();
                    list.addAll(createList(mapList));
                    final List<String> stringList = createStringList(list);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onLoadShopList(fragmentName, stringList);
                        }
                    });
                }
                else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onLoadShopList(fragmentName, new ArrayList<String>());
                        }
                    });
                }
            }
        });
    }

    private void saveList(final List<SubjectForShopping> list, final String name) {
        new Handler(mLooper).post(new Runnable() {
            @Override
            public void run() {
                MyXMLParser myXMLParser = new MyXMLParser();
                String string = myXMLParser.makeXMLString(createListMapForXML(list), ENTRY_KEY);
                FileManager fileManager = new FileManager();
                fileManager.save(getUUID(name), string);
            }
        });
    }


    @WorkerThread
    private List<SubjectForShopping> createList(List<Map<String, String>> mapList) {
        List<SubjectForShopping> list = new ArrayList<>();
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openDatabase();
        for (Map<String, String> map : mapList) {
            int id = Integer.parseInt(map.get(ID_KEY));
            int count = Integer.parseInt(map.get(COUNT_KEY));
            SubjectForShopping subjectForShopping = new SubjectForShopping(id, count);
            Subject subject = databaseManager.getSubject(id);
            if (subject != null) {
                list.add(subjectForShopping);
            }
        }
        databaseManager.closeDatabase();
        return list;
    }

    @WorkerThread
    private List<String> createStringList(List<SubjectForShopping> shoppingList) {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openDatabase();
        List<String> stringList = new ArrayList<>();
        for (SubjectForShopping subjectForShopping : shoppingList) {
            Subject subject = databaseManager.getSubject(subjectForShopping.getId());
            String string = createStringFromSubjectForShopping(subjectForShopping, subject);
            stringList.add(string);
        }

        databaseManager.closeDatabase();
        return stringList;
    }

    @WorkerThread
    private List<Map<String, String>> createListMapForXML(List<SubjectForShopping> shoppingList) {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (SubjectForShopping subjectForShopping : shoppingList) {
            Map<String, String> map = new HashMap<>();
            map.put(ID_KEY, String.valueOf(subjectForShopping.getId()));
            map.put(COUNT_KEY, String.valueOf(subjectForShopping.getCount()));
            mapList.add(map);
        }
        return mapList;
    }

    @WorkerThread
    @SuppressLint("DefaultLocale")
    private String createStringFromSubjectForShopping(SubjectForShopping subjectForShopping, Subject subject) {
        int sum = subject.getPrice() * subjectForShopping.getCount();
        return String.format("%s%4d шт %10d", subject.toString(), subjectForShopping.getCount(), sum);
    }

    private String getUUID(String string) {
        return UUID.nameUUIDFromBytes(string.getBytes()).toString();
    }

    interface ActivityShowingShoppingListPresenterListener {
        void onDeleteShopList();
        void onLoadShopList(String nameFragment, List<String> list);
    }
}
