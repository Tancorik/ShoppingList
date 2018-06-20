package com.example.tancorik.shoppinglist.Data.FileData;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.tancorik.shoppinglist.Data.SubjectForShopping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManager {

    public static final int FILE_MANAGER_ACTION_SAVE = 1;
    public static final int FILE_MANAGER_ACTION_LOAD = 2;
    public static final int FILE_MANAGER_ACRION_DELETE = 3;
    private Context mContext;

    public FileManager(Context context) {
        mContext = context;
    }

    public String doWork(@NonNull String fileName, @Nullable String fileContent, int action) {
        String result = "";
        switch (action) {
            case FILE_MANAGER_ACTION_SAVE :
                save(fileName, fileContent);
                break;
            case FILE_MANAGER_ACTION_LOAD :
                result = load(fileName);
                break;
            case FILE_MANAGER_ACRION_DELETE :
                mContext.deleteFile(fileName);
                break;
        }
        return result;
    }

    private void save(@NonNull String fileName, @Nullable String fileContent) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = mContext.openFileOutput(fileName, mContext.MODE_PRIVATE);
            if (fileContent != null) {
                fileOutputStream.write(fileContent.getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream!=null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String load(@NonNull String fileName) {
        String result = "";
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = mContext.openFileInput(fileName);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            result = new String(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


//    public void quit() {
//        mHandlerThread.quit();
//    }

//    public void loadFileList() {
//        new Handler(mLooper).post(new Runnable() {
//            @Override
//            public void run() {
//                String[] fileNameArray = mContext.fileList();
//                final List<String> fileList = new ArrayList<>();
//                for (String fileName : fileNameArray) {
//                    fileName = fileName.replace(".xml", "");
//                    fileList.add(fileName);
//                }
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mIFileManagerListener.onLoadFileList(fileList);
//                    }
//                });
//            }
//        });
//    }
//
//    public void delete(final String fileName) {
//        new Handler(mLooper).post(new Runnable() {
//            @Override
//            public void run() {
//                mContext.deleteFile(fileName);
//            }
//        });
//    }

//    public void save(final String fileName, final List<SubjectForShopping> shoppingList) {
//        new Handler(mLooper).post(new Runnable() {
//            @Override
//            public void run() {
//                String forSaveString;
//                if (shoppingList != null) {
//                    MyXMLParser myXMLParser = new MyXMLParser();
//                    forSaveString = myXMLParser.makeXMLString(makeMapListForXML(shoppingList), ENTRY_KEY);
//                }
//                else {
//                    forSaveString = "";
//                }
//                FileOutputStream fileOutputStream = null;
//                try {
//                    fileOutputStream = mContext.openFileOutput(fileName + ".xml", mContext.MODE_PRIVATE);
//                    fileOutputStream.write(forSaveString.getBytes());
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (fileOutputStream != null) {
//                            fileOutputStream.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }

//    public void load(final String fileName) {
//        new Handler(mLooper).post(new Runnable() {
//            @Override
//            public void run() {
//                String string = "";
//                FileInputStream fileInputStream = null;
//                try {
//                    fileInputStream = mContext.openFileInput(fileName + ".xml");
//                    byte[] bytes = new byte[fileInputStream.available()];
//                    fileInputStream.read(bytes);
//                    string = new String(bytes);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (fileInputStream != null) {
//                        try {
//                            fileInputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                if (!string.isEmpty()) {
//                    MyXMLParser myXMLParser = new MyXMLParser();
//                    List<Map<String, String>> mapList = myXMLParser.parseString(string, ENTRY_KEY, ID_KEY, COUNT_KEY);
//                    final List<SubjectForShopping> shoppingList = makeSubjectForShopping(mapList);
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mIFileManagerListener.onLoadFile(shoppingList);
//                        }
//                    });
//                }
//            }
//        });
//    }

//    private List<SubjectForShopping> makeSubjectForShopping (List<Map<String, String>> mapList) {
//        List<SubjectForShopping> shoppingList = new ArrayList<>();
//        for (Map<String, String> map : mapList) {
//            SubjectForShopping subject = new SubjectForShopping();
//            subject.setId(Integer.parseInt(map.get(ID_KEY)));
//            subject.setCount(Integer.parseInt(map.get(COUNT_KEY)));
//            shoppingList.add(subject);
//        }
//        return shoppingList;
//    }

//    private List<Map<String, String>> makeMapListForXML(List<SubjectForShopping> shoppingList) {
//        List<Map<String, String>> mapList = new ArrayList<>();
//        for (SubjectForShopping subject : shoppingList) {
//            Map<String, String> map = new HashMap<>();
//            map.put(ID_KEY, String.valueOf(subject.getId()));
//            map.put(COUNT_KEY, String.valueOf(subject.getCount()));
//            mapList.add(map);
//        }
//        return mapList;
//    }

}
