package com.example.tancorik.shoppinglist.Data.FileData;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.tancorik.shoppinglist.ContextProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {

    private Context mContext;

    public FileManager() {
        ContextProvider contextProvider = ContextProvider.getInstance();
        mContext = contextProvider.getContext();
    }

    public void deleteFile(String fileName) {
        mContext.deleteFile(fileName);
    }

    public void save(@NonNull String fileName, String fileContent) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
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

    public String load(@NonNull String fileName) {
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
}
