package com.example.tancorik.shoppinglist.Data.FileData;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyXMLParser {

    private static final String LOG_TAG = "MyXMLParserLog";

    public List<Map<String, String>> parseString(String stringForParse, String entryKey, String... keys) {
        List<String> entryList = parse(stringForParse, entryKey);
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String,String> stringMap;
        for (String string:entryList) {
            stringMap = new HashMap<>();
            for (String resultKey:keys) {
                List<String> result = parse(string, resultKey);
                if (result.size() == 1) {
                    stringMap.put(resultKey, result.get(0));
                }
                else {
                    Log.i(LOG_TAG, "Значений по ключу большу или нет вообще");
                }
            }
            mapList.add(stringMap);
        }
        return mapList;
    }

    public String makeXMLString(List<Map<String, String>> mapList, String entryKey) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map<String, String> map : mapList) {
            stringBuilder.append(bKey(entryKey));
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                stringBuilder.append(bKey(key));
                stringBuilder.append(map.get(key));
                stringBuilder.append(eKey(key));
            }
            stringBuilder.append(eKey(entryKey));
        }
        return stringBuilder.toString();
    }

    private String bKey(String key) {
        return "<" + key + ">";
    }

    private String eKey(String key) {
        return "</" + key + ">";
    }

    private List<String> parse(String forParseString, String key) {
        String beginKey = bKey(key);
        String endKey = eKey(key);
        List<String> stringList = new ArrayList<>();
        do {
            int begin = forParseString.indexOf(beginKey);
            if (begin<0) break;
            int end = forParseString.indexOf(endKey);
            String afterParsing = forParseString.substring(begin + beginKey.length(), end);
            forParseString = forParseString.replaceFirst(beginKey + afterParsing + endKey, "");
            stringList.add(afterParsing);
        } while (true);
        return stringList;
    }
}